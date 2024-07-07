package ai.smartassets.challenge.aplication.port.out;

import ai.smartassets.challenge.infraestructure.persistence.model.BrandEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrandRepository extends MongoRepository<BrandEntity, String> {
}
