package ai.smartassets.challenge.adapter.in;

import ai.smartassets.challenge.aplication.port.in.ManageBrandUseCase;
import ai.smartassets.challenge.domain.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {
    private final ManageBrandUseCase manageBrandUseCase;

    @Autowired
    public BrandController(ManageBrandUseCase manageBrandUseCase) {
        this.manageBrandUseCase = manageBrandUseCase;
    }

    @PostMapping
    public ResponseEntity<Brand> createBrand(@RequestBody Brand brand) {
        Brand createdBrand = manageBrandUseCase.createBrand(brand);
        return ResponseEntity.ok(createdBrand);
    }

    @GetMapping
    public ResponseEntity<List<Brand>> listBrands(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Brand> brands = manageBrandUseCase.listBrands(PageRequest.of(page, size));
        return ResponseEntity.ok(brands.toList());
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<Brand> getBrandById(@PathVariable String brandId) {
        return manageBrandUseCase.getBrandById(brandId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{brandId}")
    public ResponseEntity<Brand> updateBrand(@PathVariable String brandId, @RequestBody Brand brand) {
        return manageBrandUseCase.updateBrand(brandId, brand)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<?> deleteBrand(@PathVariable String brandId) {
        if (manageBrandUseCase.deleteBrand(brandId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
