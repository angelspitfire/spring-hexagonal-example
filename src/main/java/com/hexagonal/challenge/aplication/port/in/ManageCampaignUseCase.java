package com.hexagonal.challenge.aplication.port.in;

import com.hexagonal.challenge.aplication.dto.CampaignCreationDTO;
import com.hexagonal.challenge.aplication.dto.CampaignUpdateDto;
import com.hexagonal.challenge.aplication.dto.CreativeUploadDTO;
import com.hexagonal.challenge.domain.Campaign;
import com.hexagonal.challenge.domain.Creative;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ManageCampaignUseCase {

    List<Campaign> listCampaigns(PageRequest pageRequest);

    Optional<Campaign> getCampaignById(String campaignId);;

    Optional<Campaign> updateCampaign(@NotNull @NotBlank String campaignId, @Valid CampaignUpdateDto campaignDto);

    boolean deleteCampaign(String campaignId);

    Campaign createCampaignForBrand(String brandId, @Valid CampaignCreationDTO campaignDTO);

    List<Campaign> findCampaignsByBrandId(String brandId, PageRequest pageRequest);

    List<Creative> findCreativesByBrandIdAndCampaignId(String brandId, String campaignId, PageRequest pageRequest);

    Optional<Creative> uploadCreativeForCampaign(String brandId, String campaignId, CreativeUploadDTO creativeUploadDTO);
}
