package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.dto.VoucherDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;
import sg.edu.nus.iss.springboot.voucher.management.enums.VoucherStatus;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
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

    @Override
    public List<VoucherDTO> findAllClaimedVouchersByEmail(String email) {
        logger.info("Getting all claimed voucher for email {}...", email);
        List<Voucher> vouchers = voucherRepository.findAllClaimedVouchersByEmail(email);
        logger.info("Found {}, converting to DTOs...", vouchers.size());
        List<VoucherDTO> voucherDTOs = new ArrayList<VoucherDTO>();
        for (Voucher voucher : vouchers) {
            voucherDTOs.add(DTOMapper.toVoucherDTO(voucher));
        }
        return voucherDTOs;
    }

    @Override
    public List<VoucherDTO> findAllClaimedVouchersByCampaignId(String campaignId) {
        logger.info("Getting all claimed voucher for campaign id {}...", campaignId);
        List<Voucher> vouchers = voucherRepository.findByCampaignCampaignId(campaignId);
        logger.info("Found {}, converting to DTOs...", vouchers.size());
        List<VoucherDTO> voucherDTOs = new ArrayList<VoucherDTO>();
        for (Voucher voucher : vouchers) {
            voucherDTOs.add(DTOMapper.toVoucherDTO(voucher));
        }
        return voucherDTOs;
    }

    @Override
    public VoucherDTO claim(Voucher voucher) {
        try {
            User user = userRepository.findByEmail(voucher.getClaimedBy().getEmail());
            Campaign campaign = campaignRepository.findById(voucher.getCampaign().getCampaignId()).orElseThrow();
            voucher.setClaimedBy(user);
            voucher.setClaimTime(LocalDateTime.now());
            voucher.setCampaign(campaign);
            logger.info("Saving voucher...");
            Voucher savedVoucher = voucherRepository.save(voucher);
            logger.info("Saved successfully...");
            return DTOMapper.toVoucherDTO(savedVoucher);
        } catch (Exception ex) {
            logger.error("Voucher saving exception... {}", ex.toString());
            return null;
        }
    }

    @Override
    public VoucherDTO consume(Voucher voucher) {
        try {
            // Add validation here to make sure the same userId is passed
            voucher.setConsumedTime(LocalDateTime.now());
            voucher.setVoucherStatus(VoucherStatus.CONSUMED);
            logger.info("Consuming voucher...");
            Voucher savedVoucher = voucherRepository.save(voucher);
            logger.info("Consumed successfully...");
            return DTOMapper.toVoucherDTO(savedVoucher);
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
            return DTOMapper.toVoucherDTO(voucher);
        }
        logger.warn("Didn't find any voucher for voucherId {}...", voucherId);
        return null;
    }

}
