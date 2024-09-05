package ai.smartassets.challenge.aplication.port.in;

import ai.smartassets.challenge.aplication.dto.CampaignResponse;
import ai.smartassets.challenge.aplication.dto.CampaignUpdateRequest;
import ai.smartassets.challenge.aplication.dto.CreativeUploadRequest;
import ai.smartassets.challenge.domain.Campaign;
import ai.smartassets.challenge.domain.Creative;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ManageCampaignUseCase {
    CampaignResponse createCampaign(String brandId, Campaign campaign) ;

    List<CampaignResponse> listCampaigns(PageRequest pageRequest);

    Optional<CampaignResponse> getCampaignById(String campaignId);

    Optional<CampaignResponse> updateCampaign(String campaignId, CampaignUpdateRequest campaign);

    boolean deleteCampaign(String campaignId);

    CampaignResponse createCampaignForBrand(String brandId, Campaign campaign);

    List<CampaignResponse> findCampaignsByBrandId(String brandId, PageRequest pageRequest);

    List<Creative> findCreativesByBrandIdAndCampaignId(String brandId, String campaignId, PageRequest pageRequest);

    Creative uploadCreativeForCampaign(String brandId, String campaignId, CreativeUploadRequest creativeUploadRequest);
}
