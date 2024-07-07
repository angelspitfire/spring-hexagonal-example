package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.domain.Campaign;
import ai.smartassets.challenge.aplication.port.out.CampaignRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Campaign campaign = mock(Campaign.class);
        Page<Campaign> expectedPage = new PageImpl<>(Collections.singletonList(campaign));
        when(campaignRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Campaign> result = campaignRepositoryAdapter.findAll(pageable);

        assertEquals(expectedPage, result);
        verify(campaignRepository).findAll(pageable);
    }

    @Test
    void save() {
        Campaign campaign = mock(Campaign.class);
        when(campaignRepository.save(campaign)).thenReturn(campaign);

        Campaign result = campaignRepositoryAdapter.save(campaign);

        assertEquals(campaign, result);
        verify(campaignRepository).save(campaign);
    }

    @Test
    void findById() {
        String id = "testId";
        Optional<Campaign> expectedCampaign = Optional.of(mock(Campaign.class));
        when(campaignRepository.findById(id)).thenReturn(expectedCampaign);

        Optional<Campaign> result = campaignRepositoryAdapter.findById(id);

        assertEquals(expectedCampaign, result);
        verify(campaignRepository).findById(id);
    }

    @Test
    void delete() {
        Campaign campaign = mock(Campaign.class);

        campaignRepositoryAdapter.delete(campaign);

        verify(campaignRepository).delete(campaign);
    }

    @Test
    void deleteById() {
        String id = "testId";

        campaignRepositoryAdapter.deleteById(id);

        verify(campaignRepository).deleteById(id);
    }
}