package com.hexagonal.challenge.aplication.port.out;

import com.hexagonal.challenge.infraestructure.persistence.model.CampaignEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CampaignRepository extends MongoRepository<CampaignEntity, String>{
    List<CampaignEntity> findByBrandIdAndId(String brandId, String campaignId, PageRequest pageRequest);

    List<CampaignEntity> findByBrandId(String brandId, Pageable pageable);
}

