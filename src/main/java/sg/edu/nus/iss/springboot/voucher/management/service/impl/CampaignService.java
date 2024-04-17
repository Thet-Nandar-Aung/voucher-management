package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.StoreDTO;
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
	public Map<Long, List<CampaignDTO>> findAllActiveCampaigns(Pageable pageable) {
		logger.info("Getting all active campaigns...");
		Map<Long, List<CampaignDTO>> result = new HashMap<>();
		List<CampaignDTO> campaignDTOList = new ArrayList<>();

		Page<Campaign> campaignPages = campaignRepository.findByCampaignStatusIn(Arrays.asList(CampaignStatus.PROMOTED),
				pageable);
		long totalRecord = campaignPages.getTotalElements();

		if (totalRecord > 0) {
			for (Campaign campaign : campaignPages.getContent()) {
				campaign.setVoucher(voucherRepository.findByCampaignCampaignId(campaign.getCampaignId()));
				CampaignDTO campaignDTO = DTOMapper.toCampaignDTO(campaign);
				campaignDTOList.add(campaignDTO);
			}

		} else {
			logger.info("Campaign not found...");
		}

		result.put(totalRecord, campaignDTOList);

		return result;
	}

	@Override
	public Map<Long, List<CampaignDTO>> findAllCampaignsByStoreId(String storeId, Pageable pageable) {
		logger.info("Getting all campaigns by Store Id...");
		Map<Long, List<CampaignDTO>> result = new HashMap<>();
		Page<Campaign> campaignPages = campaignRepository.findByStoreStoreId(storeId, pageable);
		long totalRecord = campaignPages.getTotalElements();
		List<CampaignDTO> campaignDTOList = new ArrayList<>();

		if (totalRecord > 0) {
			for (Campaign campaign : campaignPages) {
				campaign.setVoucher(voucherRepository.findByCampaignCampaignId(campaign.getCampaignId()));
				campaignDTOList.add(DTOMapper.toCampaignDTO(campaign));
			}
		} else {
			logger.info("Campaign not found...");
		}

		result.put(totalRecord, campaignDTOList);
		return result;
	}

	@Override
	public Map<Long, List<CampaignDTO>> findByStoreIdAndStatus(String storeId, CampaignStatus campaignStatus,
			Pageable pageable) {
		logger.info("Getting all campaigns by Store Id and Status...");

		Map<Long, List<CampaignDTO>> result = new HashMap<>();

		Page<Campaign> campaignPages = campaignRepository.findByStoreStoreIdAndCampaignStatus(storeId, campaignStatus,
				pageable);

		long totalRecord = campaignPages.getTotalElements();
		List<CampaignDTO> campaignDTOList = new ArrayList<>();
		if (totalRecord > 0) {
			for (Campaign campaign : campaignPages) {
				campaign.setVoucher(voucherRepository.findByCampaignCampaignId(campaign.getCampaignId()));
				campaignDTOList.add(DTOMapper.toCampaignDTO(campaign));
			}
		} else {
			logger.info("Campaign not found...");
		}
		result.put(totalRecord, campaignDTOList);
		return result;
	}

	@Override
	public Map<Long, List<CampaignDTO>> findAllCampaignsByEmail(String email, Pageable pageable) {
		logger.info("Getting all campaigns by email...");
		Map<Long, List<CampaignDTO>> result = new HashMap<>();

		Page<Campaign> campaignPages = campaignRepository.findByCreatedByEmail(email, pageable);
		long totalRecord = campaignPages.getTotalElements();
		List<CampaignDTO> campaignDTOList = new ArrayList<>();
		if (totalRecord > 0) {
			for (Campaign campaign : campaignPages) {
				campaign.setVoucher(voucherRepository.findByCampaignCampaignId(campaign.getCampaignId()));
				campaignDTOList.add(DTOMapper.toCampaignDTO(campaign));
			}
		}
		result.put(totalRecord, campaignDTOList);
		return result;
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
			logger.info("Update campaign...");
			Campaign savedCampaign = campaignRepository.save(dbCampaign.get());
			logger.info("Updated successfully...");
			campaignDTO = DTOMapper.toCampaignDTO(savedCampaign);

		} catch (Exception ex) {
			logger.error("Campaign updating exception... {}", ex.toString());

		}

		return campaignDTO;

	}

	/*
	 * @Override public boolean delete(Campaign campaign) { boolean isDeleted =
	 * false;
	 * 
	 * try { logger.info("Deleting campaignId {}...", campaign.getCampaignId());
	 * 
	 * User user = userRepository.findByEmail(campaign.getUpdatedBy().getEmail());
	 * campaign.setUpdatedBy(user); campaign.setUpdatedDate(LocalDateTime.now());
	 * campaign.setDeleted(true);
	 * 
	 * logger.info("Saving campaign..."); Campaign savedCampaign =
	 * campaignRepository.save(campaign); logger.info("Saved successfully..."); if
	 * (savedCampaign.isDeleted()) { isDeleted = true; }
	 * 
	 * logger.info("Deleted successfully..."); } catch (Exception ex) {
	 * logger.error("Campaign deleting exception... {}", ex.toString()); }
	 * 
	 * return isDeleted; }
	 */

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
						notifyObservers(dbCampaign.orElse(campaign)); // notify observer
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

	@Override
	public List<Campaign> expired() {
		List<Campaign> expiredList = new ArrayList<Campaign>();
		try {
			List<Campaign> dbCampaigns = campaignRepository.findByEndDateBefore(LocalDateTime.now());
			  if (!dbCampaigns.isEmpty()) {
				for (Campaign campaing : dbCampaigns) {
					campaing.setCampaignStatus(CampaignStatus.EXPIRED);
					campaing.setUpdatedDate(LocalDateTime.now());
					logger.info("Update campaign...");
					Campaign savedCampaign = campaignRepository.save(campaing);
					logger.info("Saved successfully...");
					expiredList.add(savedCampaign);
				}
			}

		} catch (Exception ex) {
			logger.error("Campaign expiring exception... {}", ex.toString());

		}
		return expiredList;

	}

}
