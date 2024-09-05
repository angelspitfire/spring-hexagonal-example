package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.dto.CampaignCreationDTO;
import ai.smartassets.challenge.aplication.dto.CampaignResponse;
import ai.smartassets.challenge.aplication.dto.CampaignUpdateDto;
import ai.smartassets.challenge.aplication.dto.CreativeUploadDTO;
import ai.smartassets.challenge.aplication.exception.BrandNotFoundException;
import ai.smartassets.challenge.aplication.port.out.*;
import ai.smartassets.challenge.domain.Creative;
import ai.smartassets.challenge.infraestructure.persistence.model.BrandEntity;
import ai.smartassets.challenge.infraestructure.persistence.model.CampaignEntity;
import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
import ai.smartassets.challenge.infraestructure.service.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ManageCampaignUseCaseImplTest {

    @Mock
    private CampaignRepositoryPort campaignRepository;

    @Mock
    private BrandRepositoryPort brandRepository;

    @Mock
    private CreativeRepositoryPort creativeRepository;

    @Mock
    private FileStorageService fileStorageService;

    private ManageCampaignUseCaseImpl manageCampaignUseCase;

    @BeforeEach
    void setUp() {
        openMocks(this);
        manageCampaignUseCase = new ManageCampaignUseCaseImpl(campaignRepository, brandRepository, creativeRepository, fileStorageService);
    }

    @Test
    void givenPageableRequest_whenListCampaigns_thenCampaignsAreReturnedSuccessfully() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        CampaignEntity campaign = mock(CampaignEntity.class);
        List<CampaignEntity> campaignList = List.of(campaign);
        Page<CampaignEntity> page = new PageImpl<>(campaignList, pageRequest, campaignList.size());
        when(campaignRepository.findAll(pageRequest)).thenReturn(page);

        List<CampaignResponse> result = manageCampaignUseCase.listCampaigns(pageRequest);

        verify(campaignRepository).findAll(pageRequest);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertThat(result.get(0).campaignId()).isEqualTo(campaign.getId());
        assertThat(result.get(0).name()).isEqualTo(campaign.getName());
        assertThat(result.get(0).description()).isEqualTo(campaign.getDescription());
    }

    @Test
    void givenCampaignId_whenGetCampaignById_thenCampaignIsFoundSuccessfully() {
        String id = "testId";
        Optional<CampaignEntity> campaign = Optional.of(mock(CampaignEntity.class));
        when(campaignRepository.findById(id)).thenReturn(campaign);

        Optional<CampaignResponse> result = manageCampaignUseCase.getCampaignById(id);

        verify(campaignRepository).findById(id);
        assertThat(result.get().campaignId()).isEqualTo(campaign.get().getId());
        assertThat(result.get().name()).isEqualTo(campaign.get().getName());
        assertThat(result.get().description()).isEqualTo(campaign.get().getDescription());
    }

    @Test
    void givenCampaignIdAndUpdatedInfo_whenUpdateCampaign_thenCampaignIsUpdatedSuccessfully() {
        String id = "testId";
        CampaignEntity existingCampaignEntity = new CampaignEntity();
        existingCampaignEntity.setId(id);
        existingCampaignEntity.setName("Original Name");
        existingCampaignEntity.setDescription("Original Description");
        CampaignEntity updatedCampaignEntity = new CampaignEntity();
        updatedCampaignEntity.setId(id);
        updatedCampaignEntity.setName("Updated Name");
        updatedCampaignEntity.setDescription("Updated Description");
        when(campaignRepository.existsById(id)).thenReturn(true);
        when(campaignRepository.findById(id)).thenReturn(Optional.of(existingCampaignEntity));
        when(campaignRepository.save(any(CampaignEntity.class))).thenReturn(updatedCampaignEntity);

        CampaignUpdateDto updatedCampaign = new CampaignUpdateDto("Updated Name", "Updated Description");
        Optional<CampaignResponse> result = manageCampaignUseCase.updateCampaign(id, updatedCampaign);

        verify(campaignRepository).findById(id);
        verify(campaignRepository).save(existingCampaignEntity);
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().name());
        assertEquals("Updated Description", result.get().description());
    }

    @Test
    void givenCampaignId_whenDeleteCampaign_thenCampaignIsDeletedSuccessfully() {
        String id = "testId";
        CampaignEntity campaign = new CampaignEntity();
        when(campaignRepository.findById(id)).thenReturn(Optional.of(campaign));

        boolean result = manageCampaignUseCase.deleteCampaign(id);

        verify(campaignRepository).delete(campaign);
        assertTrue(result);
    }

    @Test
    void givenExistingBrandIdAndCampaignDetails_whenCreateCampaignForBrand_thenCampaignIsCreatedSuccessfully() {
        String brandId = "brand123";
        CampaignCreationDTO campaignDTO = new CampaignCreationDTO("Campaign Name", "Campaign Description");
        CampaignEntity campaignEntity = new CampaignEntity("camp123", "Campaign Name", "Campaign Description", brandId);

        when(brandRepository.existsById(brandId)).thenReturn(true);
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(new BrandEntity(brandId, "Brand name", "Description")));
        when(campaignRepository.save(any(CampaignEntity.class))).thenReturn(campaignEntity);

        CampaignResponse result = manageCampaignUseCase.createCampaignForBrand(brandId, campaignDTO);

        verify(brandRepository).existsById(brandId);
        verify(brandRepository).findById(brandId);
        verify(campaignRepository).save(any(CampaignEntity.class));
        assertNotNull(result.campaignId());
        assertEquals(brandId, campaignEntity.getBrandId());
        assertEquals(campaignDTO.getName(), result.name());
        assertEquals(campaignDTO.getDescription(), result.description());
    }

    @Test
    void givenNonExistentBrandId_whenCreateCampaignForBrand_thenBrandNotFoundExceptionIsThrown() {
        String brandId = "nonExistentBrand";
        CampaignCreationDTO campaign = new CampaignCreationDTO("Campaign Name", "Campaign Description");
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        assertThrows(BrandNotFoundException.class, () -> manageCampaignUseCase.createCampaignForBrand(brandId, campaign));

        verify(campaignRepository, never()).save(any(CampaignEntity.class));
    }

    @Test
    void givenBrandId_whenFindCampaignsByBrandId_thenCorrectCampaignsAreReturned() {
        String brandId = "brand123";
        List<CampaignEntity> campaignEntities = List.of(
                new CampaignEntity("camp1", "Campaign 1", "Description 1", brandId),
                new CampaignEntity("camp2", "Campaign 2", "Description 2", brandId)
        );

        when(brandRepository.existsById("brand123")).thenReturn(true);

        when(campaignRepository.findByBrandId(anyString(), any(Pageable.class))).thenReturn(campaignEntities);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<CampaignResponse> campaigns = manageCampaignUseCase.findCampaignsByBrandId(brandId, pageRequest);

        assertEquals(2, campaigns.size());
        assertEquals("Campaign 1", campaigns.get(0).name());
        assertEquals("Campaign 2", campaigns.get(1).name());
    }


    @Test
    void givenBrandIdAndCampaignId_whenFindCreativesByBrandIdAndCampaignId_thenCorrectCreativesAreReturned() {
        String brandId = "brand123";
        String campaignId = "camp123";
        PageRequest pageRequest = PageRequest.of(0, 10);

        BrandEntity brandEntity = new BrandEntity(brandId, "Brand Name", "Brand Description");

        CampaignEntity campaignEntity = new CampaignEntity(campaignId, "Campaign Name", "Campaign Description", brandId);
        CreativeEntity creativeEntity1 = new CreativeEntity("1", "Creative 1", "Description 1", "http://test.com/creative1.pdf", campaignId);
        CreativeEntity creativeEntity2 = new CreativeEntity("2", "Creative 2", "Description 2", "http://test.com/creative2.pdf", campaignId);
        List<CreativeEntity> creativeEntities = List.of(creativeEntity1, creativeEntity2);

        when(brandRepository.existsById(brandId)).thenReturn(true);

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brandEntity));
        when(campaignRepository.existsById(campaignId)).thenReturn(true);
        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaignEntity));
        when(creativeRepository.findByCampaignId(campaignId, pageRequest)).thenReturn(creativeEntities);

        List<Creative> creatives = manageCampaignUseCase.findCreativesByBrandIdAndCampaignId(brandId, campaignId, pageRequest);

        assertEquals(2, creatives.size());
        assertEquals("Creative 1", creatives.get(0).getName());
        assertEquals("Creative 2", creatives.get(1).getName());

        verify(brandRepository).existsById(brandId);
        verify(campaignRepository).existsById(campaignId);
        verify(creativeRepository).findByCampaignId(campaignId, pageRequest);
    }

    @Test
    public void givenBrandIdAndCampaignIdAndCreativeDetails_whenUploadCreativeForCampaign_thenCreativeIsUploadedSuccessfully() {
        String brandId = "brand123";
        String campaignId = "camp123";
        CreativeUploadDTO creativeUploadDTO = new CreativeUploadDTO("Creative Name", "Creative Description",  new MockMultipartFile("creative.pdf", "creative.pdf", "application/pdf", new byte[0]));
        CreativeEntity creativeEntity = new CreativeEntity("1", "Creative Name", "Creative Description", "http://test.com/creative.pdf", campaignId);
        Creative creative = new Creative("1", "Creative Name", "Creative Description", "http://test.com/creative.pdf",  campaignId);

        when(brandRepository.existsById(brandId)).thenReturn(true);
        when(campaignRepository.existsById(campaignId)).thenReturn(true);
        when(creativeRepository.save(any(CreativeEntity.class))).thenReturn(creativeEntity);

        Creative result = manageCampaignUseCase.uploadCreativeForCampaign(brandId, campaignId, creativeUploadDTO).get();

        verify(brandRepository).existsById(brandId);
        verify(campaignRepository).existsById(campaignId);
        verify(creativeRepository).save(any(CreativeEntity.class));
        assertEquals(creative.getCreativeId(), result.getCreativeId());
        assertEquals(creative.getName(), result.getName());
        assertEquals(creative.getDescription(), result.getDescription());
        assertEquals(creative.getCreativeUrl(), result.getCreativeUrl());
    }
}