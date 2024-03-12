package sg.edu.nus.iss.springboot.voucher.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, String> {

    @Query("SELECT v.* FROM Voucher v JOIN User u on u.userId = v.claimedBy WHERE u.email = ?1")
    List<Voucher> findAllClaimedVouchersByEmail(String email);

    @Query("SELECT v.* FROM Voucher v WHERE v.campaignId = ?1")
    List<Voucher> findAllClaimedVouchersByCampaignId(String campaignId);

}
