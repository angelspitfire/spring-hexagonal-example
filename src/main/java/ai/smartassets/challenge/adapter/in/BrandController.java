package ai.smartassets.challenge.adapter.in;

import ai.smartassets.challenge.aplication.dto.BrandCreationRequest;
import ai.smartassets.challenge.aplication.dto.BrandResponse;
import ai.smartassets.challenge.aplication.dto.CampaignResponse;
import ai.smartassets.challenge.aplication.dto.CreativeUploadRequest;
import ai.smartassets.challenge.aplication.port.in.ManageBrandUseCase;
import ai.smartassets.challenge.aplication.port.in.ManageCampaignUseCase;
import ai.smartassets.challenge.domain.Brand;
import ai.smartassets.challenge.domain.Campaign;
import ai.smartassets.challenge.domain.Creative;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {
    private final ManageBrandUseCase manageBrandUseCase;
    private final ManageCampaignUseCase manageCampaignUseCase;

    @Autowired
    public BrandController(ManageBrandUseCase manageBrandUseCase, ManageCampaignUseCase manageCampaignUseCase) {
        this.manageBrandUseCase = manageBrandUseCase;
        this.manageCampaignUseCase = manageCampaignUseCase;
    }

    @PostMapping
    public ResponseEntity<BrandResponse> createBrand(@RequestBody BrandCreationRequest brand) {
        BrandResponse createdBrand = manageBrandUseCase.createBrand(brand);
        return ResponseEntity.ok(createdBrand);
    }

    @GetMapping
    public ResponseEntity<List<BrandResponse>> listBrands(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        List<BrandResponse> brands = manageBrandUseCase.listBrands(PageRequest.of(page, size));
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable String brandId) {
        return manageBrandUseCase.getBrandById(brandId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{brandId}")
    public ResponseEntity<BrandResponse> updateBrand(@PathVariable String brandId, @RequestBody Brand brand) {
        return manageBrandUseCase.updateBrand(brandId, brand)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<?> deleteBrand(@PathVariable String brandId) {
        if (manageBrandUseCase.deleteBrand(brandId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{brandId}/campaigns")
    public ResponseEntity<CampaignResponse> createCampaignForBrand(@PathVariable String brandId, @RequestBody Campaign campaign) {
        CampaignResponse createdCampaign = manageCampaignUseCase.createCampaignForBrand(brandId, campaign);
        return ResponseEntity.ok(createdCampaign);
    }

    @GetMapping("/{brandId}/campaigns")
    public ResponseEntity<List<CampaignResponse>> listCampaignsForBrand(@PathVariable String brandId,
                                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<CampaignResponse> campaigns = manageCampaignUseCase.findCampaignsByBrandId(brandId, pageRequest);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/brands/{brandId}/campaigns/{campaignId}/creatives")
    public ResponseEntity<List<Creative>> listCreativesForCampaign(@PathVariable String brandId, @PathVariable String campaignId, @RequestParam(value = "page", defaultValue = "0") int page,@RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Creative> creatives = manageCampaignUseCase.findCreativesByBrandIdAndCampaignId(brandId, campaignId, pageRequest);
        return ResponseEntity.ok(creatives);
    }

    @PostMapping("/{brandId}/campaigns/{campaignId}/creatives/upload")
    public ResponseEntity<Creative> uploadCreative(@PathVariable String brandId,
                                                   @PathVariable String campaignId,
                                                   @RequestParam("file") MultipartFile file,
                                                   @RequestParam("name") String name,
                                                   @RequestParam("description") String description) {


        CreativeUploadRequest creativeUploadRequest = new CreativeUploadRequest(name, description, file);

        Creative createdCreative = manageCampaignUseCase.uploadCreativeForCampaign(brandId, campaignId, creativeUploadRequest);
        return ResponseEntity.ok(createdCreative);
    }
}
