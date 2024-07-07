package ai.smartassets.challenge.aplication.port.out;

import ai.smartassets.challenge.domain.Campaign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CampaignRepositoryPort {
    Page<Campaign> findAll(Pageable pageable);

    Campaign save(Campaign campaign);

    Optional<Campaign> findById(String id);

    void delete(Campaign campaign);

    void deleteById(String id);
}
