package ai.smartassets.challenge.aplication.port.in;

import ai.smartassets.challenge.aplication.dto.CreativeUploadDTO;
import ai.smartassets.challenge.domain.Campaign;
import ai.smartassets.challenge.domain.Creative;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ManageCampaignUseCase {
    Campaign createCampaign(String brandId, Campaign campaign) ;

    List<Campaign> listCampaigns(PageRequest pageRequest);

    Optional<Campaign> getCampaignById(String campaignId);

    Optional<Campaign> updateCampaign(String campaignId, Campaign campaign);

    boolean deleteCampaign(String campaignId);

    Campaign createCampaignForBrand(String brandId, Campaign campaign);

    List<Campaign> findCampaignsByBrandId(String brandId, PageRequest pageRequest);

    List<Creative> findCreativesByBrandIdAndCampaignId(String brandId, String campaignId, PageRequest pageRequest);

    Creative uploadCreativeForCampaign(String brandId, String campaignId, CreativeUploadDTO creativeUploadDTO);
}
