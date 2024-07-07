package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.aplication.port.out.BrandRepository;
import ai.smartassets.challenge.aplication.port.out.BrandRepositoryPort;
import ai.smartassets.challenge.domain.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BrandRepositoryAdapter implements BrandRepositoryPort {

    private final BrandRepository brandRepository;

    @Autowired
    public BrandRepositoryAdapter(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Page<Brand> findAll(Pageable pageable) {
        return brandRepository.findAll(pageable);
    }

    @Override
    public Optional<Brand> findById(String id) {
        return brandRepository.findById(id);
    }

    @Override
    public void delete(Brand brand) {
        brandRepository.delete(brand);
    }
}