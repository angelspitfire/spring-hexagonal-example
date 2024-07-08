package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.exception.BrandNotFoundException;
import ai.smartassets.challenge.aplication.exception.CampaignNotFoundException;
import ai.smartassets.challenge.aplication.port.in.ManageCampaignUseCase;
import ai.smartassets.challenge.aplication.port.out.*;
import ai.smartassets.challenge.domain.Campaign;
import ai.smartassets.challenge.domain.Creative;
import ai.smartassets.challenge.infraestructure.persistence.model.CampaignEntity;
import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
@Slf4j
public class ManageCampaignUseCaseImpl implements ManageCampaignUseCase {

    private final CampaignRepositoryPort campaignRepository;
    private final BrandRepositoryPort brandRepository;
    private final CreativeRepositoryPort creativeRepository;

    @Autowired
    public ManageCampaignUseCaseImpl(CampaignRepositoryPort campaignRepository, BrandRepositoryPort brandRepository, CreativeRepositoryPort creativeRepository) {
        this.campaignRepository = campaignRepository;
        this.brandRepository = brandRepository;
        this.creativeRepository = creativeRepository;
    }

    @Override
    public Campaign createCampaign(String brandId, Campaign campaign) {

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
    public List<Campaign> listCampaigns(PageRequest pageRequest) {
        return campaignRepository.findAll(pageRequest).map(ManageCampaignUseCaseImpl::getCampaign).toList();
    }

    @Override
    public Optional<Campaign> getCampaignById(String id) {
        return campaignRepository.findById(id).map(ManageCampaignUseCaseImpl::getCampaign);
    }

    @Override
    public Optional<Campaign> updateCampaign(String campaignId, Campaign campaign) {
        return campaignRepository.findById(campaignId).map(campaignEntity -> {
            campaignEntity.setName(campaign.getName());
            campaignEntity.setDescription(campaign.getDescription());
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
    public Campaign createCampaignForBrand(String brandId, Campaign campaign) {
        return brandRepository.findById(brandId)
                .map(brand -> {
                    CampaignEntity campaignEntity = getCampaignEntity(brandId, campaign);
                    return campaignRepository.save(campaignEntity);
                })
                .map(ManageCampaignUseCaseImpl::getCampaign)
                .orElseThrow(() -> new BrandNotFoundException("Brand with id " + brandId + " not found"));
    }

    @Override
    public List<Campaign> findCampaignsByBrandId(String brandId, PageRequest pageRequest) {
        validateBrandIdForExistence(brandId);

        log.info("Starting to find campaigns for brandId: {}", brandId);

        List<Campaign> campaigns = campaignRepository.findByBrandId(brandId, pageRequest)
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

        CampaignEntity campaignEntity = campaignRepository.findByBrandIdAndId(brandId, campaignId, pageRequest)
                .stream()
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Campaign with id {} not found or does not belong to brand with id {}", campaignId, brandId); // Logging
                    return new CampaignNotFoundException("Campaign with id " + campaignId + " not found or does not belong to brand with id " + brandId);
                });

        List<CreativeEntity> creativeEntities = creativeRepository.findByCampaignId(campaignId);
        log.info("Found {} creatives for campaignId {}", creativeEntities.size(), campaignId); // Logging
        return creativeEntities.stream()
                .map(ManageCampaignUseCaseImpl::getCreative)
                .toList();
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

    private static Campaign getCampaign(CampaignEntity campaignEntity) {
        return new Campaign(campaignEntity.getId(), campaignEntity.getName(), campaignEntity.getDescription());
    }

    private Function<CampaignEntity, Stream<? extends CreativeEntity>> getCampaignEntity() {
        return campaignEntity -> creativeRepository.findByCampaignId(campaignEntity.getId()).stream();
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