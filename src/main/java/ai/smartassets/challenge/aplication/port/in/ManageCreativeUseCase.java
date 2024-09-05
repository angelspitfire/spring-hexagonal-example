package ai.smartassets.challenge.aplication.port.in;

import ai.smartassets.challenge.aplication.dto.CreativeResponse;
import ai.smartassets.challenge.aplication.dto.CreativeUpdateRequest;
import ai.smartassets.challenge.domain.Creative;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ManageCreativeUseCase {
    CreativeResponse createCreative(Creative creative);

    List<CreativeResponse> listCreatives(PageRequest pageRequest);

    Optional<CreativeResponse> getCreativeById(String creativeId);

    Optional<CreativeResponse> updateCreative(String creativeId, CreativeUpdateRequest creative);

    boolean deleteCreative(String creativeId);
}
