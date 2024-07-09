package ai.smartassets.challenge.aplication.port.out;

import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CreativeRepositoryPort {

    Page<CreativeEntity> findAll(Pageable pageable);

    CreativeEntity save(CreativeEntity creative);

    Optional<CreativeEntity> findById(String id);

    void delete(CreativeEntity creative);

    List<CreativeEntity> findByCampaignId(String campaignId, PageRequest pageRequest);

    boolean existsById(String creativeId);
}
