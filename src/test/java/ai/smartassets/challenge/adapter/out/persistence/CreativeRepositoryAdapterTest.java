package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.domain.Creative;
import ai.smartassets.challenge.aplication.port.out.CreativeRepository;
import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        CreativeEntity creative = mock(CreativeEntity.class);
        Page<CreativeEntity> expectedPage = new PageImpl<>(List.of(creative));
        when(creativeRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Creative> result = creativeRepositoryAdapter.findAll(pageable);

        assertThat(result.getContent().get(0).getCreativeId()).isEqualTo(creative.getId());
        assertThat(result.getContent().get(0).getName()).isEqualTo(creative.getName());
        assertThat(result.getContent().get(0).getDescription()).isEqualTo(creative.getDescription());
        verify(creativeRepository).findAll(pageable);
    }

    @Test
    void save() {
        CreativeEntity creativeEntity = new CreativeEntity("1", "Test Creative", "Description", "http://test.com/doc.pdf");
        when(creativeRepository.save(creativeEntity)).thenReturn(creativeEntity);

        Creative creative = new Creative(creativeEntity.getId(), creativeEntity.getName(), creativeEntity.getDescription(), creativeEntity.getCreativeUrl());
        Creative result = creativeRepositoryAdapter.save(creative);

        assertThat(result.getCreativeId()).isEqualTo(creativeEntity.getId());
        assertThat(result.getName()).isEqualTo(creativeEntity.getName());
        assertThat(result.getDescription()).isEqualTo(creativeEntity.getDescription());
        assertThat(result.getCreativeUrl()).isEqualTo(creativeEntity.getCreativeUrl());

        verify(creativeRepository).save(creativeEntity);
    }

    @Test
    void findById() {
        String id = "testId";
        Optional<CreativeEntity> expectedCreative = Optional.of(mock(CreativeEntity.class));
        when(creativeRepository.findById(id)).thenReturn(expectedCreative);

        Optional<Creative> result = creativeRepositoryAdapter.findById(id);

        assertThat(result.get().getCreativeId()).isEqualTo(expectedCreative.get().getId());
        assertThat(result.get().getName()).isEqualTo(expectedCreative.get().getName());
        assertThat(result.get().getDescription()).isEqualTo(expectedCreative.get().getDescription());
        assertThat(result.get().getCreativeUrl()).isEqualTo(expectedCreative.get().getCreativeUrl());

        verify(creativeRepository).findById(id);
    }

    @Test
    void delete() {
        CreativeEntity creativeEntity = new CreativeEntity("1", "Test Creative", "Description", "http://test.com/doc.pdf");
        when(creativeRepository.save(any(CreativeEntity.class))).thenReturn(creativeEntity);

        Creative creative = new Creative(creativeEntity.getId(), creativeEntity.getName(), creativeEntity.getDescription(), creativeEntity.getCreativeUrl());
        creativeRepositoryAdapter.delete(creative);

        verify(creativeRepository).delete(creativeEntity);
    }

    @Test
    void deleteById() {
        String id = "testId";

        creativeRepositoryAdapter.deleteById(id);

        verify(creativeRepository).deleteById(id);
    }
}