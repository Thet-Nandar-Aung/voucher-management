package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.observer.ICampaignSubject;
import sg.edu.nus.iss.springboot.voucher.management.observer.impl.FeedObserver;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.StoreRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.VoucherRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.ICampaignService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;

@Service
public class CampaignService implements ICampaignService, ICampaignSubject {

	private static final Logger logger = LoggerFactory.getLogger(CampaignService.class);
	private List<FeedObserver> feedObservers = new ArrayList<FeedObserver>();

	@Autowired
	private CampaignRepository campaignRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private VoucherRepository voucherRepository;

	@Autowired
	private FeedObserver feedObserver;

	@Override
	public List<CampaignDTO> findAllActiveCampaigns() {
		logger.info("Getting all active campaigns...");
		List<Campaign> campaigns = campaignRepository.findByCampaignStatusIn(Arrays.asList(CampaignStatus.PROMOTED));
		logger.info("Found {}, converting to DTOs...", campaigns.size());
		List<CampaignDTO> campaignDTOs = new ArrayList<CampaignDTO>();
		for (Campaign campaign : campaigns) {
			campaign.setVoucher(voucherRepository.findByCampaignCampaignId(campaign.getCampaignId()));
			campaignDTOs.add(DTOMapper.toCampaignDTO(campaign));
		}
		return campaignDTOs;
	}

	@Override
	public List<CampaignDTO> findAllCampaignsByStoreId(String storeId) {
		logger.info("Getting all campaigns by Store Id...");
		List<Campaign> campaigns = campaignRepository.findByStoreStoreId(storeId);
		logger.info("Found {}, converting to DTOs...", campaigns.size());
		List<CampaignDTO> campaignDTOs = new ArrayList<CampaignDTO>();
		for (Campaign campaign : campaigns) {
			campaign.setVoucher(voucherRepository.findByCampaignCampaignId(campaign.getCampaignId()));
			campaignDTOs.add(DTOMapper.toCampaignDTO(campaign));
		}
		return campaignDTOs;
	}

	@Override
	public List<CampaignDTO> findAllCampaignsByEmail(String email) {
		logger.info("Getting all campaigns by email...");
		List<Campaign> campaigns = campaignRepository.findByCreatedByEmail(email);
		logger.info("Found {}, converting to DTOs...", campaigns.size());
		List<CampaignDTO> campaignDTOs = new ArrayList<CampaignDTO>();
		for (Campaign campaign : campaigns) {
			campaign.setVoucher(voucherRepository.findByCampaignCampaignId(campaign.getCampaignId()));
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
			campaign.setVoucher(voucherRepository.findByCampaignCampaignId(campaignId));
			return DTOMapper.toCampaignDTO(campaign);
		}
		logger.warn("Didn't find any campaign for campaignId {}...", campaignId);
		return null;
	}

	@Override
	public CampaignDTO create(Campaign campaign) {
		CampaignDTO campaignDTO = new CampaignDTO();
		try {

			User user = userRepository.findByEmail(campaign.getCreatedBy().getEmail());
			Store store = storeRepository.findById(campaign.getStore().getStoreId()).orElseThrow();
			campaign.setPin(String.valueOf(new Random().nextInt(9000) + 1000));
			campaign.setCreatedBy(user);
			campaign.setCreatedDate(LocalDateTime.now());
			campaign.setStore(store);
			logger.info("Saving campaign...");
			Campaign savedCampaign = campaignRepository.save(campaign);
			logger.info("Saved successfully...");
			campaignDTO = DTOMapper.toCampaignDTO(savedCampaign);

		} catch (Exception ex) {
			logger.error("Campaign saving exception... {}", ex.toString());

		}
		return campaignDTO;
	}

	@Override
	public CampaignDTO update(Campaign campaign) {
		CampaignDTO campaignDTO = new CampaignDTO();
		try {
			Optional<Campaign> dbCampaign = campaignRepository.findById(campaign.getCampaignId());
			User user = userRepository.findByEmail(campaign.getUpdatedBy().getEmail());
			dbCampaign.get().setDescription(GeneralUtility.makeNotNull(campaign.getDescription()));
			dbCampaign.get().setAmount(campaign.getAmount());
			dbCampaign.get().setStartDate(campaign.getStartDate());
			dbCampaign.get().setEndDate(campaign.getEndDate());
			dbCampaign.get().setNumberOfLikes(campaign.getNumberOfLikes());
			dbCampaign.get().setNumberOfVouchers(campaign.getNumberOfVouchers());
			dbCampaign.get().setTagsJson(GeneralUtility.makeNotNull(campaign.getTagsJson()));
			dbCampaign.get().setTandc(GeneralUtility.makeNotNull(campaign.getTandc()));
			dbCampaign.get().setUpdatedBy(user);
			dbCampaign.get().setUpdatedDate(LocalDateTime.now());
			logger.info("Saving campaign...");
			Campaign savedCampaign = campaignRepository.save(dbCampaign.get());
			logger.info("Saved successfully...");
			campaignDTO = DTOMapper.toCampaignDTO(savedCampaign);

		} catch (Exception ex) {
			logger.error("Campaign updating exception... {}", ex.toString());

		}

		return campaignDTO;

	}

