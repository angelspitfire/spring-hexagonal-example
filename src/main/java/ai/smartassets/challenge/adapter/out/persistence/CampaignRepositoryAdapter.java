package ai.smartassets.challenge.adapter.out.persistence;

import ai.smartassets.challenge.aplication.port.out.CampaignRepository;
import ai.smartassets.challenge.aplication.port.out.CampaignRepositoryPort;
import ai.smartassets.challenge.infraestructure.persistence.model.CampaignEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CampaignRepositoryAdapter implements CampaignRepositoryPort {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignRepositoryAdapter(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    @Override
    public CampaignEntity save(CampaignEntity entity) {
        return campaignRepository.save(entity);
    }

    @Override
    public Page<CampaignEntity> findAll(PageRequest pageRequest) {
        return campaignRepository.findAll(pageRequest);
    }

    @Override
    public Optional<CampaignEntity> findById(String id) {
        return campaignRepository.findById(id);
    }

    @Override
    public void delete(CampaignEntity campaign) {
        campaignRepository.delete(campaign);
    }

    @Override
    public List<CampaignEntity> findByBrandId(String brandId, Pageable pageable) {
        return campaignRepository.findByBrandId(brandId, pageable);
    }

    @Override
    public List<CampaignEntity> findByBrandIdAndId(String brandId, String campaignId, PageRequest pageRequest) {
        return campaignRepository.findByBrandIdAndId(brandId, campaignId, pageRequest);
    }

    @Override
    public void deleteById(String id) {
        campaignRepository.deleteById(id);
    }

    @Override
    public boolean existsById(String campaignId) {
        return campaignRepository.existsById(campaignId);
    }
}
