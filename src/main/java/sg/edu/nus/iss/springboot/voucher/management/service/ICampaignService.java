package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;

public interface ICampaignService {

    List<CampaignDTO> findAllActiveCampaigns();

    List<CampaignDTO> findAllCampaignsByStoreId(String storeId);

    List<CampaignDTO> findAllCampaignsByEmail(String storeId);

    CampaignDTO findByCampaignId(String campaignId);
    
    CampaignDTO create(Campaign campaign);

    CampaignDTO update(Campaign campaign);

    boolean delete(Campaign campaign);
    
    CampaignDTO promote(String campaign);
    
    List<Campaign>  findByDescription(String description);
    
    Optional<Campaign>  findById(String campaignId);

}
