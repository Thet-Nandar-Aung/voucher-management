package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;

public interface IVoucherService {

    List<Voucher> findAllVouchers();

    Optional<Voucher> findByVoucherId(String voucherId);
    
    Voucher save(Voucher voucher);

    void delete(String voucherId);

}
