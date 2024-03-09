package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.StoreRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.ICampaignService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;

@Service
public class CampaignService implements ICampaignService        {

    private static final Logger logger = LoggerFactory.getLogger(CampaignService.class);

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public List<Campaign> findAllCampaigns() {
        return campaignRepository.findAll();
    }

    @Override
    public Optional<Campaign> findByCampaignId(String campaignId) {
       return campaignRepository.findById(campaignId);
    }

    @Override
    public CampaignDTO create(Campaign campaign) {
        User user = userRepository.findById(campaign.getCreatedBy().getUserId()).orElseThrow();
        Store store = storeRepository.findById(campaign.getStore().getStoreId()).orElseThrow();
        campaign.setCreatedBy(user);
        campaign.setCreatedDate(LocalDateTime.now());
        campaign.setUpdatedBy(user);
        campaign.setUpdatedDate(LocalDateTime.now());
        campaign.setStore(store);
        Campaign savedCampaign = campaignRepository.save(campaign);
        CampaignDTO campaignDTO = DTOMapper.toCampaignDTO(savedCampaign);
        return campaignDTO;
    }

    @Override
    public Campaign update(Campaign campaign) {
        campaign.setUpdatedDate(LocalDateTime.now());
        return campaignRepository.save(campaign);
    }

    @Override
	public void delete(String campaignId) {
        campaignRepository.findById(campaignId).ifPresent(campaign -> campaignRepository.delete(campaign));
	}

}
