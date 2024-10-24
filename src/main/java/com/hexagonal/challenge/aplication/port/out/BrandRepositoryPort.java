package com.hexagonal.challenge.aplication.port.out;

import com.hexagonal.challenge.infraestructure.persistence.model.BrandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BrandRepositoryPort {

    BrandEntity save(BrandEntity brandEntity);

    Page<BrandEntity> findAll(Pageable pageable);

    void delete(BrandEntity brandEntity);

    Optional<BrandEntity> findById(String id);

    boolean existsById(String id);

    void deleteById(String id);
}
