package ai.smartassets.challenge.aplication.service;

import ai.smartassets.challenge.aplication.dto.CreativeResponse;
import ai.smartassets.challenge.aplication.dto.CreativeUpdateRequest;
import ai.smartassets.challenge.aplication.port.in.ManageCreativeUseCase;
import ai.smartassets.challenge.aplication.port.out.CreativeRepositoryPort;
import ai.smartassets.challenge.domain.Creative;
import ai.smartassets.challenge.infraestructure.persistence.model.CreativeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManageCreativeUseCaseImpl implements ManageCreativeUseCase {

    private final CreativeRepositoryPort creativeRepository;

    @Autowired
    public ManageCreativeUseCaseImpl(CreativeRepositoryPort creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public CreativeResponse createCreative(Creative creative) {
        CreativeEntity entity = getEntity(creative);
        return getCreative(creativeRepository.save(entity));
    }

    @Override
    public List<CreativeResponse> listCreatives(PageRequest pageRequest) {
        return creativeRepository.findAll(pageRequest).stream().map(ManageCreativeUseCaseImpl::getCreative).toList();
    }

    @Override
    public Optional<CreativeResponse> getCreativeById(String creativeId) {
        return creativeRepository.findById(creativeId).map(ManageCreativeUseCaseImpl::getCreative);
    }

    @Override
    public Optional<CreativeResponse> updateCreative(String creativeId, CreativeUpdateRequest creative) {
        return creativeRepository.findById(creativeId).map(creativeEntity -> {
            creativeEntity.setName(creative.name());
            creativeEntity.setDescription(creative.description());
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

    private static CreativeEntity getEntity(Creative creative) {
        return new CreativeEntity(creative.getCreativeId(), creative.getName(), creative.getDescription(), creative.getCreativeUrl(), creative.getCampaignId());
    }

    private static CreativeResponse getCreative(CreativeEntity creativeEntity) {
        return new CreativeResponse(creativeEntity.getId(), creativeEntity.getName(), creativeEntity.getDescription(), creativeEntity.getCreativeUrl(), creativeEntity.getCampaignId());
    }
}
