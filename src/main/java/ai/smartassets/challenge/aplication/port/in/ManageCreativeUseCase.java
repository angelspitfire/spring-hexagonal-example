package ai.smartassets.challenge.aplication.port.in;

import ai.smartassets.challenge.domain.Creative;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ManageCreativeUseCase {
    Creative createCreative(Creative creative);

    List<Creative> listCreatives(PageRequest pageRequest);

    Optional<Creative> getCreativeById(String creativeId);

    Optional<Creative> updateCreative(String creativeId, Creative creative);

    boolean deleteCreative(String creativeId);
}
