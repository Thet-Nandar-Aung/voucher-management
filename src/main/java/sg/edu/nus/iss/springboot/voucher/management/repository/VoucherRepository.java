package sg.edu.nus.iss.springboot.voucher.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, String>{
   
}
