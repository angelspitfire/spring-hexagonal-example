package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.port.out.BrandRepository;
import ai.smartassets.challenge.domain.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ManageBrandUseCaseImplTest {

    private BrandRepository brandRepositoryPort;
    private ManageBrandUseCaseImpl manageBrandUseCase;

    @BeforeEach
    void setUp() {
        brandRepositoryPort = Mockito.mock(BrandRepository.class);
        manageBrandUseCase = new ManageBrandUseCaseImpl(brandRepositoryPort);
    }

    @Test
    void createBrand() {
        Brand brand = new Brand("1", "Test Brand", "Description");
        when(brandRepositoryPort.save(any(Brand.class))).thenReturn(brand);

        Brand createdBrand = manageBrandUseCase.createBrand(brand);

        assertEquals(brand, createdBrand);
    }

    @Test
    void listBrands() {
        PageRequest pageable = PageRequest.of(0, 10);
        Brand brand = new Brand("1", "Test Brand", "Description");
        Page<Brand> brandPage = new PageImpl<>(Collections.singletonList(brand));
        when(brandRepositoryPort.findAll(any(PageRequest.class))).thenReturn(brandPage);

        List<Brand> result = manageBrandUseCase.listBrands(pageable);

        assertFalse(result.isEmpty());
        assertEquals(brand, result.get(0));
    }

    @Test
    void getBrandById() {
        Brand brand = new Brand("1", "Test Brand", "Description");
        when(brandRepositoryPort.findById("1")).thenReturn(Optional.of(brand));

        Optional<Brand> foundBrand = manageBrandUseCase.getBrandById("1");

        assertTrue(foundBrand.isPresent());
        assertEquals(brand, foundBrand.get());
    }

    @Test
    void updateBrand() {
        Brand existingBrand = new Brand("1", "Test Brand", "Description");
        Brand updatedBrand = new Brand("1", "Updated Brand", "Updated Description");
        when(brandRepositoryPort.findById("1")).thenReturn(Optional.of(existingBrand));
        when(brandRepositoryPort.save(any(Brand.class))).thenReturn(updatedBrand);

        Optional<Brand> result = manageBrandUseCase.updateBrand("1", updatedBrand);

        assertTrue(result.isPresent());
        assertEquals(updatedBrand.getName(), result.get().getName());
        assertEquals(updatedBrand.getDescription(), result.get().getDescription());
    }

    @Test
    void deleteBrand() {
        Brand brand = new Brand("1", "Test Brand", "Description");
        when(brandRepositoryPort.findById("1")).thenReturn(Optional.of(brand));
        Mockito.doNothing().when(brandRepositoryPort).delete(any(Brand.class));

        boolean result = manageBrandUseCase.deleteBrand("1");

        assertTrue(result);
        Mockito.verify(brandRepositoryPort, Mockito.times(1)).delete(brand);
    }
}