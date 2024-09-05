package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.dto.BrandCreationDto;
import ai.smartassets.challenge.aplication.dto.BrandResponse;
import ai.smartassets.challenge.aplication.dto.BrandUpdateDto;
import ai.smartassets.challenge.aplication.exception.BrandCreationException;
import ai.smartassets.challenge.aplication.exception.BrandNotFoundException;
import ai.smartassets.challenge.aplication.port.out.BrandRepositoryPort;
import ai.smartassets.challenge.infraestructure.persistence.model.BrandEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ManageBrandUseCaseImplTest {

    private BrandRepositoryPort brandRepositoryPort;
    private ManageBrandUseCaseImpl manageBrandUseCase;

    @BeforeEach
    void setUp() {
        brandRepositoryPort = Mockito.mock(BrandRepositoryPort.class);
        manageBrandUseCase = new ManageBrandUseCaseImpl(brandRepositoryPort);
    }

    @Test
    void givenBrandDto_whenCreateBrand_thenBrandIsCreatedSuccessfully() {
        BrandEntity brandEntity = new BrandEntity("1", "Test Brand", "Description");
        when(brandRepositoryPort.save(any(BrandEntity.class))).thenReturn(brandEntity);

        BrandCreationDto brand = new BrandCreationDto("Test Brand", "Description");
        BrandResponse createdBrand = manageBrandUseCase.createBrand(brand);

        assertThat(createdBrand.brandId()).isEqualTo(brandEntity.getId());
        assertThat(createdBrand.name()).isEqualTo(brandEntity.getName());
        assertThat(createdBrand.description()).isEqualTo(brandEntity.getDescription());
    }

    @Test
    void givenPageable_whenListBrands_thenBrandsAreReturned() {
        PageRequest pageable = PageRequest.of(0, 10);
        BrandEntity brand = new BrandEntity("1", "Test Brand", "Description");
        Page<BrandEntity> brandPage = new PageImpl<>(Collections.singletonList(brand));
        when(brandRepositoryPort.findAll(any(PageRequest.class))).thenReturn(brandPage);

        List<BrandResponse> result = manageBrandUseCase.listBrands(pageable);

        assertFalse(result.isEmpty());
        assertThat(result.get(0).brandId()).isEqualTo(brand.getId());
        assertThat(result.get(0).name()).isEqualTo(brand.getName());
        assertThat(result.get(0).description()).isEqualTo(brand.getDescription());
    }

    @Test
    void givenBrandId_whenGetBrandById_thenBrandIsFound() {
        BrandEntity brand = new BrandEntity("1", "Test Brand", "Description");
        when(brandRepositoryPort.findById("1")).thenReturn(Optional.of(brand));

        Optional<BrandResponse> foundBrand = manageBrandUseCase.getBrandById("1");

        assertTrue(foundBrand.isPresent());
        assertThat(foundBrand.get().brandId()).isEqualTo(brand.getId());
        assertThat(foundBrand.get().name()).isEqualTo(brand.getName());
        assertThat(foundBrand.get().description()).isEqualTo(brand.getDescription());
    }

    @Test
    void givenBrandIdAndUpdatedInfo_whenUpdateBrand_thenBrandIsUpdatedSuccessfully() {
        BrandEntity existingEntityBrand = new BrandEntity("1", "Test Brand", "Description");
        BrandEntity updatedEntityBrand = new BrandEntity("1", "Updated Brand", "Updated Description");
        when(brandRepositoryPort.findById("1")).thenReturn(Optional.of(existingEntityBrand));
        when(brandRepositoryPort.save(any(BrandEntity.class))).thenReturn(updatedEntityBrand);

        BrandUpdateDto updatedBrand = new BrandUpdateDto("Updated Brand", "Updated Description");
        Optional<BrandResponse> result = manageBrandUseCase.updateBrand("1", updatedBrand);

        assertTrue(result.isPresent());
        assertEquals(updatedEntityBrand.getName(), result.get().name());
        assertEquals(updatedEntityBrand.getDescription(), result.get().description());
    }

    @Test
    void givenBrandId_whenDeleteBrand_thenBrandIsDeletedSuccessfully() {
        BrandEntity brand = new BrandEntity("1", "Test Brand", "Description");
        when(brandRepositoryPort.existsById("1")).thenReturn(true);
        Mockito.doNothing().when(brandRepositoryPort).deleteById("1");

        boolean result = manageBrandUseCase.deleteBrand("1");

        assertTrue(result);
        verify(brandRepositoryPort, times(1)).existsById("1");
        verify(brandRepositoryPort, times(1)).deleteById("1");
    }

    @Test
    void givenDataAccessException_whenCreateBrand_thenBrandCreationExceptionIsThrown() {
        BrandCreationDto brandCreationDto = new BrandCreationDto( "Test Brand", "Description");
        when(brandRepositoryPort.save(any(BrandEntity.class))).thenThrow(new DataAccessException("Test Exception") {
        });

        assertThrows(BrandCreationException.class, () -> manageBrandUseCase.createBrand(brandCreationDto));
    }

    @Test
    void givenNoBrands_whenListBrands_thenEmptyListIsReturned() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<BrandEntity> brandPage = new PageImpl<>(Collections.emptyList());
        when(brandRepositoryPort.findAll(any(PageRequest.class))).thenReturn(brandPage);

        List<BrandResponse> result = manageBrandUseCase.listBrands(pageable);

        assertTrue(result.isEmpty());
    }

    @Test
    void givenNonExistingBrandId_whenGetBrandById_thenOptionalEmptyIsReturned() {
        when(brandRepositoryPort.findById("non-existing")).thenReturn(Optional.empty());

        Optional<BrandResponse> result = manageBrandUseCase.getBrandById("non-existing");

        assertFalse(result.isPresent());
    }

    @Test
    void givenNonExistingBrand_whenUpdateBrand_thenOptionalEmptyIsReturned() {
        BrandUpdateDto updatedBrand = new BrandUpdateDto("Updated Brand", "Updated Description");
        when(brandRepositoryPort.findById("non-existing")).thenReturn(Optional.empty());

        Optional<BrandResponse> result = manageBrandUseCase.updateBrand("non-existing", updatedBrand);

        assertFalse(result.isPresent());
    }

    @Test
    void givenNonExistingBrandId_whenDeleteBrand_thenBrandNotFoundExceptionIsThrown() {
        String nonExistingBrandId = "non-existing";
        when(brandRepositoryPort.existsById(nonExistingBrandId)).thenReturn(false);

        assertThrows(BrandNotFoundException.class, () -> manageBrandUseCase.deleteBrand(nonExistingBrandId));

        verify(brandRepositoryPort, times(1)).existsById(nonExistingBrandId);
        verify(brandRepositoryPort, never()).deleteById(nonExistingBrandId);
    }
}