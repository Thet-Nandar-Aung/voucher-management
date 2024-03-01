package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;

public interface ICampaignService {

    List<Campaign> findAllCampaigns();

    Optional<Campaign> findByCampaignId(String campaignId);
    
    Campaign create(Campaign campaign);

    Campaign update(Campaign campaign);

    void delete(String campaignId);

}
