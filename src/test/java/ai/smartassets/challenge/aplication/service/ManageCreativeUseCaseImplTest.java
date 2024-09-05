package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.dto.CreativeResponse;
import ai.smartassets.challenge.aplication.dto.CreativeUpdateRequest;
import ai.smartassets.challenge.aplication.dto.CreativeUploadRequest;
import ai.smartassets.challenge.aplication.port.out.CreativeRepositoryPort;
import ai.smartassets.challenge.domain.Creative;
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
import static org.mockito.MockitoAnnotations.openMocks;

class ManageCreativeUseCaseImplTest {

    @Mock
    private CreativeRepositoryPort creativeRepository;

    private ManageCreativeUseCaseImpl manageCreativeUseCase;

    @BeforeEach
    void setUp() {
        openMocks(this);
        manageCreativeUseCase = new ManageCreativeUseCaseImpl(creativeRepository);
    }

    @Test
    void createCreative() {
        CreativeEntity creativeEntity = new CreativeEntity("1", "Test Creative", "Description", "http://test.com/doc.pdf", "1");
        Creative creative = new Creative(creativeEntity.getId(), creativeEntity.getName(), creativeEntity.getDescription(), creativeEntity.getCreativeUrl(), creativeEntity.getCampaignId());

        when(creativeRepository.save(any(CreativeEntity.class))).thenReturn(creativeEntity);

        CreativeResponse result = manageCreativeUseCase.createCreative(creative);

        verify(creativeRepository).save(creativeEntity);
        assertThat(result.creativeId()).isEqualTo(creativeEntity.getId());
        assertThat(result.name()).isEqualTo(creativeEntity.getName());
        assertThat(result.description()).isEqualTo(creativeEntity.getDescription());
        assertThat(result.creativeUrl()).isEqualTo(creativeEntity.getCreativeUrl());
    }

    @Test
    void listCreatives() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        CreativeEntity creative = new CreativeEntity("1", "Test Creative", "Description", "http://test.com/doc.pdf", "1");
        List<CreativeEntity> creativeList = List.of(creative);
        Page<CreativeEntity> page = new PageImpl<>(creativeList);
        when(creativeRepository.findAll(pageRequest)).thenReturn(page);

        List<CreativeResponse> result = manageCreativeUseCase.listCreatives(pageRequest);

        verify(creativeRepository).findAll(pageRequest);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        assertThat(creative.getId()).isEqualTo(result.get(0).creativeId());
        assertThat(creative.getName()).isEqualTo(result.get(0).name());
        assertThat(creative.getDescription()).isEqualTo(result.get(0).description());
        assertThat(creative.getCreativeUrl()).isEqualTo(result.get(0).creativeUrl());
    }

    @Test
    void getCreativeById() {
        String id = "testId";
        Optional<CreativeEntity> creativeEntity = Optional.of(new CreativeEntity("1", "Test Creative", "Description", "http://test.com/doc.pdf", "1"));
        when(creativeRepository.findById(id)).thenReturn(creativeEntity);

        Optional<CreativeResponse> result = manageCreativeUseCase.getCreativeById(id);

        verify(creativeRepository).findById(id);

        assertThat(creativeEntity.get().getId()).isEqualTo(result.get().creativeId());
        assertThat(creativeEntity.get().getDescription()).isEqualTo(result.get().description());
        assertThat(creativeEntity.get().getName()).isEqualTo(result.get().name());
        assertThat(creativeEntity.get().getId()).isEqualTo(result.get().creativeId());
    }

    @Test
    void updateCreative() {
        String id = "testId";
        CreativeEntity existingCreativeEntity = new CreativeEntity();
        CreativeEntity updatedCreativeEntity = new CreativeEntity();
        updatedCreativeEntity.setName("Updated Name");
        when(creativeRepository.findById(id)).thenReturn(Optional.of(existingCreativeEntity));
        when(creativeRepository.save(any(CreativeEntity.class))).thenReturn(updatedCreativeEntity);

        CreativeUpdateRequest updatedCreative = new CreativeUpdateRequest(updatedCreativeEntity.getName(), updatedCreativeEntity.getDescription());
        Optional<CreativeResponse> result = manageCreativeUseCase.updateCreative(id, updatedCreative);

        verify(creativeRepository).findById(id);
        verify(creativeRepository).save(existingCreativeEntity);
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().name());
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