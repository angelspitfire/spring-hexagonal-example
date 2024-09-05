package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.dto.CreativeDTO;
import ai.smartassets.challenge.aplication.dto.CreativeResponse;
import ai.smartassets.challenge.aplication.dto.CreativeUpdateDTO;
import ai.smartassets.challenge.aplication.exception.CreativeNotFoundException;
import ai.smartassets.challenge.aplication.port.in.ManageCreativeUseCase;
import ai.smartassets.challenge.aplication.port.out.CreativeRepositoryPort;
import ai.smartassets.challenge.domain.Creative;
import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
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
    public CreativeResponse createCreative(@Valid CreativeDTO creativeDTO) {

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
    public List<CreativeResponse> listCreatives(PageRequest pageRequest) {
        return creativeRepository.findAll(pageRequest).stream().map(ManageCreativeUseCaseImpl::getCreative).toList();
    }

    @Override
    public Optional<CreativeResponse> getCreativeById(@NotBlank String creativeId) {
        return creativeRepository.findById(creativeId).map(ManageCreativeUseCaseImpl::getCreative);
    }

    @Override
    public Optional<CreativeResponse> updateCreative(@NotBlank String creativeId, CreativeUpdateDTO creative) {

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

    private CreativeResponse convertToCreativeDomain(CreativeEntity savedEntity) {
        return new CreativeResponse(savedEntity.getId(),
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

    private static CreativeResponse getCreative(CreativeEntity creativeEntity) {
        return new CreativeResponse(creativeEntity.getId(), creativeEntity.getName(), creativeEntity.getDescription(), creativeEntity.getCreativeUrl(), creativeEntity.getCampaignId());
    }
}
