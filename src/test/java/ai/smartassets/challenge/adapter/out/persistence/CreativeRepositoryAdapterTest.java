package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.aplication.port.out.CreativeRepository;
import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreativeRepositoryAdapterTest {

    private CreativeRepositoryAdapter creativeRepositoryAdapter;

    @Mock
    private CreativeRepository creativeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        creativeRepositoryAdapter = new CreativeRepositoryAdapter(creativeRepository);
    }

    @Test
    void testSaveCreative() {
        CreativeEntity creative = new CreativeEntity("1", "Creative Name", "Creative Description", "url", "campaignId");
        when(creativeRepository.save(any(CreativeEntity.class))).thenReturn(creative);

        CreativeEntity savedCreative = creativeRepositoryAdapter.save(creative);

        assertNotNull(savedCreative);
        assertEquals("Creative Name", savedCreative.getName());
    }

    @Test
    void testFindById() {
        Optional<CreativeEntity> creative = Optional.of(new CreativeEntity("1", "Creative Name", "Creative Description", "url", "campaignId"));
        when(creativeRepository.findById("1")).thenReturn(creative);

        Optional<CreativeEntity> foundCreative = creativeRepositoryAdapter.findById("1");

        assertTrue(foundCreative.isPresent());
        assertEquals("Creative Name", foundCreative.get().getName());
    }

    @Test
    void testFindByIdNotFound() {
        when(creativeRepository.findById("2")).thenReturn(Optional.empty());

        Optional<CreativeEntity> foundCreative = creativeRepositoryAdapter.findById("2");

        assertFalse(foundCreative.isPresent());
    }

    @Test
    void testDeleteCreative() {
        doNothing().when(creativeRepository).delete(any(CreativeEntity.class));

        assertDoesNotThrow(() -> creativeRepositoryAdapter.delete(new CreativeEntity("1", "Creative Name", "Creative Description", "url", "campaignId")));
    }

    @Test
    void testFindAll() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        CreativeEntity creative = new CreativeEntity("1", "Creative Name", "Creative Description", "url", "campaignId");
        when(creativeRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(Collections.singletonList(creative)));

        var creatives = creativeRepositoryAdapter.findAll(pageRequest);

        assertFalse(creatives.isEmpty());
        assertEquals(1, creatives.getTotalElements());
        assertEquals("Creative Name", creatives.getContent().get(0).getName());
    }
}