package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.domain.Creative;
import ai.smartassets.challenge.aplication.port.out.CreativeRepository;
import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
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

class ManageCreativeUseCaseImplTest {

    @Mock
    private CreativeRepository creativeRepository;

    private ManageCreativeUseCaseImpl manageCreativeUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        manageCreativeUseCase = new ManageCreativeUseCaseImpl(creativeRepository);
    }

    @Test
    void createCreative() {
        CreativeEntity creativeEntity = new CreativeEntity("1", "Test Creative", "Description", "http://test.com/doc.pdf");
        Creative creative = new Creative(creativeEntity.getId(), creativeEntity.getName(), creativeEntity.getDescription(), creativeEntity.getCreativeUrl());

        when(creativeRepository.save(any(CreativeEntity.class))).thenReturn(creativeEntity);

        Creative result = manageCreativeUseCase.createCreative(creative);

        verify(creativeRepository).save(creativeEntity);
        assertThat(result).isEqualTo(creative);
    }

    @Test
    void listCreatives() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        CreativeEntity creative = new CreativeEntity("1", "Test Creative", "Description", "http://test.com/doc.pdf");
        List<CreativeEntity> creativeList = List.of(creative);
        Page<CreativeEntity> page = new PageImpl<>(creativeList);
        when(creativeRepository.findAll(pageRequest)).thenReturn(page);

        List<Creative> result = manageCreativeUseCase.listCreatives(pageRequest);

        verify(creativeRepository).findAll(pageRequest);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        assertThat(creative.getId()).isEqualTo(result.get(0).getCreativeId());
        assertThat(creative.getName()).isEqualTo(result.get(0).getName());
        assertThat(creative.getDescription()).isEqualTo(result.get(0).getDescription());
        assertThat(creative.getCreativeUrl()).isEqualTo(result.get(0).getCreativeUrl());
    }

    @Test
    void getCreativeById() {
        String id = "testId";
        Optional<CreativeEntity> creativeEntity = Optional.of(new CreativeEntity("1", "Test Creative", "Description", "http://test.com/doc.pdf"));
        when(creativeRepository.findById(id)).thenReturn(creativeEntity);

        Optional<Creative> result = manageCreativeUseCase.getCreativeById(id);

        verify(creativeRepository).findById(id);

        assertThat(creativeEntity.get().getCreativeUrl()).isEqualTo(result.get().getCreativeUrl());
        assertThat(creativeEntity.get().getDescription()).isEqualTo(result.get().getDescription());
        assertThat(creativeEntity.get().getName()).isEqualTo(result.get().getName());
        assertThat(creativeEntity.get().getId()).isEqualTo(result.get().getCreativeId());
    }

    @Test
    void updateCreative() {
        String id = "testId";
        CreativeEntity existingCreativeEntity = new CreativeEntity();
        CreativeEntity updatedCreativeEntity = new CreativeEntity();
        updatedCreativeEntity.setName("Updated Name");
        when(creativeRepository.findById(id)).thenReturn(Optional.of(existingCreativeEntity));
        when(creativeRepository.save(any(CreativeEntity.class))).thenReturn(updatedCreativeEntity);

        Creative updatedCreative = new Creative(updatedCreativeEntity.getId(), updatedCreativeEntity.getName(), updatedCreativeEntity.getDescription(), updatedCreativeEntity.getCreativeUrl());
        Optional<Creative> result = manageCreativeUseCase.updateCreative(id, updatedCreative);

        verify(creativeRepository).findById(id);
        verify(creativeRepository).save(existingCreativeEntity);
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void deleteCreative() {
        String id = "testId";
        CreativeEntity creative = new CreativeEntity();
        when(creativeRepository.findById(id)).thenReturn(Optional.of(creative));

        boolean result = manageCreativeUseCase.deleteCreative(id);

        verify(creativeRepository).delete(creative);
        assertTrue(result);
    }
}