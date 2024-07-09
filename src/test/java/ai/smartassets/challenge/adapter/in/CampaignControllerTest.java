package ai.smartassets.challenge.adapter.in;

import ai.smartassets.challenge.aplication.dto.CampaignUpdateDto;
import ai.smartassets.challenge.aplication.port.in.ManageCampaignUseCase;
import ai.smartassets.challenge.domain.Campaign;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CampaignController.class)
class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManageCampaignUseCase manageCampaignUseCase;

    @Test
    void listCampaigns_Success() throws Exception {
        List<Campaign> expectedCampaigns = List.of(
                new Campaign("1", "CampaignName1", "Description1"),
                new Campaign("2", "CampaignName2", "Description2")
        );
        when(manageCampaignUseCase.listCampaigns(PageRequest.of(0, 10))).thenReturn(expectedCampaigns);

        mockMvc.perform(get("/campaigns?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].campaignId").value(expectedCampaigns.get(0).getCampaignId()))
                .andExpect(jsonPath("$[1].campaignId").value(expectedCampaigns.get(1).getCampaignId()));
    }

    @Test
    void getCampaignById_Success() throws Exception {
        Campaign expectedCampaign = new Campaign("1", "CampaignName", "Description");
        when(manageCampaignUseCase.getCampaignById(eq("1"))).thenReturn(Optional.of(expectedCampaign));

        mockMvc.perform(get("/campaigns/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.campaignId").value(expectedCampaign.getCampaignId()));
    }

    @Test
    void updateCampaign_Success() throws Exception {
        CampaignUpdateDto campaignUpdateDto = new CampaignUpdateDto("UpdatedCampaignName", "UpdatedDescription");
        Campaign updatedCampaign = new Campaign("1", "UpdatedCampaignName", "UpdatedDescription");
        when(manageCampaignUseCase.updateCampaign(eq("1"), any(CampaignUpdateDto.class))).thenReturn(Optional.of(updatedCampaign));

        mockMvc.perform(patch("/campaigns/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(campaignUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.campaignId").value(updatedCampaign.getCampaignId()))
                .andExpect(jsonPath("$.name").value(updatedCampaign.getName()))
                .andExpect(jsonPath("$.description").value(updatedCampaign.getDescription()));
    }

    @Test
    void deleteCampaign_Success() throws Exception {
        when(manageCampaignUseCase.deleteCampaign("1")).thenReturn(true);

        mockMvc.perform(delete("/campaigns/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCampaign_NotFound() throws Exception {
        when(manageCampaignUseCase.deleteCampaign("nonExistingCampaignId")).thenReturn(false);

        mockMvc.perform(delete("/campaigns/nonExistingCampaignId"))
                .andExpect(status().isNotFound());
    }
}