	@Override
	public boolean delete(Campaign campaign) {
		boolean isDeleted = false;

		try {
			logger.info("Deleting campaignId {}...", campaign.getCampaignId());

			User user = userRepository.findByEmail(campaign.getUpdatedBy().getEmail());
			campaign.setUpdatedBy(user);
			campaign.setUpdatedDate(LocalDateTime.now());
			campaign.setDeleted(true);

			logger.info("Saving campaign...");
			Campaign savedCampaign = campaignRepository.save(campaign);
			logger.info("Saved successfully...");
			if (savedCampaign.isDeleted()) {
				isDeleted = true;
			}

			logger.info("Deleted successfully...");
		} catch (Exception ex) {
			logger.error("Campaign deleting exception... {}", ex.toString());
		}

		return isDeleted;
	}

	@Override
	public CampaignDTO promote(Campaign campaign) {
		CampaignDTO campaignDTO = new CampaignDTO();
		try {

			Optional<Campaign> dbCampaign = campaignRepository.findById(campaign.getCampaignId());
			if (dbCampaign.isPresent()) {
				logger.info("Promoting campaign: status {}..", dbCampaign.get().getCampaignStatus());
				if (dbCampaign.get().getCampaignStatus().equals(CampaignStatus.CREATED)) {

					User user = userRepository.findByEmail(campaign.getUpdatedBy().getEmail());

					LocalDateTime startDate = dbCampaign.get().getStartDate();
					LocalDateTime endDate = dbCampaign.get().getEndDate();

					logger.info("Promoting campaign:startDate{} ,endDate{}...", startDate, endDate);

					if ((startDate.isAfter(LocalDateTime.now()) || startDate.equals(LocalDateTime.now()))
							&& endDate.isAfter(LocalDateTime.now())) {
						dbCampaign.get().setCampaignStatus(CampaignStatus.PROMOTED);
						dbCampaign.get().setUpdatedBy(user);
						dbCampaign.get().setUpdatedDate(LocalDateTime.now());
						Campaign promottedCampaign = campaignRepository.save(dbCampaign.get());
						logger.info("Promotted successfully...");
						campaignDTO = DTOMapper.toCampaignDTO(promottedCampaign);
						registerObserver(feedObserver); // attach observer
						notifyObservers(campaign); // notify observer
						logger.info("Feed generated successfully...");
					} else {
						logger.info(
								"Promoting campaign Failed: startDate{} should not be greater than current date and endDate{} should not be less than current date...",
								startDate, endDate);
					}
				}
			}

		} catch (Exception ex) {
			logger.error("Campaign Promoting exception... {}", ex.toString());

		}
		return campaignDTO;
	}

	@Override
	public List<Campaign> findByDescription(String description) {
		// TODO Auto-generated method stub
		return campaignRepository.findByDescription(description);
	}

	@Override
	public Optional<Campaign> findById(String campaignId) {
		// TODO Auto-generated method stub
		return campaignRepository.findById(campaignId);
	}

	@Override
	public List<CampaignDTO> findByStoreIdAndStatus(String storeId, CampaignStatus campaignStatus) {
		logger.info("Getting all campaigns by Store Id and Status...");
		List<Campaign> campaigns = campaignRepository.findByStoreStoreIdAndCampaignStatus(storeId, campaignStatus);
		logger.info("Found {}, converting to DTOs...", campaigns.size());
		List<CampaignDTO> campaignDTOs = new ArrayList<CampaignDTO>();
		for (Campaign campaign : campaigns) {
			campaign.setVoucher(voucherRepository.findByCampaignCampaignId(campaign.getCampaignId()));
			campaignDTOs.add(DTOMapper.toCampaignDTO(campaign));
		}
		return campaignDTOs;
	}

	@Override
	public void registerObserver(FeedObserver observer) {
		feedObservers.add(observer);
	}

	@Override
	public void removeObserver(FeedObserver observer) {
		feedObservers.remove(observer);
	}

	@Override
	public void notifyObservers(Campaign campaign) {
		for (FeedObserver feedObserver : feedObservers) {
			feedObserver.update(campaign);
		}
	}

}
