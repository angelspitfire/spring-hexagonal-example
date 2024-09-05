package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.infraestructure.persistence.model.CampaignEntity;
import ai.smartassets.challenge.aplication.port.out.CampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class CampaignRepositoryAdapterTest {

    private CampaignRepositoryAdapter campaignRepositoryAdapter;

    @Mock
    private CampaignRepository campaignRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
        campaignRepositoryAdapter = new CampaignRepositoryAdapter(campaignRepository);
    }

    @Test
    void givenCampaignEntity_whenSaveCampaign_thenCampaignIsSavedSuccessfully() {
        CampaignEntity campaign = new CampaignEntity("1", "Campaign Name", "Campaign Description", "brandId");
        when(campaignRepository.save(any(CampaignEntity.class))).thenReturn(campaign);

        CampaignEntity savedCampaign = campaignRepositoryAdapter.save(campaign);

        assertNotNull(savedCampaign);
        assertEquals("Campaign Name", savedCampaign.getName());
    }

    @Test
    void givenCampaignId_whenFindById_thenCampaignIsFoundSuccessfully() {
        Optional<CampaignEntity> campaign = Optional.of(new CampaignEntity("1", "Campaign Name", "Campaign Description", "brandId"));
        when(campaignRepository.findById("1")).thenReturn(campaign);

        Optional<CampaignEntity> foundCampaign = campaignRepositoryAdapter.findById("1");

        assertTrue(foundCampaign.isPresent());
        assertEquals("Campaign Name", foundCampaign.get().getName());
    }

    @Test
    void givenNonExistingCampaignId_whenFindById_thenNoCampaignIsFound() {
        when(campaignRepository.findById("2")).thenReturn(Optional.empty());

        Optional<CampaignEntity> foundCampaign = campaignRepositoryAdapter.findById("2");

        assertFalse(foundCampaign.isPresent());
    }

    @Test
    void givenCampaignId_whenDeleteCampaign_thenCampaignIsDeletedSuccessfully() {
        doNothing().when(campaignRepository).deleteById("1");

        assertDoesNotThrow(() -> campaignRepositoryAdapter.deleteById("1"));
    }

    @Test
    void givenPageableRequest_whenFindAll_thenCampaignsAreReturnedSuccessfully() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        CampaignEntity campaign = new CampaignEntity("1", "Campaign Name", "Campaign Description", "brandId");
        when(campaignRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(Collections.singletonList(campaign)));

        var campaigns = campaignRepositoryAdapter.findAll(pageRequest);

        assertFalse(campaigns.isEmpty());
        assertEquals(1, campaigns.getTotalElements());
        assertEquals("Campaign Name", campaigns.getContent().get(0).getName());
    }

    @Test
    void givenBrandIdAndPageableRequest_whenFindByBrandId_thenCampaignsAreReturnedSuccessfully() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        CampaignEntity campaign = new CampaignEntity("1", "Campaign Name", "Campaign Description", "brandId");
        when(campaignRepository.findByBrandId("brandId", pageRequest)).thenReturn(Collections.singletonList(campaign));

        var campaigns = campaignRepositoryAdapter.findByBrandId("brandId", pageRequest);

        assertFalse(campaigns.isEmpty());
        assertEquals("Campaign Name", campaigns.get(0).getName());
    }

    @Test
    void givenBrandIdAndCampaignIdAndPageableRequest_whenFindByBrandIdAndId_thenCampaignsAreReturnedSuccessfully() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        CampaignEntity campaign = new CampaignEntity("1", "Campaign Name", "Campaign Description", "brandId");
        when(campaignRepository.findByBrandIdAndId("brandId", "1", pageRequest)).thenReturn(Collections.singletonList(campaign));

        var campaigns = campaignRepositoryAdapter.findByBrandIdAndId("brandId", "1", pageRequest);

        assertFalse(campaigns.isEmpty());
        assertEquals("Campaign Name", campaigns.get(0).getName());
    }

    @Test
    void givenCampaignId_whenExistsById_thenCampaignExists() {
        when(campaignRepository.existsById("1")).thenReturn(true);

        assertTrue(campaignRepositoryAdapter.existsById("1"));
    }

    @Test
    void givenNonExistingCampaignId_whenExistsById_thenCampaignDoesNotExist() {
        when(campaignRepository.existsById("2")).thenReturn(false);

        assertFalse(campaignRepositoryAdapter.existsById("2"));
    }

    @Test
    void givenBrandId_whenExistsByBrandId_thenCampaignExists() {
        when(campaignRepository.existsById("brandId")).thenReturn(true);

        assertTrue(campaignRepositoryAdapter.existsById("brandId"));
    }

    @Test
    void givenNonExistingBrandId_whenExistsByBrandId_thenCampaignDoesNotExist() {
        when(campaignRepository.existsById("brandId")).thenReturn(false);

        assertFalse(campaignRepositoryAdapter.existsById("brandId"));
    }
}