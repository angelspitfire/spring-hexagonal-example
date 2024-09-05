package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.dto.CampaignResponse;
import ai.smartassets.challenge.aplication.dto.CampaignUpdateRequest;
import ai.smartassets.challenge.aplication.dto.CreativeUploadRequest;
import ai.smartassets.challenge.aplication.exception.BrandNotFoundException;
import ai.smartassets.challenge.aplication.exception.CampaignNotFoundException;
import ai.smartassets.challenge.aplication.port.in.ManageCampaignUseCase;
import ai.smartassets.challenge.aplication.port.out.*;
import ai.smartassets.challenge.domain.Campaign;
import ai.smartassets.challenge.domain.Creative;
import ai.smartassets.challenge.infraestructure.persistence.model.CampaignEntity;
import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
import ai.smartassets.challenge.infraestructure.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
    public CampaignResponse createCampaign(String brandId, Campaign campaign) {

        if (brandId == null || brandId.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand ID cannot be null or empty");
        }

        if (campaign == null || campaign.getName() == null || campaign.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Campaign details are not valid");
        }

        brandRepository.findById(brandId).orElseThrow(() -> {
            log.error("Brand with id {} not found", brandId);
            return new BrandNotFoundException("Brand with id " + brandId + " not found");
        });

        log.info("Creating campaign for brandId {}: {}", brandId, campaign);
        CampaignEntity entity = getEntity(campaign, brandId);
        CampaignEntity savedEntity = campaignRepository.save(entity);
        return getCampaign(savedEntity);
    }

    @Override
    public List<CampaignResponse> listCampaigns(PageRequest pageRequest) {
        return campaignRepository.findAll(pageRequest).map(ManageCampaignUseCaseImpl::getCampaign).toList();
    }

    @Override
    public Optional<CampaignResponse> getCampaignById(String id) {
        return campaignRepository.findById(id).map(ManageCampaignUseCaseImpl::getCampaign);
    }

    @Override
    public Optional<CampaignResponse> updateCampaign(String campaignId, CampaignUpdateRequest campaign) {
        return campaignRepository.findById(campaignId).map(campaignEntity -> {
            campaignEntity.setName(campaign.name());
            campaignEntity.setDescription(campaign.description());
            return getCampaign(campaignRepository.save(campaignEntity));
        });
    }

    @Override
    public boolean deleteCampaign(String id) {
        return campaignRepository.findById(id)
                .map(campaign -> {
                    campaignRepository.delete(campaign);
                    return true;
                }).orElse(false);
    }

    @Override
    public CampaignResponse createCampaignForBrand(String brandId, Campaign campaign) {
        return brandRepository.findById(brandId)
                .map(brand -> {
                    CampaignEntity campaignEntity = getCampaignEntity(brandId, campaign);
                    return campaignRepository.save(campaignEntity);
                })
                .map(ManageCampaignUseCaseImpl::getCampaign)
                .orElseThrow(() -> new BrandNotFoundException("Brand with id " + brandId + " not found"));
    }

    @Override
    public List<CampaignResponse> findCampaignsByBrandId(String brandId, PageRequest pageRequest) {
        validateBrandIdForExistence(brandId);

        log.info("Starting to find campaigns for brandId: {}", brandId);

        List<CampaignResponse> campaigns = campaignRepository.findByBrandId(brandId, pageRequest)
                .stream()
                .map(ManageCampaignUseCaseImpl::getCampaign)
                .toList();

        log.info("Found {} campaigns for brandId: {}", campaigns.size(), brandId);

        return campaigns;
    }

    @Override
    public List<Creative> findCreativesByBrandIdAndCampaignId(String brandId, String campaignId, PageRequest pageRequest) {

        if (brandId == null || brandId.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand ID cannot be null or empty");
        }

        if (campaignId == null || campaignId.trim().isEmpty()) {
            throw new IllegalArgumentException("Campaign ID cannot be null or empty");
        }

        brandRepository.findById(brandId).orElseThrow(() -> {
            log.error("Brand with id {} not found", brandId); // Logging
            return new BrandNotFoundException("Brand with id " + brandId + " not found");
        });

        campaignRepository.findById(campaignId).orElseThrow(() -> {
            log.error("Campaign with id {} not found", campaignId); // Logging
            return new CampaignNotFoundException("Campaign with id " + campaignId + " not found");
        });

        log.info("Starting to find creatives for brandId: {} and campaignId: {}", brandId, campaignId); // Logging

        return creativeRepository.findByCampaignId(campaignId, pageRequest)
                .stream()
                .map(ManageCampaignUseCaseImpl::getCreative)
                .toList();
    }

    @Override
    public Creative uploadCreativeForCampaign(String brandId, String campaignId, CreativeUploadRequest creativeUploadRequest) {

        brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException("Brand with id " + brandId + " not found"));

        campaignRepository.findById(campaignId)
                .orElseThrow(() -> new CampaignNotFoundException("Campaign with id " + campaignId + " not found"));

        String fileLocation = fileStorageService.storeFile(creativeUploadRequest.file());

        CreativeEntity creative = new CreativeEntity();
        creative.setName(creativeUploadRequest.name());
        creative.setDescription(creativeUploadRequest.description());
        creative.setCreativeUrl(fileLocation);
        creative.setCampaignId(campaignId);

        CreativeEntity savedCreative = creativeRepository.save(creative);

        return convertToCreativeDomain(savedCreative);
    }

    private Creative convertToCreativeDomain(CreativeEntity savedCreative) {
        return new Creative(savedCreative.getId(),
                savedCreative.getName(),
                savedCreative.getDescription(),
                savedCreative.getCreativeUrl(),
                savedCreative.getCampaignId());
    }

    private void validateBrandIdForExistence(String brandId) {
        validateBrandIdNotEmpty(brandId);
        ensureBrandExists(brandId);
    }

    private void validateBrandIdNotEmpty(String brandId) {
        if (brandId == null || brandId.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand ID cannot be null or empty.");
        }
    }

    private void ensureBrandExists(String brandId) {
        boolean brandExists = brandRepository.existsById(brandId);
        if (!brandExists) {
            String errorMessage = String.format("Brand with id %s not found.", brandId);
            log.error(errorMessage);
            throw new BrandNotFoundException(errorMessage);
        }
    }

    private static CampaignResponse getCampaign(CampaignEntity campaignEntity) {
        return new CampaignResponse(campaignEntity.getId(), campaignEntity.getName(), campaignEntity.getDescription());
    }

    private static Creative getCreative(CreativeEntity e) {
        return new Creative(e.getId(), e.getName(), e.getDescription(), e.getCreativeUrl(), e.getCampaignId());
    }

    private static CampaignEntity getCampaignEntity(String brandId, Campaign campaign) {
        return new CampaignEntity(
                campaign.getCampaignId(),
                campaign.getName(),
                campaign.getDescription(),
                brandId);
    }

    private static CampaignEntity getEntity(Campaign campaign, String brandId) {
        return new CampaignEntity(campaign.getCampaignId(), campaign.getName(), campaign.getDescription(), brandId);
    }
}