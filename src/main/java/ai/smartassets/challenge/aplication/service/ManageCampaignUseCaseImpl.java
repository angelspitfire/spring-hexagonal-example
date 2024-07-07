package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.port.in.ManageCampaignUseCase;
import ai.smartassets.challenge.aplication.port.out.CampaignRepository;
import ai.smartassets.challenge.domain.Campaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManageCampaignUseCaseImpl implements ManageCampaignUseCase {

    private final CampaignRepository campaignRepository;

    @Autowired
    public ManageCampaignUseCaseImpl(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public Campaign createCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @Override
    public Page<Campaign> listCampaigns(PageRequest pageRequest) {
        return campaignRepository.findAll(pageRequest);
    }

    @Override
    public Optional<Campaign> getCampaignById(String id) {
        return campaignRepository.findById(id);
    }

    @Override
    public Optional<Campaign> updateCampaign(String campaignId, Campaign campaign) {
        return campaignRepository.findById(campaignId)
                .map(existingCampaign -> {
                    // Update properties of existingCampaign with values from campaign
                    // This is a simplified example. You should set each property you want to update individually.
                    existingCampaign.setName(campaign.getName());
                    existingCampaign.setDescription(campaign.getDescription());
                    // Add more fields to update as necessary
                    return campaignRepository.save(existingCampaign);
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
}