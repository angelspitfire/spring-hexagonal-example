package com.hexagonal.challenge.adapter.in;

import com.hexagonal.challenge.aplication.dto.BrandCreationDto;
import com.hexagonal.challenge.aplication.dto.BrandUpdateDto;
import com.hexagonal.challenge.aplication.dto.CampaignCreationDTO;
import com.hexagonal.challenge.aplication.dto.CreativeUploadDTO;
import com.hexagonal.challenge.aplication.port.in.ManageBrandUseCase;
import com.hexagonal.challenge.aplication.port.in.ManageCampaignUseCase;
import com.hexagonal.challenge.domain.Brand;
import com.hexagonal.challenge.domain.Campaign;
import com.hexagonal.challenge.domain.Creative;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {
    private final ManageBrandUseCase manageBrandUseCase;
    private final ManageCampaignUseCase manageCampaignUseCase;

    public BrandController(ManageBrandUseCase manageBrandUseCase, ManageCampaignUseCase manageCampaignUseCase) {
        this.manageBrandUseCase = manageBrandUseCase;
        this.manageCampaignUseCase = manageCampaignUseCase;
    }

    @PostMapping
    public ResponseEntity<Brand> createBrand(@RequestBody @Valid BrandCreationDto brand) {
        Brand createdBrand = manageBrandUseCase.createBrand(brand);
        return ResponseEntity.ok(createdBrand);
    }

    @GetMapping
    public ResponseEntity<List<Brand>> listBrands(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Brand> brands = manageBrandUseCase.listBrands(PageRequest.of(page, size));
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/{brandId}")
    public ResponseEntity<Brand> getBrandById(@PathVariable String brandId) {
        return manageBrandUseCase.getBrandById(brandId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{brandId}")
    public ResponseEntity<Brand> updateBrand(@PathVariable String brandId, @RequestBody @Valid BrandUpdateDto brand) {
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
    public ResponseEntity<Campaign> createCampaignForBrand(@PathVariable String brandId, @RequestBody @Valid CampaignCreationDTO campaign) {
        Campaign createdCampaign = manageCampaignUseCase.createCampaignForBrand(brandId, campaign);
        return ResponseEntity.ok(createdCampaign);
    }

    @GetMapping("/{brandId}/campaigns")
    public ResponseEntity<List<Campaign>> listCampaignsForBrand(@PathVariable String brandId,
                                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                                @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Campaign> campaigns = manageCampaignUseCase.findCampaignsByBrandId(brandId, pageRequest);
        return ResponseEntity.ok(campaigns);
    }

    @GetMapping("/{brandId}/campaigns/{campaignId}/creatives")
    public ResponseEntity<List<Creative>> listCreativesForCampaign(@PathVariable String brandId, @PathVariable String campaignId, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Creative> creatives = manageCampaignUseCase.findCreativesByBrandIdAndCampaignId(brandId, campaignId, pageRequest);
        return ResponseEntity.ok(creatives);
    }

    @PostMapping("/{brandId}/campaigns/{campaignId}/creatives/upload")
    public ResponseEntity<Creative> uploadCreative(@PathVariable String brandId,
                                                   @PathVariable String campaignId,
                                                   @Valid @RequestBody CreativeUploadDTO creativeUploadDTO) {

        return manageCampaignUseCase
                .uploadCreativeForCampaign(brandId, campaignId, creativeUploadDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
