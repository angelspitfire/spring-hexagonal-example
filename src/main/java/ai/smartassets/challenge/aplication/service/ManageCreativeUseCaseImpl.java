package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.port.in.ManageCreativeUseCase;
import ai.smartassets.challenge.aplication.port.out.CreativeRepository;
import ai.smartassets.challenge.domain.Creative;
import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
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
        CreativeEntity entity = getEntity(creative);
        return getCreative(creativeRepository.save(entity));
    }

    private static CreativeEntity getEntity(Creative creative) {
        return new CreativeEntity(creative.getCreativeId(), creative.getName(), creative.getDescription(), creative.getCreativeUrl(), creative.getCampaignId());
    }

    @Override
    public List<Creative> listCreatives(PageRequest pageRequest) {
        return creativeRepository.findAll(pageRequest).stream().map(ManageCreativeUseCaseImpl::getCreative).toList();
    }

    private static Creative getCreative(CreativeEntity creativeEntity) {
        return new Creative(creativeEntity.getId(), creativeEntity.getName(), creativeEntity.getDescription(), creativeEntity.getCreativeUrl(), creativeEntity.getCampaignId());
    }

    @Override
    public Optional<Creative> getCreativeById(String creativeId) {
        return creativeRepository.findById(creativeId).map(ManageCreativeUseCaseImpl::getCreative);
    }

    @Override
    public Optional<Creative> updateCreative(String creativeId, Creative creative) {
        return creativeRepository.findById(creativeId).map(creativeEntity -> {
            creativeEntity.setName(creative.getName());
            creativeEntity.setDescription(creative.getDescription());
            creativeEntity.setCreativeUrl(creative.getCreativeUrl());
            return getCreative(creativeRepository.save(creativeEntity));
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
