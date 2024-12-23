package com.hexagonal.challenge.adapter.out.persistence;

import com.hexagonal.challenge.aplication.port.out.CreativeRepository;
import com.hexagonal.challenge.aplication.port.out.CreativeRepositoryPort;
import com.hexagonal.challenge.infraestructure.persistence.model.CreativeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CreativeRepositoryAdapter implements CreativeRepositoryPort {

    private final CreativeRepository creativeRepository;

    public CreativeRepositoryAdapter(CreativeRepository creativeRepository) {
        this.creativeRepository = creativeRepository;
    }

    @Override
    public Page<CreativeEntity> findAll(Pageable pageable) {
        return creativeRepository.findAll(pageable);
    }

    @Override
    public CreativeEntity save(CreativeEntity creative) {
        return creativeRepository.save(creative);
    }

    @Override
    public Optional<CreativeEntity> findById(String id) {
        return creativeRepository.findById(id);
    }

    @Override
    public void delete(CreativeEntity creative) {
        creativeRepository.delete(creative);
    }

    @Override
    public List<CreativeEntity> findByCampaignId(String campaignId, PageRequest pageRequest) {
        return creativeRepository.findByCampaignId(campaignId);
    }

    @Override
    public boolean existsById(String creativeId) {
       return creativeRepository.existsById(creativeId);
    }
}
