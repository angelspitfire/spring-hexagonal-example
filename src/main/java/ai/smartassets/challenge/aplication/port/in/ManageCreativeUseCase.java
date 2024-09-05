package ai.smartassets.challenge.aplication.port.in;

import ai.smartassets.challenge.aplication.dto.CreativeDTO;
import ai.smartassets.challenge.aplication.dto.CreativeResponse;
import ai.smartassets.challenge.aplication.dto.CreativeUpdateDTO;
import ai.smartassets.challenge.domain.Creative;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface ManageCreativeUseCase {

    CreativeResponse createCreative(@Valid CreativeDTO creativeDTO);

    List<CreativeResponse> listCreatives(PageRequest pageRequest);

    Optional<CreativeResponse> getCreativeById(String creativeId);

    Optional<CreativeResponse> updateCreative(@NotBlank String creativeId, CreativeUpdateDTO creative);

    boolean deleteCreative(String creativeId);
}
