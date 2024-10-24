package com.hexagonal.challenge.aplication.service;

import com.hexagonal.challenge.aplication.port.out.BrandRepository;
import com.hexagonal.challenge.domain.Brand;
import com.hexagonal.challenge.infraestructure.persistence.model.BrandEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

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