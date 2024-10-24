package com.hexagonal.challenge.aplication.service;

import com.hexagonal.challenge.aplication.dto.CreativeDTO;
import com.hexagonal.challenge.aplication.dto.CreativeUpdateDTO;
import com.hexagonal.challenge.aplication.port.out.CreativeRepositoryPort;
import com.hexagonal.challenge.domain.Creative;
import com.hexagonal.challenge.infraestructure.persistence.model.CreativeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
    void givenCreativeObject_whenCreateCreative_thenCreativeIsCreatedSuccessfully() {
        CreativeEntity creativeEntity = new CreativeEntity("1", "Test Creative", "Description", "http://test.com/doc.pdf", "1");
        CreativeDTO creativeDTO = new CreativeDTO(creativeEntity.getName(), creativeEntity.getDescription(), creativeEntity.getCreativeUrl(), creativeEntity.getCampaignId());

        when(creativeRepository.save(any(CreativeEntity.class))).thenReturn(creativeEntity);

        Creative result = manageCreativeUseCase.createCreative(creativeDTO);

        verify(creativeRepository).save(any(CreativeEntity.class));
        assertThat(result.getCreativeId()).isEqualTo(creativeEntity.getId());
        assertThat(result.getName()).isEqualTo(creativeDTO.getName());
        assertThat(result.getDescription()).isEqualTo(creativeDTO.getDescription());
        assertThat(result.getCreativeUrl()).isEqualTo(creativeDTO.getCreativeUrl());
    }

    @Test
    void givenPageableRequest_whenListCreatives_thenCreativesAreReturnedSuccessfully() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        CreativeEntity creative = new CreativeEntity("1", "Test Creative", "Description", "http://test.com/doc.pdf", "1");
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
    void givenCreativeId_whenGetCreativeById_thenCreativeIsFoundSuccessfully() {
        String id = "testId";
        Optional<CreativeEntity> creativeEntity = Optional.of(new CreativeEntity("1", "Test Creative", "Description", "http://test.com/doc.pdf", "1"));
        when(creativeRepository.findById(id)).thenReturn(creativeEntity);

        Optional<Creative> result = manageCreativeUseCase.getCreativeById(id);

        verify(creativeRepository).findById(id);

        assertThat(creativeEntity.get().getCreativeUrl()).isEqualTo(result.get().getCreativeUrl());
        assertThat(creativeEntity.get().getDescription()).isEqualTo(result.get().getDescription());
        assertThat(creativeEntity.get().getName()).isEqualTo(result.get().getName());
        assertThat(creativeEntity.get().getId()).isEqualTo(result.get().getCreativeId());
    }

    @Test
    void givenCreativeIdAndUpdatedInfo_whenUpdateCreative_thenCreativeIsUpdatedSuccessfully() {
        String id = "testId";
        CreativeEntity existingCreativeEntity = new CreativeEntity();
        CreativeEntity updatedCreativeEntity = new CreativeEntity();
        updatedCreativeEntity.setName("Updated Name");
        when(creativeRepository.findById(id)).thenReturn(Optional.of(existingCreativeEntity));
        when(creativeRepository.save(any(CreativeEntity.class))).thenReturn(updatedCreativeEntity);

        CreativeUpdateDTO updatedCreative = new CreativeUpdateDTO(updatedCreativeEntity.getName(), updatedCreativeEntity.getDescription(), updatedCreativeEntity.getCreativeUrl(), updatedCreativeEntity.getCampaignId());
        Optional<Creative> result = manageCreativeUseCase.updateCreative(id, updatedCreative);

        verify(creativeRepository).findById(id);
        verify(creativeRepository).save(existingCreativeEntity);
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void givenCreativeId_whenDeleteCreative_thenCreativeIsDeletedSuccessfully() {
        String id = "testId";
        CreativeEntity creative = new CreativeEntity();
        when(creativeRepository.findById(id)).thenReturn(Optional.of(creative));

        boolean result = manageCreativeUseCase.deleteCreative(id);

        verify(creativeRepository).delete(creative);
        assertTrue(result);
    }
}