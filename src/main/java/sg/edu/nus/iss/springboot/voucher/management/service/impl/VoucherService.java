package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;
import sg.edu.nus.iss.springboot.voucher.management.repository.VoucherRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.IVoucherService;

@Service
public class VoucherService implements IVoucherService        {

    private static final Logger logger = LoggerFactory.getLogger(VoucherService.class);

    @Autowired
    private VoucherRepository VoucherRepository;

    @Override
    public List<Voucher> findAllVouchers() {
    	logger.info("Calling " + Thread.currentThread().getName());
        return VoucherRepository.findAll();
    }

    @Override
    public Optional<Voucher> findByVoucherId(String voucherId) {
    	logger.info("Calling " + Thread.currentThread().getName());
       return VoucherRepository.findById(voucherId);
    }

    @Override
    public Voucher save(Voucher voucher) {
    	logger.info("Calling " + Thread.currentThread().getName());
        voucher.setClaimTime(LocalDateTime.now());
        return VoucherRepository.save(voucher);
    }

	@Override
	public void delete(String voucherId) {
    	logger.info("Calling " + Thread.currentThread().getName());
		VoucherRepository.deleteById(voucherId);
	}

}
