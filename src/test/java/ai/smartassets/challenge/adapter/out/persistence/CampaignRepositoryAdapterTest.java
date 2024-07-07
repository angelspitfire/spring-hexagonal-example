package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.domain.Campaign;
import ai.smartassets.challenge.aplication.port.out.CampaignRepository;
import ai.smartassets.challenge.infraestructure.persistence.model.CampaignEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

class CampaignRepositoryAdapterTest {

    @Mock
    private CampaignRepository campaignRepository;

    private CampaignRepositoryAdapter campaignRepositoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        campaignRepositoryAdapter = new CampaignRepositoryAdapter(campaignRepository);
    }

    @Test
    void findAll() {
        Pageable pageable = mock(Pageable.class);
        CampaignEntity campaign = mock(CampaignEntity.class);
        Page<CampaignEntity> expectedPage = new PageImpl<>(Collections.singletonList(campaign));
        when(campaignRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Campaign> result = campaignRepositoryAdapter.findAll(pageable);

        assertThat(result.getContent().get(0).getCampaignId()).isEqualTo(campaign.getId());
        assertThat(result.getContent().get(0).getName()).isEqualTo(campaign.getName());
        assertThat(result.getContent().get(0).getDescription()).isEqualTo(campaign.getDescription());
        verify(campaignRepository).findAll(pageable);
    }

    @Test
    void save() {
        CampaignEntity campaignEntity = new CampaignEntity("1", "Test Campaign", "Description");
        Campaign campaign = new Campaign(campaignEntity.getId(), campaignEntity.getName(), campaignEntity.getDescription());
        when(campaignRepository.save(campaignEntity)).thenReturn(campaignEntity);

        Campaign result = campaignRepositoryAdapter.save(campaign);

        assertThat(result.getCampaignId()).isEqualTo(campaignEntity.getId());
        assertThat(result.getName()).isEqualTo(campaignEntity.getName());
        assertThat(result.getDescription()).isEqualTo(campaignEntity.getDescription());

        verify(campaignRepository).save(campaignEntity);
    }

    @Test
    void findById() {
        String id = "testId";
        Optional<CampaignEntity> expectedCampaign = Optional.of(mock(CampaignEntity.class));

        when(campaignRepository.findById(id)).thenReturn(expectedCampaign);

        Optional<Campaign> result = campaignRepositoryAdapter.findById(id);

        assertThat(result.get().getCampaignId()).isEqualTo(expectedCampaign.get().getId());
        assertThat(result.get().getName()).isEqualTo(expectedCampaign.get().getName());
        assertThat(result.get().getDescription()).isEqualTo(expectedCampaign.get().getDescription());
        verify(campaignRepository).findById(id);
    }

    @Test
    void delete() {
        Campaign campaign = mock(Campaign.class);

        campaignRepositoryAdapter.delete(campaign);

        CampaignEntity entity = new CampaignEntity(campaign.getCampaignId(), campaign.getName(), campaign.getDescription());

        verify(campaignRepository).delete(entity);
    }

    @Test
    void deleteById() {
        String id = "testId";

        campaignRepositoryAdapter.deleteById(id);

        verify(campaignRepository).deleteById(id);
    }
}