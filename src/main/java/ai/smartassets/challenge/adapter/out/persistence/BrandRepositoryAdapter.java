package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.aplication.port.out.BrandRepository;
import ai.smartassets.challenge.aplication.port.out.BrandRepositoryPort;
import ai.smartassets.challenge.infraestructure.persistence.model.BrandEntity;
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
    public BrandEntity save(BrandEntity brandEntity) {
        return brandRepository.save(brandEntity);
    }

    @Override
    public Page<BrandEntity> findAll(Pageable pageable) {
        return brandRepository.findAll(pageable);
    }

    @Override
    public void delete(BrandEntity brandEntity) {
        brandRepository.delete(brandEntity);
    }

    @Override
    public Optional<BrandEntity> findById(String id) {
        return brandRepository.findById(id);
    }

    @Override
    public boolean existsById(String id) {
        return brandRepository.existsById(id);
    }

    @Override
    public void deleteById(String id) {
        brandRepository.deleteById(id);
    }
}