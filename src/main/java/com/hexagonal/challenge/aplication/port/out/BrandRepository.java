package com.hexagonal.challenge.aplication.port.out;

import com.hexagonal.challenge.infraestructure.persistence.model.BrandEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrandRepository extends MongoRepository<BrandEntity, String> {
}
