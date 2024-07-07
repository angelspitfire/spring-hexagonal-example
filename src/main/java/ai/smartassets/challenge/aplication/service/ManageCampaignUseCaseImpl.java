package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.exception.BrandNotFoundException;
import ai.smartassets.challenge.aplication.exception.CampaignNotFoundException;
import ai.smartassets.challenge.aplication.port.in.ManageCampaignUseCase;
import ai.smartassets.challenge.aplication.port.out.BrandRepository;
import ai.smartassets.challenge.aplication.port.out.CampaignRepository;
import ai.smartassets.challenge.aplication.port.out.CreativeRepository;
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

@Service @Slf4j
public class ManageCampaignUseCaseImpl implements ManageCampaignUseCase {

    private final CampaignRepository campaignRepository;
    private final BrandRepository brandRepository;
    private final CreativeRepository creativeRepository;

    @Autowired
    public ManageCampaignUseCaseImpl(CampaignRepository campaignRepository, BrandRepository brandRepository, CreativeRepository creativeRepository) {
        this.campaignRepository = campaignRepository;
        this.brandRepository = brandRepository;
        this.creativeRepository = creativeRepository;
    }

    @Override
    public Campaign createCampaign(Campaign campaign) {

        CampaignEntity entity = getEntity(campaign, null);
        return getCampaign(campaignRepository.save(entity));
    }

    @Override
    public List<Campaign> listCampaigns(PageRequest pageRequest) {
        return campaignRepository.findAll(pageRequest).map(ManageCampaignUseCaseImpl::getCampaign).toList();
    }

    private static Campaign getCampaign(CampaignEntity campaignEntity) {
        return new Campaign(campaignEntity.getId(), campaignEntity.getName(), campaignEntity.getDescription());
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
    public List<Campaign> findCampaignsByBrandId(String brandId) {
        return campaignRepository.findByBrandId(brandId).stream()
                .map(ManageCampaignUseCaseImpl::getCampaign)
                .toList();
    }

    @Override
    public List<Creative> findCreativesByBrandIdAndCampaignId(String brandId, String campaignId) {

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

        CampaignEntity campaignEntity = campaignRepository.findByBrandIdAndId(brandId, campaignId)
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