package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class CampaignService implements ICampaignService {

    private static final Logger logger = LoggerFactory.getLogger(CampaignService.class);

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Override
    public List<CampaignDTO> findAllCampaigns() {

        logger.info("Getting all campaigns...");
        List<Campaign> campaigns = campaignRepository.findAll();
        logger.info("Found {}, converting to DTOs...", campaigns.size());
        List<CampaignDTO> campaignDTOs = new ArrayList<CampaignDTO>();
        for (Campaign campaign : campaigns) {
            campaignDTOs.add(DTOMapper.toCampaignDTO(campaign));
        }
        return campaignDTOs;
    }

    @Override
    public CampaignDTO findByCampaignId(String campaignId) {
        logger.info("Getting campaign for campaignId {} ...", campaignId);
        Campaign campaign = campaignRepository.findById(campaignId).orElse(null);
        if (campaign != null) {
            logger.info("Campaign found...");
            return DTOMapper.toCampaignDTO(campaign);
        }
        logger.warn("Didn't find any campaign for campaignId {}...", campaignId);
        return null;
    }

    @Override
    public CampaignDTO create(Campaign campaign) {
        try {
            User user = userRepository.findByEmail(campaign.getCreatedBy().getEmail());
            Store store = storeRepository.findById(campaign.getStore().getStoreId()).orElseThrow();
            campaign.setCreatedBy(user);
            campaign.setCreatedDate(LocalDateTime.now());
            campaign.setUpdatedBy(user);
            campaign.setUpdatedDate(LocalDateTime.now());
            campaign.setStore(store);
            logger.info("Saving campaign...");
            Campaign savedCampaign = campaignRepository.save(campaign);
            logger.info("Saved successfully...");
            return DTOMapper.toCampaignDTO(savedCampaign);
        } catch (Exception ex) {
            logger.error("Campaign saving exception... {}", ex.toString());
            return null;
        }

    }

    @Override
    public CampaignDTO update(Campaign campaign) {
        try {
            User user = userRepository.findByEmail(campaign.getCreatedBy().getEmail());
            campaign.setUpdatedBy(user);
            campaign.setUpdatedDate(LocalDateTime.now());
            logger.info("Saving campaign...");
            Campaign savedCampaign = campaignRepository.save(campaign);
            logger.info("Saved successfully...");
            CampaignDTO campaignDTO = DTOMapper.toCampaignDTO(savedCampaign);
            return campaignDTO;
        } catch (Exception ex) {
            logger.error("Campaign updating exception... {}", ex.toString());
            return null;
        }

    }

    @Override
    public void delete(String campaignId) {
        if (campaignId == null) {
            logger.info("CampaignId is null...");
            return;
        }
        try {
            logger.info("Deleting campaignId {}...", campaignId);
            campaignRepository.findById(campaignId).ifPresent(campaign -> campaignRepository.delete(campaign));
            logger.info("Deleted successfully...");
        } catch (Exception ex) {
            logger.error("Campaign deleting exception... {}", ex.toString());
        }
    }
}
