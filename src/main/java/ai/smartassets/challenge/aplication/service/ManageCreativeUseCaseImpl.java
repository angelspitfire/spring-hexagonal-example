package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.port.in.ManageCreativeUseCase;
import ai.smartassets.challenge.aplication.port.out.CreativeRepository;
import ai.smartassets.challenge.domain.Creative;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ManageCreativeUseCaseImpl implements ManageCreativeUseCase {

    private final CreativeRepository creativeRepository;

    @Autowired
    public ManageCreativeUseCaseImpl(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public Creative createCreative(Creative creative) {
        return creativeRepository.save(creative);
    }

    @Override
    public Page<Creative> listCreatives(PageRequest of) {
        return creativeRepository.findAll(of);
    }

    @Override
    public Optional<Creative> getCreativeById(String creativeId) {
        return creativeRepository.findById(creativeId);
    }

    @Override
    public Optional<Creative> updateCreative(String creativeId, Creative creative) {
        return creativeRepository.findById(creativeId)
                .map(existingCreative -> {
                    existingCreative.setName(creative.getName());
                    existingCreative.setDescription(creative.getDescription());
                    return creativeRepository.save(existingCreative);
                });
    }

    @Override
    public boolean deleteCreative(String creativeId) {
        return creativeRepository.findById(creativeId)
                .map(creative -> {
                    creativeRepository.delete(creative);
                    return true;
                }).orElse(false);
    }
}
