package ai.smartassets.challenge.aplication.port.in;

import ai.smartassets.challenge.aplication.dto.BrandResponse;
import ai.smartassets.challenge.aplication.dto.BrandCreationRequest;
import ai.smartassets.challenge.domain.Brand;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ManageBrandUseCase {

    BrandResponse createBrand(BrandCreationRequest brand);

    List<BrandResponse> listBrands(Pageable pageable);

    Optional<BrandResponse> getBrandById(String id);

    Optional<BrandResponse> updateBrand(String id, Brand brand);

    boolean deleteBrand(String id);
}
