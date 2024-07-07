package ai.smartassets.challenge.aplication.port.out;

import ai.smartassets.challenge.domain.Creative;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CreativeRepositoryPort {

    Page<Creative> findAll(Pageable pageable);

    Creative save(Creative creative);

    Optional<Creative> findById(String id);

    void delete(Creative creative);

    void deleteById(String id);
}

