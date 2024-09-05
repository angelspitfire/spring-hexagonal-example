package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.dto.BrandResponse;
import ai.smartassets.challenge.aplication.dto.BrandCreationRequest;
import ai.smartassets.challenge.aplication.port.in.ManageBrandUseCase;
import ai.smartassets.challenge.aplication.port.out.BrandRepositoryPort;
import ai.smartassets.challenge.domain.Brand;
import ai.smartassets.challenge.infraestructure.persistence.model.BrandEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service @Slf4j
public class ManageBrandUseCaseImpl implements ManageBrandUseCase {

    private final BrandRepositoryPort brandRepository;

    @Autowired
    public ManageBrandUseCaseImpl(BrandRepositoryPort brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public BrandResponse createBrand(BrandCreationRequest brand) {
        BrandEntity brandEntity = new BrandEntity(null, brand.name(), brand.description());
        BrandEntity entity = brandRepository.save(brandEntity);
        return new BrandResponse(entity.getId(), entity.getName(), entity.getDescription());
    }

    @Override
    public List<BrandResponse> listBrands(Pageable pageable) {
        return brandRepository.findAll(pageable)
                .stream().map(brand -> new BrandResponse(brand.getId(), brand.getName(), brand.getDescription()))
                .toList();
    }

    @Override
    public Optional<BrandResponse> getBrandById(String id) {
        return brandRepository.findById(id)
                .map(brand -> new BrandResponse(brand.getId(), brand.getName(), brand.getDescription()));
    }

    @Override
    public Optional<BrandResponse> updateBrand(String id, Brand brand) {
        return brandRepository.findById(id).map(brandEntity -> {
            brandEntity.setName(brand.getName());
            brandEntity.setDescription(brand.getDescription());
            BrandEntity entity = brandRepository.save(brandEntity);
            return Optional.of(new BrandResponse(entity.getId(), entity.getName(), entity.getDescription()));
        }).orElse(Optional.empty());
    }

    @Override
    public boolean deleteBrand(String id) {
        log.info("Attempting to delete brand with id: {}", id);
        return brandRepository.findById(id).map(brandEntity -> {
            brandRepository.deleteById(id);
            log.info("Brand with id {} deleted.", id);
            return true;
        }).orElseGet(() -> {
            log.warn("Brand with id {} does not exist, cannot delete.", id);
            return false;
        });
    }
}