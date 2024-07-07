package ai.smartassets.challenge.aplication.port.in;

import ai.smartassets.challenge.domain.Creative;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface ManageCreativeUseCase {
    Creative createCreative(Creative creative);

    Page<Creative> listCreatives(PageRequest of);

    Optional<Creative> getCreativeById(String creativeId);

    Optional<Creative> updateCreative(String creativeId, Creative creative);

    boolean deleteCreative(String creativeId);
}
