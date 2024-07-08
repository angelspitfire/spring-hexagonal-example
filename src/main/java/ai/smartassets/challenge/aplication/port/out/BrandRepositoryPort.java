package ai.smartassets.challenge.aplication.port.out;

import ai.smartassets.challenge.infraestructure.persistence.model.BrandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BrandRepositoryPort {

    BrandEntity save(BrandEntity brandEntity);

    Page<BrandEntity> findAll(Pageable pageable);

    void delete(BrandEntity brandEntity);

    Optional<BrandEntity> findById(String id);
}
