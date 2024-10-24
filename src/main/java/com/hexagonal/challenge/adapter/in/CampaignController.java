package com.hexagonal.challenge.adapter.in;

import com.hexagonal.challenge.aplication.dto.CampaignUpdateDto;
import com.hexagonal.challenge.aplication.port.in.ManageCampaignUseCase;
import com.hexagonal.challenge.domain.Campaign;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {
    private final ManageCampaignUseCase manageCampaignUseCase;

    public CampaignController(ManageCampaignUseCase manageCampaignUseCase) {
        this.manageCampaignUseCase = manageCampaignUseCase;
    }

    @GetMapping
    public ResponseEntity<List<Campaign>> listCampaigns(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Campaign> campaigns = manageCampaignUseCase.listCampaigns(PageRequest.of(page, size));
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<Campaign> getCampaignById(@PathVariable String campaignId) {
        return manageCampaignUseCase.getCampaignById(campaignId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{campaignId}")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable String campaignId, @RequestBody CampaignUpdateDto campaign) {
        return manageCampaignUseCase.updateCampaign(campaignId, campaign)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{campaignId}")
    public ResponseEntity<?> deleteCampaign(@PathVariable String campaignId) {
        if (manageCampaignUseCase.deleteCampaign(campaignId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}