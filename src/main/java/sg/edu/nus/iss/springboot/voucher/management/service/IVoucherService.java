package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import sg.edu.nus.iss.springboot.voucher.management.dto.VoucherDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;

public interface IVoucherService {

	Map<Long, List<VoucherDTO>> findAllClaimedVouchersByEmail(String email,Pageable pageable);

	Map<Long, List<VoucherDTO>> findAllClaimedVouchersByCampaignId(String campaignId,Pageable pageable);

    VoucherDTO findByVoucherId(String voucherId);

    VoucherDTO claim(Voucher voucher);

    VoucherDTO consume(Voucher voucher);
    
    List<VoucherDTO> findByCampaignIdAndClaimedBy(Campaign campaignId, User claimedBy); 
    
    List<Voucher> findByCampaignCampaignId(String campaignId);
}
