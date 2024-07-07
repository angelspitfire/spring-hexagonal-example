package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.port.out.BrandRepository;
import ai.smartassets.challenge.domain.Brand;
import ai.smartassets.challenge.infraestructure.persistence.model.BrandEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand saveBrand(Brand brand) {
        BrandEntity entity = new BrandEntity(brand.getBrandId(), brand.getName(), brand.getDescription());
        return getBrand(brandRepository.save(entity));
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll().stream().map(BrandService::getBrand).toList();
    }

    private static Brand getBrand(BrandEntity brandEntity) {
        return new Brand(brandEntity.getId(), brandEntity.getName(), brandEntity.getDescription());
    }
}