package ai.smartassets.challenge.aplication.port.out;

import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CreativeRepository extends MongoRepository<CreativeEntity, String> {
    List<CreativeEntity> findByCampaignId(String campaignId);
}
