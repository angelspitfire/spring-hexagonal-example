package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.port.out.BrandRepositoryPort;
import ai.smartassets.challenge.domain.Brand;
import ai.smartassets.challenge.infraestructure.persistence.model.BrandEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ManageBrandUseCaseImplTest {

    private BrandRepositoryPort brandRepositoryPort;
    private ManageBrandUseCaseImpl manageBrandUseCase;

    @BeforeEach
    void setUp() {
        brandRepositoryPort = Mockito.mock(BrandRepositoryPort.class);
        manageBrandUseCase = new ManageBrandUseCaseImpl(brandRepositoryPort);
    }

    @Test
    void createBrand() {
        BrandEntity brandEntity = new BrandEntity("1", "Test Brand", "Description");
        when(brandRepositoryPort.save(any(BrandEntity.class))).thenReturn(brandEntity);

        Brand brand = new Brand("1", "Test Brand", "Description");
        Brand createdBrand = manageBrandUseCase.createBrand(brand);

        assertThat(createdBrand.getBrandId()).isEqualTo(brandEntity.getId());
        assertThat(createdBrand.getName()).isEqualTo(brandEntity.getName());
        assertThat(createdBrand.getDescription()).isEqualTo(brandEntity.getDescription());
    }

    @Test
    void listBrands() {
        PageRequest pageable = PageRequest.of(0, 10);
        BrandEntity brand = new BrandEntity("1", "Test Brand", "Description");
        Page<BrandEntity> brandPage = new PageImpl<>(Collections.singletonList(brand));
        when(brandRepositoryPort.findAll(any(PageRequest.class))).thenReturn(brandPage);

        List<Brand> result = manageBrandUseCase.listBrands(pageable);

        assertFalse(result.isEmpty());
        assertThat(result.get(0).getBrandId()).isEqualTo(brand.getId());
        assertThat(result.get(0).getName()).isEqualTo(brand.getName());
        assertThat(result.get(0).getDescription()).isEqualTo(brand.getDescription());
    }

    @Test
    void getBrandById() {
        BrandEntity brand = new BrandEntity("1", "Test Brand", "Description");
        when(brandRepositoryPort.findById("1")).thenReturn(Optional.of(brand));

        Optional<Brand> foundBrand = manageBrandUseCase.getBrandById("1");

        assertTrue(foundBrand.isPresent());
        assertThat(foundBrand.get().getBrandId()).isEqualTo(brand.getId());
        assertThat(foundBrand.get().getName()).isEqualTo(brand.getName());
        assertThat(foundBrand.get().getDescription()).isEqualTo(brand.getDescription());
    }

    @Test
    void updateBrand() {
        BrandEntity existingEntityBrand = new BrandEntity("1", "Test Brand", "Description");
        BrandEntity updatedEntityBrand = new BrandEntity("1", "Updated Brand", "Updated Description");
        when(brandRepositoryPort.findById("1")).thenReturn(Optional.of(existingEntityBrand));
        when(brandRepositoryPort.save(any(BrandEntity.class))).thenReturn(updatedEntityBrand);

        Brand updatedBrand = new Brand(updatedEntityBrand.getId(), updatedEntityBrand.getName(), updatedEntityBrand.getDescription());
        Optional<Brand> result = manageBrandUseCase.updateBrand("1", updatedBrand);

        assertTrue(result.isPresent());
        assertEquals(updatedEntityBrand.getName(), result.get().getName());
        assertEquals(updatedEntityBrand.getDescription(), result.get().getDescription());
    }

    @Test
    void deleteBrand() {
        BrandEntity brand = new BrandEntity("1", "Test Brand", "Description");
        when(brandRepositoryPort.findById("1")).thenReturn(Optional.of(brand));
        Mockito.doNothing().when(brandRepositoryPort).deleteById("1");

        boolean result = manageBrandUseCase.deleteBrand("1");

        assertTrue(result);
        Mockito.verify(brandRepositoryPort, Mockito.times(1)).findById("1");
        Mockito.verify(brandRepositoryPort, Mockito.times(1)).deleteById("1");
    }
}