package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.domain.Creative;
import ai.smartassets.challenge.aplication.port.out.CreativeRepository;
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

class CreativeRepositoryAdapterTest {

    @Mock
    private CreativeRepository creativeRepository;

    private CreativeRepositoryAdapter creativeRepositoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        creativeRepositoryAdapter = new CreativeRepositoryAdapter(creativeRepository);
    }

    @Test
    void findAll() {
        Pageable pageable = mock(Pageable.class);
        Creative creative = mock(Creative.class);
        Page<Creative> expectedPage = new PageImpl<>(Collections.singletonList(creative));
        when(creativeRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Creative> result = creativeRepositoryAdapter.findAll(pageable);

        assertEquals(expectedPage, result);
        verify(creativeRepository).findAll(pageable);
    }

    @Test
    void save() {
        Creative creative = mock(Creative.class);
        when(creativeRepository.save(creative)).thenReturn(creative);

        Creative result = creativeRepositoryAdapter.save(creative);

        assertEquals(creative, result);
        verify(creativeRepository).save(creative);
    }

    @Test
    void findById() {
        String id = "testId";
        Optional<Creative> expectedCreative = Optional.of(mock(Creative.class));
        when(creativeRepository.findById(id)).thenReturn(expectedCreative);

        Optional<Creative> result = creativeRepositoryAdapter.findById(id);

        assertEquals(expectedCreative, result);
        verify(creativeRepository).findById(id);
    }

    @Test
    void delete() {
        Creative creative = mock(Creative.class);

        creativeRepositoryAdapter.delete(creative);

        verify(creativeRepository).delete(creative);
    }

    @Test
    void deleteById() {
        String id = "testId";

        creativeRepositoryAdapter.deleteById(id);

        verify(creativeRepository).deleteById(id);
    }
}