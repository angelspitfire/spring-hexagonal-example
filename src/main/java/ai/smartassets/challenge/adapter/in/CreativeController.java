package ai.smartassets.challenge.adapter.in;

import ai.smartassets.challenge.aplication.port.in.ManageCreativeUseCase;
import ai.smartassets.challenge.domain.Creative;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @PostMapping
    public ResponseEntity<Creative> createCreative(@RequestBody Creative creative) {
        Creative createdCreative = manageCreativeUseCase.createCreative(creative);
        return ResponseEntity.ok(createdCreative);
    }

    @GetMapping
    public ResponseEntity<List<Creative>> listCreatives(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Creative> creatives = manageCreativeUseCase.listCreatives(PageRequest.of(page, size));
        return ResponseEntity.ok(creatives.toList());
    }

    @GetMapping("/{creativeId}")
    public ResponseEntity<Creative> getCreativeById(@PathVariable String creativeId) {
        return manageCreativeUseCase.getCreativeById(creativeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{creativeId}")
    public ResponseEntity<Creative> updateCreative(@PathVariable String creativeId, @RequestBody Creative creative) {
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