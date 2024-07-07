package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.aplication.port.out.BrandRepository;
import ai.smartassets.challenge.domain.Brand;
import ai.smartassets.challenge.infraestructure.persistence.model.BrandEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        BrandEntity brandEntity = new BrandEntity("1", "Test Brand", "Description");
        when(brandRepository.save(any(BrandEntity.class))).thenReturn(brandEntity);

        Brand brand = new Brand("1", "Test Brand", "Description");

        Brand savedBrand = brandRepositoryAdapter.save(brand);

        assertThat(savedBrand.getBrandId()).isEqualTo(brandEntity.getId());
        assertThat(savedBrand.getName()).isEqualTo(brandEntity.getName());
        assertThat(savedBrand.getDescription()).isEqualTo(brandEntity.getDescription());
    }

    @Test
    void findById() {
        BrandEntity brand = new BrandEntity("1", "Test Brand", "Description");
        when(brandRepository.findById("1")).thenReturn(Optional.of(brand));

        Optional<Brand> foundBrand = brandRepositoryAdapter.findById("1");

        assertThat(foundBrand.get().getBrandId()).isEqualTo(brand.getId());
        assertThat(foundBrand.get().getName()).isEqualTo(brand.getName());
        assertThat(foundBrand.get().getDescription()).isEqualTo(brand.getDescription());
    }

    @Test
    void delete() {
        BrandEntity brandEntity = new BrandEntity("1", "Test Brand", "Description");
        Brand brand = new Brand("1", "Test Brand", "Description");
        brandRepositoryAdapter.delete(brand);
        verify(brandRepository).delete(brandEntity);
    }
}