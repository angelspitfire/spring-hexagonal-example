package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.dto.CampaignCreationDTO;
import ai.smartassets.challenge.aplication.dto.CampaignResponse;
import ai.smartassets.challenge.aplication.dto.CampaignUpdateDto;
import ai.smartassets.challenge.aplication.dto.CreativeUploadDTO;
import ai.smartassets.challenge.aplication.exception.BrandNotFoundException;
import ai.smartassets.challenge.aplication.exception.CampaignNotFoundException;
import ai.smartassets.challenge.aplication.exception.UpdateOperationException;
import ai.smartassets.challenge.aplication.port.in.ManageCampaignUseCase;
import ai.smartassets.challenge.aplication.port.out.*;
import ai.smartassets.challenge.domain.Brand;
import ai.smartassets.challenge.domain.Campaign;
import ai.smartassets.challenge.domain.Creative;
import ai.smartassets.challenge.infraestructure.persistence.model.CampaignEntity;
import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
import ai.smartassets.challenge.infraestructure.service.FileStorageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Validated
@Slf4j
public class ManageCampaignUseCaseImpl implements ManageCampaignUseCase {

    private final CampaignRepositoryPort campaignRepository;
    private final BrandRepositoryPort brandRepository;
    private final CreativeRepositoryPort creativeRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public ManageCampaignUseCaseImpl(CampaignRepositoryPort campaignRepository, BrandRepositoryPort brandRepository, CreativeRepositoryPort creativeRepository, FileStorageService fileStorageService) {
        this.campaignRepository = campaignRepository;
        this.brandRepository = brandRepository;
        this.creativeRepository = creativeRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Optional<CampaignResponse> getCampaignById(@NotBlank String id) {
        return campaignRepository.findById(id).map(ManageCampaignUseCaseImpl::getCampaign);
    }

    @Override
    public Optional<CampaignResponse> updateCampaign(@NotBlank String campaignId, @Valid CampaignUpdateDto campaignDto) {
        validateEntityExists(campaignRepository::existsById, campaignId, Campaign.class);
        return campaignRepository.findById(campaignId).map(campaignEntity -> {
            campaignEntity.setName(campaignDto.getName());
            campaignEntity.setDescription(campaignDto.getDescription());
            CampaignEntity savedEntity = campaignRepository.save(campaignEntity);

            if (savedEntity != null) {
                return Optional.of(convertToCampaignResponse(savedEntity));
            } else {
                throw new UpdateOperationException("Failed to update campaign with ID: " + campaignId);
            }

        }).orElse(Optional.empty());
    }

    @Override
    public boolean deleteCampaign(@NotBlank String id) {
        return campaignRepository.findById(id)
                .map(campaign -> {
                    campaignRepository.delete(campaign);
                    return true;
                }).orElse(false);
    }

    @Override
    public CampaignResponse createCampaignForBrand(@NotBlank String brandId, @Valid CampaignCreationDTO campaignDTO) {
        validateEntityExists(brandRepository::existsById, brandId, Brand.class);

        return brandRepository.findById(brandId)
                .map(brand -> {
                    CampaignEntity campaignEntity = getCampaignEntityFromDTO(brandId, campaignDTO);
                    return campaignRepository.save(campaignEntity);
                })
                .map(ManageCampaignUseCaseImpl::getCampaign)
                .orElseThrow(() -> notFoundException(Brand.class, brandId));
    }

    @Override
    public Optional<Creative> uploadCreativeForCampaign(@NotBlank String brandId, @NotBlank String campaignId, @Valid CreativeUploadDTO creativeUploadDTO) {
        validateEntityExists(brandRepository::existsById, brandId, Brand.class);
        validateEntityExists(campaignRepository::existsById, campaignId, Campaign.class);

        try {
            String fileLocation = storeCreativeFile(creativeUploadDTO.getFile());
            CreativeEntity creative = createCreativeEntity(creativeUploadDTO, fileLocation, campaignId);
            Creative savedCreative = convertToCreativeDomain(creativeRepository.save(creative));
            return Optional.of(savedCreative);
        } catch (Exception e) {
            log.error("Failed to upload creative for campaign: {}", campaignId, e);
            return Optional.empty();
        }
    }

    @Override
    public List<CampaignResponse> listCampaigns(PageRequest pageRequest) {
        return campaignRepository.findAll(pageRequest)
                .stream()
                .map(ManageCampaignUseCaseImpl::getCampaign)
                .toList();
    }

    @Override
    public List<CampaignResponse> findCampaignsByBrandId(@NotBlank String brandId, PageRequest pageRequest) {
        log.info("Starting to find campaigns for brandId: {}", brandId);
        List<CampaignResponse> campaigns = campaignRepository.findByBrandId(brandId, pageRequest)
                .stream()
                .map(ManageCampaignUseCaseImpl::getCampaign)
                .toList();
        log.info("Found {} campaigns for brandId: {}", campaigns.size(), brandId);
        return campaigns;
    }

    @Override
    public List<Creative> findCreativesByBrandIdAndCampaignId(@NotBlank  String brandId, @NotBlank  String campaignId, PageRequest pageRequest) {
        log.info("Finding creatives for brandId: {} and campaignId: {}", brandId, campaignId);
        validateEntityExists(brandRepository::existsById, brandId, Brand.class);
        validateEntityExists(campaignRepository::existsById, campaignId, Campaign.class);
        return creativeRepository.findByCampaignId(campaignId, pageRequest)
                .stream()
                .map(ManageCampaignUseCaseImpl::getCreative)
                .toList();
    }

    private CampaignResponse convertToCampaignResponse(CampaignEntity campaignEntity) {
        return new CampaignResponse(campaignEntity.getId(), campaignEntity.getName(), campaignEntity.getDescription());
    }

    private String storeCreativeFile(MultipartFile file) throws IOException {
        log.info("Storing file: {}", file.getOriginalFilename());
        return fileStorageService.storeFile(file);
    }

    private CreativeEntity createCreativeEntity(CreativeUploadDTO creativeUploadDTO, String fileLocation, String campaignId) {
        log.info("Creating creative entity for file: {}", fileLocation);
        return new CreativeEntity(null, creativeUploadDTO.getName(), creativeUploadDTO.getDescription(), fileLocation, campaignId);
    }

    private CampaignEntity getCampaignEntityFromDTO(String brandId, CampaignCreationDTO campaignDTO) {
        return new CampaignEntity(
                null,
                campaignDTO.getName(),
                campaignDTO.getDescription(),
                brandId);
    }

    private Creative convertToCreativeDomain(CreativeEntity savedCreative) {
        return new Creative(savedCreative.getId(),
                savedCreative.getName(),
                savedCreative.getDescription(),
                savedCreative.getCreativeUrl(),
                savedCreative.getCampaignId());
    }

    private static CampaignResponse getCampaign(CampaignEntity campaignEntity) {
        return new CampaignResponse(campaignEntity.getId(), campaignEntity.getName(), campaignEntity.getDescription());
    }

    private static Creative getCreative(CreativeEntity e) {
        return new Creative(e.getId(), e.getName(), e.getDescription(), e.getCreativeUrl(), e.getCampaignId());
    }

    private <T> void validateEntityExists(Function<String, Boolean> existsByIdFunction, String id, Class<T> entityType) {
        if (!existsByIdFunction.apply(id)) {
            throw notFoundException(entityType, id);
        }
    }

    private RuntimeException notFoundException(Class<?> entityType, String id) {
        String entityName = entityType.getSimpleName();
        String errorMessage = String.format("%s with id %s not found", entityName, id);
        log.error(errorMessage);
        return switch (entityName) {
            case "Brand" -> new BrandNotFoundException(errorMessage);
            case "Campaign" -> new CampaignNotFoundException(errorMessage);
            default -> new IllegalArgumentException("Unhandled entity type: " + entityName);
        };
    }
}