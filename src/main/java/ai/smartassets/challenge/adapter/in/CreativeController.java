package ai.smartassets.challenge.adapter.in;

import ai.smartassets.challenge.aplication.dto.CreativeResponse;
import ai.smartassets.challenge.aplication.dto.CreativeUpdateDTO;
import ai.smartassets.challenge.aplication.port.in.ManageCreativeUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/creatives")
public class CreativeController {
    private final ManageCreativeUseCase manageCreativeUseCase;

    @Autowired
    public CreativeController(ManageCreativeUseCase manageCreativeUseCase) {
        this.manageCreativeUseCase = manageCreativeUseCase;
    }

    @GetMapping
    public ResponseEntity<List<CreativeResponse>> listCreatives(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        List<CreativeResponse> creatives = manageCreativeUseCase.listCreatives(PageRequest.of(page, size));
        return ResponseEntity.ok(creatives);
    }

    @GetMapping("/{creativeId}")
    public ResponseEntity<CreativeResponse> getCreativeById(@PathVariable String creativeId) {
        return manageCreativeUseCase.getCreativeById(creativeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{creativeId}")
    public ResponseEntity<CreativeResponse> updateCreative(@PathVariable String creativeId, @RequestBody CreativeUpdateDTO creative) {
        return manageCreativeUseCase.updateCreative(creativeId, creative)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{creativeId}")
    public ResponseEntity<?> deleteCreative(@PathVariable String creativeId) {
        if (manageCreativeUseCase.deleteCreative(creativeId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}