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

    public ManageCreativeUseCaseImpl(CreativeRepositoryPort creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public Creative createCreative(@Valid CreativeDTO creativeDTO) {
        log.info("Creating a new creative: {}", creativeDTO.getName());
        var entity = getEntityFromDTO(creativeDTO);
        var savedEntity = creativeRepository.save(entity);
        log.info("Successfully created creative with ID: {}", savedEntity.getId());
        return convertToCreativeDomain(savedEntity);
    }

    @Override
    public List<Creative> listCreatives(PageRequest pageRequest) {
        return creativeRepository.findAll(pageRequest)
                .stream()
                .map(this::convertToCreativeDomain)
                .toList();
    }

    @Override
    public Optional<Creative> getCreativeById(@NotBlank String creativeId) {
        return creativeRepository.findById(creativeId)
                .map(this::convertToCreativeDomain);
    }

    @Override
    public Optional<Creative> updateCreative(@NotBlank String creativeId, @Valid CreativeUpdateDTO creative) {
        var creativeEntity = creativeRepository.findById(creativeId)
                .orElseThrow(() -> new CreativeNotFoundException("Creative with id " + creativeId + " not found."));
        updateCreativeEntity(creativeEntity, creative);
        var updatedEntity = creativeRepository.save(creativeEntity);
        return Optional.of(convertToCreativeDomain(updatedEntity));
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
        return Creative.builder()
                .creativeId(savedEntity.getId())
                .name(savedEntity.getName())
                .description(savedEntity.getDescription())
                .creativeUrl(savedEntity.getCreativeUrl())
                .campaignId(savedEntity.getCampaignId())
                .build();
    }

    private CreativeEntity getEntityFromDTO(CreativeDTO creativeDTO) {
        return new CreativeEntity(
                null,
                creativeDTO.getName(),
                creativeDTO.getDescription(),
                creativeDTO.getCreativeUrl(),
                creativeDTO.getCampaignId());
    }

    private void updateCreativeEntity(CreativeEntity creativeEntity, CreativeUpdateDTO creative) {
        creativeEntity.setName(creative.getName());
        creativeEntity.setDescription(creative.getDescription());
        creativeEntity.setCreativeUrl(creative.getCreativeUrl());
    }
}