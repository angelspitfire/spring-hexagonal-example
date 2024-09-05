package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.aplication.port.out.BrandRepository;
import ai.smartassets.challenge.infraestructure.persistence.model.BrandEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class BrandRepositoryAdapterTest {

    @Mock
    private BrandRepository brandMongoRepository; // Assuming this is the MongoDB repository

    private BrandRepositoryAdapter brandRepositoryAdapter;


    @BeforeEach
    void setUp() {
        openMocks(this);
        brandRepositoryAdapter = new BrandRepositoryAdapter(brandMongoRepository);
    }

    @Test
    void saveBrand_Success() {
        BrandEntity brand = new BrandEntity("brand123", "Brand Name", "Brand Description");
        when(brandMongoRepository.save(any(BrandEntity.class))).thenReturn(brand);
        BrandEntity savedBrand = brandRepositoryAdapter.save(brand);
        assertNotNull(savedBrand);
        assertEquals("brand123", savedBrand.getId());
    }

    @Test
    void findBrandById_Success() {
        BrandEntity brand = new BrandEntity("brand123", "Brand Name", "Brand Description");
        when(brandMongoRepository.findById("brand123")).thenReturn(Optional.of(brand));
        assertTrue(brandRepositoryAdapter.findById("brand123").isPresent());
    }

    @Test
    void findBrandById_NotFound() {
        when(brandMongoRepository.findById("brand123")).thenReturn(java.util.Optional.empty());
        assertFalse(brandRepositoryAdapter.findById("brand123").isPresent());
        assertThat(brandRepositoryAdapter.findById("brand123")).isEmpty();
    }

    @Test
    void deleteBrandById_Success() {
        doNothing().when(brandMongoRepository).deleteById("brand123");
        brandRepositoryAdapter.deleteById("brand123");
        verify(brandMongoRepository).deleteById("brand123");
    }

    @Test
    void brandExistsById_Exists() {
        when(brandMongoRepository.existsById("brand123")).thenReturn(true);
        assertTrue(brandRepositoryAdapter.existsById("brand123"));
    }

    @Test
    void brandExistsById_NotExists() {
        when(brandMongoRepository.existsById("nonExistingId")).thenReturn(false);
        assertFalse(brandRepositoryAdapter.existsById("nonExistingId"));
    }

    @Test
    void findAllBrands_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<BrandEntity> brandList = List.of(new BrandEntity("brand123", "Brand Name", "Brand Description"));
        Page<BrandEntity> brandPage = new PageImpl<>(brandList, pageable, brandList.size());
        when(brandMongoRepository.findAll(pageable)).thenReturn(brandPage);

        Page<BrandEntity> result = brandRepositoryAdapter.findAll(pageable);
        assertThat(result.getContent()).hasSize(1);
        assertEquals("brand123", result.getContent().get(0).getId());
    }

    @Test
    public void deleteBrand_Success() {
        BrandEntity brand = new BrandEntity("brand123", "Brand Name", "Brand Description");
        doNothing().when(brandMongoRepository).delete(brand);
        brandRepositoryAdapter.delete(brand);
        verify(brandMongoRepository).delete(brand);
    }
}