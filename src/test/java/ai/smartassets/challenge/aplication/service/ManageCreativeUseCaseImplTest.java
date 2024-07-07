package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.domain.Creative;
import ai.smartassets.challenge.aplication.port.out.CreativeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

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
        Creative creative = mock(Creative.class);
        when(creativeRepository.save(any(Creative.class))).thenReturn(creative);

        Creative result = manageCreativeUseCase.createCreative(creative);

        verify(creativeRepository).save(creative);
        assertEquals(creative, result);
    }

    @Test
    void listCreatives() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Creative> page = mock(Page.class);
        when(creativeRepository.findAll(pageRequest)).thenReturn(page);

        Page<Creative> result = manageCreativeUseCase.listCreatives(pageRequest);

        verify(creativeRepository).findAll(pageRequest);
        assertEquals(page, result);
    }

    @Test
    void getCreativeById() {
        String id = "testId";
        Optional<Creative> creative = Optional.of(mock(Creative.class));
        when(creativeRepository.findById(id)).thenReturn(creative);

        Optional<Creative> result = manageCreativeUseCase.getCreativeById(id);

        verify(creativeRepository).findById(id);
        assertEquals(creative, result);
    }

    @Test
    void updateCreative() {
        String id = "testId";
        Creative existingCreative = new Creative();
        Creative updatedCreative = new Creative();
        updatedCreative.setName("Updated Name");
        when(creativeRepository.findById(id)).thenReturn(Optional.of(existingCreative));
        when(creativeRepository.save(any(Creative.class))).thenReturn(updatedCreative);

        Optional<Creative> result = manageCreativeUseCase.updateCreative(id, updatedCreative);

        verify(creativeRepository).findById(id);
        verify(creativeRepository).save(existingCreative);
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void deleteCreative() {
        String id = "testId";
        Creative creative = new Creative();
        when(creativeRepository.findById(id)).thenReturn(Optional.of(creative));

        boolean result = manageCreativeUseCase.deleteCreative(id);

        verify(creativeRepository).delete(creative);
        assertTrue(result);
    }
}