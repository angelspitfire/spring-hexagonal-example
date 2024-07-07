package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.aplication.port.out.CreativeRepository;
import ai.smartassets.challenge.aplication.port.out.CreativeRepositoryPort;
import ai.smartassets.challenge.domain.Creative;
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
        return creativeRepository.findAll(pageable);
    }

    @Override
    public Creative save(Creative creative) {
        return creativeRepository.save(creative);
    }

    @Override
    public Optional<Creative> findById(String id) {
        return creativeRepository.findById(id);
    }

    @Override
    public void delete(Creative creative) {
        creativeRepository.delete(creative);
    }

    @Override
    public void deleteById(String id) {
        creativeRepository.deleteById(id);
    }
}
