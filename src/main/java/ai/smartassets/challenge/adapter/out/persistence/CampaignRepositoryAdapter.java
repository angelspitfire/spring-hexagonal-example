package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.aplication.port.out.CampaignRepository;
import ai.smartassets.challenge.aplication.port.out.CampaignRepositoryPort;
import ai.smartassets.challenge.domain.Campaign;
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
        return campaignRepository.findAll(pageable);
    }

    @Override
    public Campaign save(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @Override
    public Optional<Campaign> findById(String id) {
        return campaignRepository.findById(id);
    }

    @Override
    public void delete(Campaign campaign) {
        campaignRepository.delete(campaign);
    }

    @Override
    public void deleteById(String id) {
        campaignRepository.deleteById(id);
    }
}
