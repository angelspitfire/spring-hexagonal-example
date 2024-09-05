package ai.smartassets.challenge.adapter.in;

import ai.smartassets.challenge.aplication.dto.CampaignResponse;
import ai.smartassets.challenge.aplication.dto.CampaignUpdateDto;
import ai.smartassets.challenge.aplication.port.in.ManageCampaignUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/campaigns")
public class CampaignController {
    private final ManageCampaignUseCase manageCampaignUseCase;

    @Autowired
    public CampaignController(ManageCampaignUseCase manageCampaignUseCase) {
        this.manageCampaignUseCase = manageCampaignUseCase;
    }

    @GetMapping
    public ResponseEntity<List<CampaignResponse>> listCampaigns(@RequestParam(value = "page", defaultValue = "0") int page,
                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        List<CampaignResponse> campaigns = manageCampaignUseCase.listCampaigns(PageRequest.of(page, size));
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<CampaignResponse> getCampaignById(@PathVariable String campaignId) {
        return manageCampaignUseCase.getCampaignById(campaignId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{campaignId}")
    public ResponseEntity<CampaignResponse> updateCampaign(@PathVariable String campaignId, @RequestBody CampaignUpdateDto campaign) {
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