package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;

public interface ICampaignService {

	Map<Long, List<CampaignDTO>> findAllActiveCampaigns(Pageable pageable);

	Map<Long, List<CampaignDTO>> findAllCampaignsByStoreId(String storeId,Pageable pageable);

	Map<Long, List<CampaignDTO>> findAllCampaignsByEmail(String storeId,Pageable pageable);

    CampaignDTO findByCampaignId(String campaignId);
    
    CampaignDTO create(Campaign campaign);

    CampaignDTO update(Campaign campaign);
    
    CampaignDTO promote(Campaign campaign);
    
    List<Campaign> expired();
    
    List<Campaign>  findByDescription(String description);
    
    Optional<Campaign>  findById(String campaignId);
    
    Map<Long, List<CampaignDTO>>findByStoreIdAndStatus(String storeId,CampaignStatus status,Pageable pageable);

}
