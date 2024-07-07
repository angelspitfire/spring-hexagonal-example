package ai.smartassets.challenge.aplication.port.out;

import ai.smartassets.challenge.infraestructure.persistence.model.CampaignEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CampaignRepository extends MongoRepository<CampaignEntity, String>{
    List<CampaignEntity> findByBrandIdAndId(String brandId, String campaignId);

    List<CampaignEntity> findByBrandId(String brandId);
}

