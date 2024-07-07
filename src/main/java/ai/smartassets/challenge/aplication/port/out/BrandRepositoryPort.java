package ai.smartassets.challenge.aplication.port.out;

import ai.smartassets.challenge.domain.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BrandRepositoryPort {
    Brand save(Brand brand);

    Page<Brand> findAll(Pageable pageable);

    Optional<Brand> findById(String id);

    void delete(Brand brand);
}
