package com.hexagonal.challenge.aplication.port.out;

import com.hexagonal.challenge.infraestructure.persistence.model.CampaignEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CampaignRepositoryPort {
    CampaignEntity save(CampaignEntity entity);

    Page<CampaignEntity> findAll(PageRequest pageRequest);

    Optional<CampaignEntity> findById(String id);

    void delete(CampaignEntity campaign);

    List<CampaignEntity> findByBrandId(String brandId, Pageable pageable);

    List<CampaignEntity> findByBrandIdAndId(String brandId, String campaignId, PageRequest pageRequest);

    void deleteById(String id);

    boolean existsById(String campaignId);
}
