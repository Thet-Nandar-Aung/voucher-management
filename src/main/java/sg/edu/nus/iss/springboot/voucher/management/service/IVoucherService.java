package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;

import sg.edu.nus.iss.springboot.voucher.management.dto.VoucherDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;

public interface IVoucherService {

    List<VoucherDTO> findAllClaimedVouchersByEmail(String email);

    List<VoucherDTO> findAllClaimedVouchersByCampaignId(String campaignId);

    VoucherDTO findByVoucherId(String voucherId);

    VoucherDTO claim(Voucher voucher);

    VoucherDTO consume(Voucher voucher);
}
