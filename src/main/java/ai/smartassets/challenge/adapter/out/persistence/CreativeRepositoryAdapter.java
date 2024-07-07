package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.aplication.port.out.CreativeRepository;
import ai.smartassets.challenge.aplication.port.out.CreativeRepositoryPort;
import ai.smartassets.challenge.domain.Creative;
import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class CreativeRepositoryAdapter implements CreativeRepositoryPort {

    private final CreativeRepository creativeRepository;

    @Autowired
    public CreativeRepositoryAdapter(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public Page<Creative> findAll(Pageable pageable) {
        return creativeRepository.findAll(pageable).map(CreativeRepositoryAdapter::getCreative);
    }

    @Override
    public Creative save(Creative creative) {
        CreativeEntity entity = getEntity(creative);
        return getCreative(creativeRepository.save(entity));
    }

    @Override
    public Optional<Creative> findById(String id) {
        return creativeRepository.findById(id).map(CreativeRepositoryAdapter::getCreative);
    }

    @Override
    public void delete(Creative creative) {
        creativeRepository.delete(getEntity(creative));
    }

    @Override
    public void deleteById(String id) {
        creativeRepository.deleteById(id);
    }

    private static CreativeEntity getEntity(Creative creative) {
        return new CreativeEntity(creative.getCreativeId(), creative.getName(), creative.getDescription(), creative.getCreativeUrl());
    }

    private static Creative getCreative(CreativeEntity creativeEntity) {
        return new Creative(creativeEntity.getId(), creativeEntity.getName(), creativeEntity.getDescription(), creativeEntity.getCreativeUrl());
    }
}
