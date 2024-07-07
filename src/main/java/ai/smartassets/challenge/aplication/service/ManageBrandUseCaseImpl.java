package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.port.in.ManageBrandUseCase;
import ai.smartassets.challenge.aplication.port.out.BrandRepository;
import ai.smartassets.challenge.domain.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManageBrandUseCaseImpl implements ManageBrandUseCase {

    private final BrandRepository brandRepository;

    @Autowired
    public ManageBrandUseCaseImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand createBrand(Brand brand) {
        //TODO: Implement creation logic here
        return brandRepository.save(brand);
    }

    @Override
    public Page<Brand> listBrands(Pageable pageable) {
        //TODO: Implement listing logic here
        return brandRepository.findAll(pageable);
    }

    @Override
    public Optional<Brand> getBrandById(String id) {
        //TODO: Implement get by ID logic here
        return brandRepository.findById(id);
    }

    @Override
    public Optional<Brand> updateBrand(String id, Brand brand) {
        //TODO: Implement update logic here, including fetching and updating the brand
        return brandRepository.findById(id)
                .map(existingBrand -> {
                    //TODO: Update existingBrand fields from brand
                    return brandRepository.save(existingBrand);
                });
    }

    @Override
    public boolean deleteBrand(String id) {
        // Implement delete logic here
        return brandRepository.findById(id)
                .map(brand -> {
                    brandRepository.delete(brand);
                    return true;
                }).orElse(false);
    }
}