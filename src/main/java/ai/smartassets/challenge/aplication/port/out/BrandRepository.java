package ai.smartassets.challenge.aplication.port.out;

import ai.smartassets.challenge.domain.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrandRepository extends MongoRepository<Brand, String> {
}
