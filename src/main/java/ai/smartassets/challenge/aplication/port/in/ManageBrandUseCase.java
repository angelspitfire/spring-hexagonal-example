package ai.smartassets.challenge.aplication.port.in;

import ai.smartassets.challenge.domain.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ManageBrandUseCase {

    Brand createBrand(Brand brand);

    Page<Brand> listBrands(Pageable pageable);

    Optional<Brand> getBrandById(String id);

    Optional<Brand> updateBrand(String id, Brand brand);

    boolean deleteBrand(String id);
}
