package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.aplication.port.out.CampaignRepository;
import ai.smartassets.challenge.aplication.port.out.CampaignRepositoryPort;
import ai.smartassets.challenge.domain.Campaign;
import ai.smartassets.challenge.infraestructure.persistence.model.CampaignEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class CampaignRepositoryAdapter implements CampaignRepositoryPort {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignRepositoryAdapter(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public Page<Campaign> findAll(Pageable pageable) {
        return campaignRepository.findAll(pageable).map(CampaignRepositoryAdapter::getCampaign);
    }

    @Override
    public Campaign save(Campaign campaign) {
        CampaignEntity entity = getEntity(campaign);
        return getCampaign(campaignRepository.save(entity));
    }

    @Override
    public Optional<Campaign> findById(String id) {
        return campaignRepository.findById(id).map(CampaignRepositoryAdapter::getCampaign);
    }

    @Override
    public void delete(Campaign campaign) {
        campaignRepository.delete(getEntity(campaign));
    }

    @Override
    public void deleteById(String id) {
        campaignRepository.deleteById(id);
    }

    private static Campaign getCampaign(CampaignEntity campaign) {
        return new Campaign(campaign.getId(), campaign.getName(), campaign.getDescription());
    }

    private static CampaignEntity getEntity(Campaign campaign) {
        return new CampaignEntity(campaign.getCampaignId(), campaign.getName(), campaign.getDescription());
    }
}
