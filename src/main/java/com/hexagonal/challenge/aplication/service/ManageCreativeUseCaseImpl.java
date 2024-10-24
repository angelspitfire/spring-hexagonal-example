package com.hexagonal.challenge.aplication.service;

import com.hexagonal.challenge.aplication.dto.CreativeDTO;
import com.hexagonal.challenge.aplication.dto.CreativeUpdateDTO;
import com.hexagonal.challenge.aplication.exception.CreativeNotFoundException;
import com.hexagonal.challenge.aplication.port.in.ManageCreativeUseCase;
import com.hexagonal.challenge.aplication.port.out.CreativeRepositoryPort;
import com.hexagonal.challenge.domain.Creative;
import com.hexagonal.challenge.infraestructure.persistence.model.CreativeEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Validated
public class ManageCreativeUseCaseImpl implements ManageCreativeUseCase {

    private final CreativeRepositoryPort creativeRepository;

    @Autowired
    public ManageCreativeUseCaseImpl(CreativeRepositoryPort creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public Creative createCreative(@Valid CreativeDTO creativeDTO) {

        try {
            log.info("Creating a new creative: {}", creativeDTO.getName());

            CreativeEntity entity = getEntityFromDTO(creativeDTO);
            CreativeEntity savedEntity = creativeRepository.save(entity);

            log.info("Successfully created creative with ID: {}", savedEntity.getId());

            return convertToCreativeDomain(savedEntity);
        } catch (Exception e) {
            log.error("Error creating creative: {}", e.getMessage());
            throw new RuntimeException("Failed to create creative", e);
        }
    }

    @Override
    public List<Creative> listCreatives(PageRequest pageRequest) {
        return creativeRepository.findAll(pageRequest).stream().map(ManageCreativeUseCaseImpl::getCreative).toList();
    }

    @Override
    public Optional<Creative> getCreativeById(@NotBlank String creativeId) {
        return creativeRepository.findById(creativeId).map(ManageCreativeUseCaseImpl::getCreative);
    }

    @Override
    public Optional<Creative> updateCreative(@NotBlank String creativeId, CreativeUpdateDTO creative) {

        CreativeEntity creativeEntity = creativeRepository.findById(creativeId)
                .orElseThrow(() -> new CreativeNotFoundException("Creative with id " + creativeId + " not found."));

        creativeEntity.setName(creative.getName());
        creativeEntity.setDescription(creative.getDescription());
        creativeEntity.setCreativeUrl(creative.getCreativeUrl());
        CreativeEntity updatedEntity = creativeRepository.save(creativeEntity);

        return Optional.of(getCreative(updatedEntity));
    }

    @Override
    public boolean deleteCreative(@NotBlank String creativeId) {
        return creativeRepository.findById(creativeId)
                .map(creative -> {
                    creativeRepository.delete(creative);
                    return true;
                }).orElse(false);
    }

    private Creative convertToCreativeDomain(CreativeEntity savedEntity) {
        return new Creative(savedEntity.getId(),
                savedEntity.getName(),
                savedEntity.getDescription(),
                savedEntity.getCreativeUrl(),
                savedEntity.getCampaignId());
    }

    private CreativeEntity getEntityFromDTO(CreativeDTO creativeDTO) {
        return new CreativeEntity(
                null,
                creativeDTO.getName(),
                creativeDTO.getDescription(),
                creativeDTO.getCreativeUrl(),
                creativeDTO.getCampaignId());
    }

    private static Creative getCreative(CreativeEntity creativeEntity) {
        return new Creative(creativeEntity.getId(), creativeEntity.getName(), creativeEntity.getDescription(), creativeEntity.getCreativeUrl(), creativeEntity.getCampaignId());
    }
}
