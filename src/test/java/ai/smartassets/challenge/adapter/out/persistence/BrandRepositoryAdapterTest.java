package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.aplication.port.out.BrandRepository;
import ai.smartassets.challenge.domain.Brand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BrandRepositoryAdapterTest {

    private BrandRepository brandRepository;
    private BrandRepositoryAdapter brandRepositoryAdapter;

    @BeforeEach
    void setUp() {
        brandRepository = Mockito.mock(BrandRepository.class);
        brandRepositoryAdapter = new BrandRepositoryAdapter(brandRepository);
    }

    @Test
    void save() {
        Brand brand = new Brand("1", "Test Brand", "Description");
        when(brandRepository.save(any(Brand.class))).thenReturn(brand);

        Brand savedBrand = brandRepositoryAdapter.save(brand);

        assertEquals(brand, savedBrand);
    }

    @Test
    void findById() {
        Brand brand = new Brand("1", "Test Brand", "Description");
        when(brandRepository.findById("1")).thenReturn(Optional.of(brand));

        Optional<Brand> foundBrand = brandRepositoryAdapter.findById("1");

        assertEquals(Optional.of(brand), foundBrand);
    }

    @Test
    void delete() {
        Brand brand = new Brand("1", "Test Brand", "Description");
        brandRepositoryAdapter.delete(brand);

        verify(brandRepository).delete(brand);
    }
}