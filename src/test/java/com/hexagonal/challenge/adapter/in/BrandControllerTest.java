package com.hexagonal.challenge.adapter.in;

import com.hexagonal.challenge.aplication.dto.BrandCreationDto;
import com.hexagonal.challenge.aplication.dto.BrandUpdateDto;
import com.hexagonal.challenge.aplication.dto.CampaignCreationDTO;
import com.hexagonal.challenge.aplication.port.in.ManageBrandUseCase;
import com.hexagonal.challenge.aplication.port.in.ManageCampaignUseCase;
import com.hexagonal.challenge.domain.Brand;
import com.hexagonal.challenge.domain.Campaign;
import com.hexagonal.challenge.domain.Creative;
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

@WebMvcTest(BrandController.class)
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ManageBrandUseCase manageBrandUseCase;

    @MockBean
    private ManageCampaignUseCase manageCampaignUseCase;

    @Test
    void createBrand_Success() throws Exception {
        BrandCreationDto brandCreationDto = new BrandCreationDto("BrandName", "Description");
        Brand createdBrand = new Brand("1", "BrandName", "Description");

        when(manageBrandUseCase.createBrand(any(BrandCreationDto.class))).thenReturn(createdBrand);

        mockMvc.perform(post("/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(brandCreationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brandId").value(createdBrand.getBrandId()))
                .andExpect(jsonPath("$.name").value(createdBrand.getName()));
    }

    @Test
    void listBrands_Success() throws Exception {
        List<Brand> brands = List.of(new Brand("1", "BrandName", "Description"));
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(manageBrandUseCase.listBrands(pageRequest)).thenReturn(brands);

        mockMvc.perform(get("/brands?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].brandId").value(brands.get(0).getBrandId()));
    }

    @Test
    void getBrandById_Exists() throws Exception {
        Brand brand = new Brand("1", "BrandName", "Description");
        when(manageBrandUseCase.getBrandById("1")).thenReturn(Optional.of(brand));

        mockMvc.perform(get("/brands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brandId").value(brand.getBrandId()))
                .andExpect(jsonPath("$.name").value(brand.getName()));
    }

    @Test
    void getBrandById_NotFound() throws Exception {
        when(manageBrandUseCase.getBrandById("nonExistingId")).thenReturn(Optional.empty());

        mockMvc.perform(get("/brands/nonExistingId"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBrand_Success() throws Exception {
        BrandUpdateDto brandUpdateDto = new BrandUpdateDto("UpdatedName", "UpdatedDescription");
        Brand updatedBrand = new Brand("1", "UpdatedName", "UpdatedDescription");
        when(manageBrandUseCase.updateBrand(eq("1"), any(BrandUpdateDto.class))).thenReturn(Optional.of(updatedBrand));

        mockMvc.perform(patch("/brands/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(brandUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brandId").value(updatedBrand.getBrandId()))
                .andExpect(jsonPath("$.name").value(updatedBrand.getName()));
    }

    @Test
    void deleteBrand_Success() throws Exception {
        when(manageBrandUseCase.deleteBrand("1")).thenReturn(true);

        mockMvc.perform(delete("/brands/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBrand_NotFound() throws Exception {
        when(manageBrandUseCase.deleteBrand("nonExistingId")).thenReturn(false);

        mockMvc.perform(delete("/brands/nonExistingId"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCampaignForBrand_Success() throws Exception {
        CampaignCreationDTO campaignCreationDTO = new CampaignCreationDTO("CampaignName", "Description");
        Campaign createdCampaign = new Campaign("1", "CampaignName", "Description");
        when(manageCampaignUseCase.createCampaignForBrand(eq("1"), any(CampaignCreationDTO.class))).thenReturn(createdCampaign);

        mockMvc.perform(post("/brands/1/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(campaignCreationDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.campaignId").value(createdCampaign.getCampaignId()))
                .andExpect(jsonPath("$.name").value(createdCampaign.getName()));
    }

    @Test
    void listCampaignsForBrand_Success() throws Exception {
        List<Campaign> campaigns = List.of(new Campaign("1", "CampaignName", "Description"));
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(manageCampaignUseCase.findCampaignsByBrandId(eq("1"), any(PageRequest.class))).thenReturn(campaigns);

        mockMvc.perform(get("/brands/1/campaigns?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].campaignId").value(campaigns.get(0).getCampaignId()));
    }

@Test
void listCreativesForCampaign_Success() throws Exception {
    List<Creative> expectedCreatives = List.of(
        new Creative("1", "CreativeName1", "Description1", "ImageURL1", "campaignId1"),
        new Creative("2", "CreativeName2", "Description2", "ImageURL2", "campaignId2")
    );
    when(manageCampaignUseCase.findCreativesByBrandIdAndCampaignId("brandId1", "1", PageRequest.of(0, 10))).thenReturn(expectedCreatives);

    mockMvc.perform(get("/brands/brandId1/campaigns/1/creatives?page=0&size=10")
                    .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].creativeId").value(expectedCreatives.get(0).getCreativeId()))
        .andExpect(jsonPath("$[0].name").value(expectedCreatives.get(0).getName()))
        .andExpect(jsonPath("$[0].description").value(expectedCreatives.get(0).getDescription()))
        .andExpect(jsonPath("$[0].creativeUrl").value(expectedCreatives.get(0).getCreativeUrl()))
        .andExpect(jsonPath("$[1].creativeId").value(expectedCreatives.get(1).getCreativeId()))
        .andExpect(jsonPath("$[1].name").value(expectedCreatives.get(1).getName()))
        .andExpect(jsonPath("$[1].description").value(expectedCreatives.get(1).getDescription()))
        .andExpect(jsonPath("$[1].creativeUrl").value(expectedCreatives.get(1).getCreativeUrl()));
}
}