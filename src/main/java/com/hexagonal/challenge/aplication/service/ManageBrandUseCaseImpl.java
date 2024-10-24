package com.hexagonal.challenge.aplication.service;

import com.hexagonal.challenge.aplication.dto.BrandCreationDto;
import com.hexagonal.challenge.aplication.dto.BrandUpdateDto;
import com.hexagonal.challenge.aplication.exception.BrandCreationException;
import com.hexagonal.challenge.aplication.exception.BrandNotFoundException;
import com.hexagonal.challenge.aplication.port.in.ManageBrandUseCase;
import com.hexagonal.challenge.aplication.port.out.BrandRepositoryPort;
import com.hexagonal.challenge.domain.Brand;
import com.hexagonal.challenge.infraestructure.persistence.model.BrandEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Validated
public class ManageBrandUseCaseImpl implements ManageBrandUseCase {

    private final BrandRepositoryPort brandRepository;

    public ManageBrandUseCaseImpl(BrandRepositoryPort brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand createBrand(@Valid BrandCreationDto brandCreationDto) {
        log.info("Attempting to create brand: {}", brandCreationDto.getName());
        try {
            BrandEntity newBrandEntity = mapToBrandEntity(brandCreationDto);
            BrandEntity savedBrandEntity = brandRepository.save(newBrandEntity);
            log.info("Brand created successfully with ID: {}", savedBrandEntity.getId());
            return mapToBrand(savedBrandEntity);
        } catch (DataAccessException e) {
            log.error("Brand creation failed for brand: {}. Error: {}", brandCreationDto.getName(), e.getMessage(), e);
            throw new BrandCreationException("Unable to create brand due to a database error. Please try again later.", e);
        } catch (Exception e) {
            log.error("Unexpected error during brand creation for brand: {}. Error: {}", brandCreationDto.getName(), e.getMessage(), e);
            throw new BrandCreationException("An unexpected error occurred while creating the brand. Please try again.", e);
        }
    }

    @Override
    public List<Brand> listBrands(Pageable pageable) {
        return brandRepository.findAll(pageable).stream().map(this::mapToBrand).toList();
    }

    @Override
    public Optional<Brand> getBrandById(@NotBlank  String id) {
        return brandRepository.findById(id).map(this::mapToBrand);
    }

    @Override
    public Optional<Brand> updateBrand(@NotBlank String id, @Valid BrandUpdateDto brandDto) {
        return brandRepository.findById(id).map(brandEntity -> {
            brandEntity.setName(brandDto.getName());
            brandEntity.setDescription(brandDto.getDescription());
            return mapToBrand(brandRepository.save(brandEntity));
        });
    }

    @Override
    public boolean deleteBrand(@NotBlank  String id) {
        checkBrandExistence(id);
        brandRepository.deleteById(id);
        log.info("Brand with id {} deleted.", id);
        return true;
    }

    private void checkBrandExistence(String id) {
        if (!brandRepository.existsById(id)) {
            throw new BrandNotFoundException("Brand with id " + id + " not found.");
        }
    }

    private BrandEntity mapToBrandEntity(BrandCreationDto brandCreationDto) {
        return new BrandEntity(null, brandCreationDto.getName(), brandCreationDto.getDescription());
    }

    private Brand mapToBrand(BrandEntity brandEntity) {
        return new Brand(brandEntity.getId(), brandEntity.getName(), brandEntity.getDescription());
    }
}