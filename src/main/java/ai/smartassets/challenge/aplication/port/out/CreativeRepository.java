package ai.smartassets.challenge.aplication.port.out;

import ai.smartassets.challenge.domain.Creative;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CreativeRepository extends MongoRepository<Creative, String> {
}
