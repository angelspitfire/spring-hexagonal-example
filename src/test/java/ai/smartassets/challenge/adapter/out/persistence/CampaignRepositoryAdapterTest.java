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

class CampaignRepositoryAdapterTest {

    private CampaignRepositoryAdapter campaignRepositoryAdapter;

    @Mock
    private CampaignRepository campaignRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        campaignRepositoryAdapter = new CampaignRepositoryAdapter(campaignRepository);
    }

    @Test
    void testSaveCampaign() {
        CampaignEntity campaign = new CampaignEntity("1", "Campaign Name", "Campaign Description", "brandId");
        when(campaignRepository.save(any(CampaignEntity.class))).thenReturn(campaign);

        CampaignEntity savedCampaign = campaignRepositoryAdapter.save(campaign);

        assertNotNull(savedCampaign);
        assertEquals("Campaign Name", savedCampaign.getName());
    }

    @Test
    void testFindById() {
        Optional<CampaignEntity> campaign = Optional.of(new CampaignEntity("1", "Campaign Name", "Campaign Description", "brandId"));
        when(campaignRepository.findById("1")).thenReturn(campaign);

        Optional<CampaignEntity> foundCampaign = campaignRepositoryAdapter.findById("1");

        assertTrue(foundCampaign.isPresent());
        assertEquals("Campaign Name", foundCampaign.get().getName());
    }

    @Test
    void testFindByIdNotFound() {
        when(campaignRepository.findById("2")).thenReturn(Optional.empty());

        Optional<CampaignEntity> foundCampaign = campaignRepositoryAdapter.findById("2");

        assertFalse(foundCampaign.isPresent());
    }

    @Test
    void testDeleteCampaign() {
        doNothing().when(campaignRepository).deleteById("1");

        assertDoesNotThrow(() -> campaignRepositoryAdapter.deleteById("1"));
    }

    @Test
    void testFindAll() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        CampaignEntity campaign = new CampaignEntity("1", "Campaign Name", "Campaign Description", "brandId");
        when(campaignRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(Collections.singletonList(campaign)));

        var campaigns = campaignRepositoryAdapter.findAll(pageRequest);

        assertFalse(campaigns.isEmpty());
        assertEquals(1, campaigns.getTotalElements());
        assertEquals("Campaign Name", campaigns.getContent().get(0).getName());
    }
}