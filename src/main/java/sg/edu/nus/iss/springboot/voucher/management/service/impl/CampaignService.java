package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.ICampaignService;

@Service
public class CampaignService implements ICampaignService        {

    private static final Logger logger = LoggerFactory.getLogger(CampaignService.class);

    @Autowired
    private CampaignRepository campaignRepository;

    @Override
    public List<Campaign> findAllCampaigns() {
    	logger.info("Calling " + Thread.currentThread().getName());
        return campaignRepository.findAll();
    }

    @Override
    public Optional<Campaign> findByCampaignId(String campaignId) {
    	logger.info("Calling " + Thread.currentThread().getName());
       return campaignRepository.findById(campaignId);
    }

    @Override
    public Campaign save(Campaign campaign) {
    	logger.info("Calling " + Thread.currentThread().getName());
        campaign.setUpdatedDate(LocalDateTime.now());
        return campaignRepository.save(campaign);
    }

	@Override
	public void delete(String campaignId) {
    	logger.info("Calling " + Thread.currentThread().getName());
		campaignRepository.deleteById(campaignId);;
	}

}
