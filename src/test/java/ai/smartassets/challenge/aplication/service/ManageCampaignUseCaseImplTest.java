package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.domain.Campaign;
import ai.smartassets.challenge.aplication.port.out.CampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManageCampaignUseCaseImplTest {

    @Mock
    private CampaignRepository campaignRepository;

    private ManageCampaignUseCaseImpl manageCampaignUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        manageCampaignUseCase = new ManageCampaignUseCaseImpl(campaignRepository);
    }

    @Test
    void createCampaign() {
        Campaign campaign = mock(Campaign.class);
        when(campaignRepository.save(any(Campaign.class))).thenReturn(campaign);

        Campaign result = manageCampaignUseCase.createCampaign(campaign);

        verify(campaignRepository).save(campaign);
        assertEquals(campaign, result);
    }

    @Test
    void listCampaigns() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Campaign campaign = mock(Campaign.class);
        List<Campaign> campaignList = List.of(campaign);
        Page<Campaign> page = new PageImpl<>(campaignList, pageRequest, campaignList.size());
        when(campaignRepository.findAll(pageRequest)).thenReturn(page);

        List<Campaign> result = manageCampaignUseCase.listCampaigns(pageRequest);

        verify(campaignRepository).findAll(pageRequest);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(campaign, result.get(0));
    }

    @Test
    void getCampaignById() {
        String id = "testId";
        Optional<Campaign> campaign = Optional.of(mock(Campaign.class));
        when(campaignRepository.findById(id)).thenReturn(campaign);

        Optional<Campaign> result = manageCampaignUseCase.getCampaignById(id);

        verify(campaignRepository).findById(id);
        assertEquals(campaign, result);
    }

    @Test
    void updateCampaign() {
        String id = "testId";
        Campaign existingCampaign = new Campaign();
        Campaign updatedCampaign = new Campaign();
        updatedCampaign.setName("Updated Name");
        when(campaignRepository.findById(id)).thenReturn(Optional.of(existingCampaign));
        when(campaignRepository.save(any(Campaign.class))).thenReturn(updatedCampaign);

        Optional<Campaign> result = manageCampaignUseCase.updateCampaign(id, updatedCampaign);

        verify(campaignRepository).findById(id);
        verify(campaignRepository).save(existingCampaign);
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void deleteCampaign() {
        String id = "testId";
        Campaign campaign = new Campaign();
        when(campaignRepository.findById(id)).thenReturn(Optional.of(campaign));

        boolean result = manageCampaignUseCase.deleteCampaign(id);

        verify(campaignRepository).delete(campaign);
        assertTrue(result);
    }
}