package ai.smartassets.challenge.aplication.port.in;

import ai.smartassets.challenge.aplication.dto.BrandCreationDto;
import ai.smartassets.challenge.aplication.dto.BrandUpdateDto;
import ai.smartassets.challenge.domain.Brand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ManageBrandUseCase {

    Brand createBrand(@Valid BrandCreationDto brandCreationDto);

    List<Brand> listBrands(Pageable pageable);

    Optional<Brand> getBrandById(String id);

    Optional<Brand> updateBrand(@NotNull @NotBlank String id, @Valid BrandUpdateDto brandDto);

    boolean deleteBrand(String id);
}
