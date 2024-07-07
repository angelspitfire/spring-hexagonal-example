package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.domain.Campaign;
import ai.smartassets.challenge.aplication.port.out.CampaignRepository;
import ai.smartassets.challenge.infraestructure.persistence.model.CampaignEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
        CampaignEntity campaignEntity = new CampaignEntity("1", "Test Campaign", "Description");
        when(campaignRepository.save(any(CampaignEntity.class))).thenReturn(campaignEntity);

        Campaign campaign = new Campaign(campaignEntity.getId(), campaignEntity.getName(), campaignEntity.getDescription());
        Campaign result = manageCampaignUseCase.createCampaign(campaign);

        verify(campaignRepository).save(campaignEntity);
        assertThat(result.getCampaignId()).isEqualTo(campaignEntity.getId());
        assertThat(result.getName()).isEqualTo(campaignEntity.getName());
        assertThat(result.getDescription()).isEqualTo(campaignEntity.getDescription());
    }

    @Test
    void listCampaigns() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        CampaignEntity campaign = mock(CampaignEntity.class);
        List<CampaignEntity> campaignList = List.of(campaign);
        Page<CampaignEntity> page = new PageImpl<>(campaignList, pageRequest, campaignList.size());
        when(campaignRepository.findAll(pageRequest)).thenReturn(page);

        List<Campaign> result = manageCampaignUseCase.listCampaigns(pageRequest);

        verify(campaignRepository).findAll(pageRequest);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertThat(result.get(0).getCampaignId()).isEqualTo(campaign.getId());
        assertThat(result.get(0).getName()).isEqualTo(campaign.getName());
        assertThat(result.get(0).getDescription()).isEqualTo(campaign.getDescription());
    }

    @Test
    void getCampaignById() {
        String id = "testId";
        Optional<CampaignEntity> campaign = Optional.of(mock(CampaignEntity.class));
        when(campaignRepository.findById(id)).thenReturn(campaign);

        Optional<Campaign> result = manageCampaignUseCase.getCampaignById(id);

        verify(campaignRepository).findById(id);
        assertThat(result.get().getCampaignId()).isEqualTo(campaign.get().getId());
        assertThat(result.get().getName()).isEqualTo(campaign.get().getName());
        assertThat(result.get().getDescription()).isEqualTo(campaign.get().getDescription());
    }

    @Test
    void updateCampaign() {
        String id = "testId";
        CampaignEntity existingCampaignEntity = new CampaignEntity();
        CampaignEntity updatedCampaignEntity = new CampaignEntity();
        updatedCampaignEntity.setName("Updated Name");
        when(campaignRepository.findById(id)).thenReturn(Optional.of(existingCampaignEntity));
        when(campaignRepository.save(any(CampaignEntity.class))).thenReturn(updatedCampaignEntity);

        Campaign updatedCampaign = new Campaign(updatedCampaignEntity.getId(), updatedCampaignEntity.getName(), updatedCampaignEntity.getDescription());
        Optional<Campaign> result = manageCampaignUseCase.updateCampaign(id, updatedCampaign);

        verify(campaignRepository).findById(id);
        verify(campaignRepository).save(existingCampaignEntity);
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void deleteCampaign() {
        String id = "testId";
        CampaignEntity campaign = new CampaignEntity();
        when(campaignRepository.findById(id)).thenReturn(Optional.of(campaign));

        boolean result = manageCampaignUseCase.deleteCampaign(id);

        verify(campaignRepository).delete(campaign);
        assertTrue(result);
    }
}