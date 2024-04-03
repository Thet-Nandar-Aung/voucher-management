package sg.edu.nus.iss.springboot.voucher.management.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, String> {

    @Query("SELECT v FROM Voucher v JOIN v.claimedBy u WHERE u.email = ?1")
    Page<Voucher> findAllClaimedVouchersByEmail(String email,Pageable pageable);

    List<Voucher> findByCampaignCampaignId(String campaignId);
    
    Page<Voucher> findByCampaignCampaignId(String campaignId,Pageable pageable);
    
    @Query("SELECT v FROM Voucher v WHERE v.campaign= ?1 AND v.claimedBy = ?2")
    List<Voucher> findByCampaignAndClaimedBy(Campaign campaign,User claimedBy);

}