package ai.smartassets.challenge.adapter.in;

import ai.smartassets.challenge.aplication.dto.CreativeResponse;
import ai.smartassets.challenge.aplication.dto.CreativeUpdateDTO;
import ai.smartassets.challenge.aplication.port.in.ManageCreativeUseCase;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreativeController.class)
class CreativeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManageCreativeUseCase manageCreativeUseCase;

    @Test
    void listCreatives_Success() throws Exception {
        List<CreativeResponse> expectedCreatives = List.of(
                new CreativeResponse("1", "CreativeName1", "Description1", "ImageURL1", "campaignId1"),
                new CreativeResponse("2", "CreativeName2", "Description2", "ImageURL2", "campaignId2")
        );

        when(manageCreativeUseCase.listCreatives(PageRequest.of(0, 10))).thenReturn(expectedCreatives);

        mockMvc.perform(get("/creatives?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].creativeId").value(expectedCreatives.get(0).creativeId()))
                .andExpect(jsonPath("$[0].name").value(expectedCreatives.get(0).name()))
                .andExpect(jsonPath("$[0].description").value(expectedCreatives.get(0).description()))
                .andExpect(jsonPath("$[0].creativeUrl").value(expectedCreatives.get(0).creativeUrl()))
                .andExpect(jsonPath("$[1].creativeId").value(expectedCreatives.get(1).creativeId()))
                .andExpect(jsonPath("$[1].name").value(expectedCreatives.get(1).name()))
                .andExpect(jsonPath("$[1].description").value(expectedCreatives.get(1).description()))
                .andExpect(jsonPath("$[1].creativeUrl").value(expectedCreatives.get(1).creativeUrl()));
    }

    @Test
    void getCreativeById_Success() throws Exception {
        CreativeResponse expectedCreative = new CreativeResponse("1", "CreativeName1", "Description1", "ImageURL1", "campaignId1");
        when(manageCreativeUseCase.getCreativeById("1")).thenReturn(Optional.of(expectedCreative));

        mockMvc.perform(get("/creatives/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creativeId").value(expectedCreative.creativeId()))
                .andExpect(jsonPath("$.name").value(expectedCreative.name()))
                .andExpect(jsonPath("$.description").value(expectedCreative.description()))
                .andExpect(jsonPath("$.creativeUrl").value(expectedCreative.creativeUrl()))
                .andExpect(jsonPath("$.campaignId").value(expectedCreative.campaignId()));
    }

    @Test
    void updateCreative_Success() throws Exception {
        CreativeUpdateDTO creativeUpdateDTO = new CreativeUpdateDTO("UpdatedCreativeName", "UpdatedDescription", "UpdatedImageURL", "UpdatedCampaignId");
        CreativeResponse updatedCreative = new CreativeResponse("1", "UpdatedCreativeName", "UpdatedDescription", "UpdatedImageURL", "UpdatedCampaignId");
        when(manageCreativeUseCase.updateCreative("1", creativeUpdateDTO)).thenReturn(Optional.of(updatedCreative));

        mockMvc.perform(patch("/creatives/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(creativeUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.creativeId").value(updatedCreative.creativeId()))
                .andExpect(jsonPath("$.name").value(updatedCreative.name()))
                .andExpect(jsonPath("$.description").value(updatedCreative.description()))
                .andExpect(jsonPath("$.creativeUrl").value(updatedCreative.creativeUrl()))
                .andExpect(jsonPath("$.campaignId").value(updatedCreative.campaignId()));
    }

    @Test
    void deleteCreative_Success() throws Exception {
        when(manageCreativeUseCase.deleteCreative("1")).thenReturn(true);

        mockMvc.perform(delete("/creatives/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCreative_NotFound() throws Exception {
        when(manageCreativeUseCase.deleteCreative("nonExistingCreativeId")).thenReturn(false);

        mockMvc.perform(delete("/creatives/nonExistingCreativeId"))
                .andExpect(status().isNotFound());
    }
}