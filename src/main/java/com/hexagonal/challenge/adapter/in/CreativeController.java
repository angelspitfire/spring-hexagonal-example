package com.hexagonal.challenge.adapter.in;

import com.hexagonal.challenge.aplication.dto.CreativeUpdateDTO;
import com.hexagonal.challenge.aplication.port.in.ManageCreativeUseCase;
import com.hexagonal.challenge.domain.Creative;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/creatives")
public class CreativeController {
    private final ManageCreativeUseCase manageCreativeUseCase;

    public CreativeController(ManageCreativeUseCase manageCreativeUseCase) {
        this.manageCreativeUseCase = manageCreativeUseCase;
    }

    @GetMapping
    public ResponseEntity<List<Creative>> listCreatives(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Creative> creatives = manageCreativeUseCase.listCreatives(PageRequest.of(page, size));
        return ResponseEntity.ok(creatives);
    }

    @GetMapping("/{creativeId}")
    public ResponseEntity<Creative> getCreativeById(@PathVariable String creativeId) {
        return manageCreativeUseCase.getCreativeById(creativeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{creativeId}")
    public ResponseEntity<Creative> updateCreative(@PathVariable String creativeId, @RequestBody CreativeUpdateDTO creative) {
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