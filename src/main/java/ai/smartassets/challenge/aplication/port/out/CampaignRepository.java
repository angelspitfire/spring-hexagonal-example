package ai.smartassets.challenge.aplication.port.out;

import ai.smartassets.challenge.domain.Campaign;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CampaignRepository extends MongoRepository<Campaign, String>{
}

