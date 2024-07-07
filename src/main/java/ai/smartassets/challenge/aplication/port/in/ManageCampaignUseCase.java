package ai.smartassets.challenge.aplication.port.in;

import ai.smartassets.challenge.domain.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface ManageCampaignUseCase {
    Campaign createCampaign(Campaign campaign);

    Page<Campaign> listCampaigns(PageRequest pageRequest);

    Optional<Campaign> getCampaignById(String campaignId);

    Optional<Campaign> updateCampaign(String campaignId, Campaign campaign);

    boolean deleteCampaign(String campaignId);
}