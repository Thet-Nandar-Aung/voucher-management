package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.VoucherDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.enums.VoucherStatus;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.FeedRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.VoucherRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.IVoucherService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;

@Service
public class VoucherService implements IVoucherService {

	private static final Logger logger = LoggerFactory.getLogger(VoucherService.class);

	@Autowired
	private VoucherRepository voucherRepository;

	@Autowired
	private CampaignRepository campaignRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FeedRepository feedRepository;

	@Override
	public Map<Long, List<VoucherDTO>> findAllClaimedVouchersByEmail(String email, Pageable pageable) {
		logger.info("Getting all claimed voucher for email {}...", email);
		Map<Long, List<VoucherDTO>> result = new HashMap<>();

		Page<Voucher> voucherPages = voucherRepository.findAllClaimedVouchersByEmail(email, pageable);
		long totalRecord = voucherPages.getTotalElements();
		List<VoucherDTO> voucherDTOList = new ArrayList<VoucherDTO>();
		if (totalRecord > 0) {
			for (Voucher voucher : voucherPages) {
				VoucherDTO voucherDTO = DTOMapper.toVoucherDTO(voucher);
				List<Voucher> voucherList= voucherRepository.findByCampaignCampaignId(voucherDTO.getCampaign().getCampaignId());
				voucherDTO.getCampaign().setNumberOfClaimedVouchers(voucherList.size());
				voucherDTOList.add(voucherDTO);
			}
		}
		result.put(totalRecord, voucherDTOList);
		return result;
	}

	@Override
	public Map<Long, List<VoucherDTO>> findAllClaimedVouchersByCampaignId(String campaignId, Pageable pageable) {
		logger.info("Getting all claimed voucher for campaign id {}...", campaignId);
		Map<Long, List<VoucherDTO>> result = new HashMap<>();

		Page<Voucher> voucherPages = voucherRepository.findByCampaignCampaignId(campaignId, pageable);
		long totalRecord = voucherPages.getTotalElements();
		List<VoucherDTO> voucherDTOList = new ArrayList<VoucherDTO>();
		if (totalRecord > 0) {

			for (Voucher voucher : voucherPages) {
				VoucherDTO voucherDTO = DTOMapper.toVoucherDTO(voucher);
				voucherDTO.getCampaign().setNumberOfClaimedVouchers((int)totalRecord);
				voucherDTOList.add(voucherDTO);
			}
		}
		result.put(totalRecord, voucherDTOList);
		return result;
	}

	@Override
	public VoucherDTO claim(Voucher voucher) {
		try {
			User user = userRepository.findByEmail(voucher.getClaimedBy().getEmail());
			Campaign campaign = campaignRepository.findById(voucher.getCampaign().getCampaignId()).orElseThrow();
			voucher.setVoucherStatus(VoucherStatus.CLAIMED);
			voucher.setClaimedBy(user);
			voucher.setClaimTime(LocalDateTime.now());
			voucher.setCampaign(campaign);
			logger.info("Saving voucher...");
			Voucher savedVoucher = voucherRepository.save(voucher);
			logger.info("Saved successfully...");
			
			VoucherDTO voucherDTO = DTOMapper.toVoucherDTO(savedVoucher);
			List<Voucher> voucherList= voucherRepository.findByCampaignCampaignId(voucherDTO.getCampaign().getCampaignId());
			voucherDTO.getCampaign().setNumberOfClaimedVouchers(voucherList.size());

			return voucherDTO;
		} catch (Exception ex) {
			logger.error("Voucher saving exception... {}", ex.toString());
			return null;
		}
	}

	@Override
	public VoucherDTO consume(Voucher voucher) {
		try {
			// Add validation here to make sure the same userId is passed

			Voucher dbVoucher = voucherRepository.findById(voucher.getVoucherId()).orElseThrow();
			if (dbVoucher == null) {
				logger.info("Voucher Id {} is not found.", voucher.getVoucherId());
				return null;
			}
			dbVoucher.setConsumedTime(LocalDateTime.now());
			dbVoucher.setVoucherStatus(VoucherStatus.CONSUMED);
			logger.info("Consuming voucher...");
			Voucher savedVoucher = voucherRepository.save(dbVoucher);
			logger.info("Consumed successfully...");
			
			VoucherDTO voucherDTO =  DTOMapper.toVoucherDTO(savedVoucher);
			List<Voucher> voucherList= voucherRepository.findByCampaignCampaignId(voucherDTO.getCampaign().getCampaignId());
			voucherDTO.getCampaign().setNumberOfClaimedVouchers(voucherList.size());

			return voucherDTO;
		} catch (Exception ex) {
			logger.error("Voucher consuming exception... {}", ex.toString());
			return null;
		}
	}

	@Override
	public VoucherDTO findByVoucherId(String voucherId) {
		logger.info("Getting voucher for voucher id {}...", voucherId);
		Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
		if (voucher != null) {
			logger.info("Voucher found...");
			VoucherDTO voucherDTO=DTOMapper.toVoucherDTO(voucher);
			List<Voucher> voucherList= voucherRepository.findByCampaignCampaignId(voucherDTO.getCampaign().getCampaignId());
			voucherDTO.getCampaign().setNumberOfClaimedVouchers(voucherList.size());
			
			return voucherDTO;
		}
		logger.warn("Didn't find any voucher for voucherId {}...", voucherId);
		return null;
	}

	@Override
	public List<VoucherDTO> findByCampaignIdAndClaimedBy(Campaign campaign, User claimedBy) {
		logger.info("Getting voucher for CampaignId", campaign.getCampaignId() ," Claimedby{}...", claimedBy.getEmail() );
		List<VoucherDTO> voucherDTOList = new ArrayList<VoucherDTO>();
		User user = userRepository.findByEmail(claimedBy.getEmail());
		List<Voucher> voucherList = voucherRepository.findByCampaignAndClaimedBy(campaign, user);
		if (voucherList.size() > 0) {
			for (Voucher voucher : voucherList) {
				voucherDTOList.add(DTOMapper.toVoucherDTO(voucher));
			}

		}else {
			logger.warn("Didn't find any voucher CampaignId", campaign.getCampaignId() ," Claimedby{}...", claimedBy.getEmail() );
		}
		return voucherDTOList;
	}

	@Override
	public List<Voucher> findByCampaignCampaignId(String campaignId) {
		// TODO Auto-generated method stub
		return voucherRepository.findByCampaignCampaignId(campaignId);
	}

}
