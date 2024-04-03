package sg.edu.nus.iss.springboot.voucher.management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;

import java.util.List;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;


public interface CampaignRepository extends JpaRepository<Campaign, String> {

    
    @Query("SELECT c FROM Campaign c WHERE c.campaignStatus IN ?1")
    List<Campaign> findByCampaignStatusIn(List<CampaignStatus> statuses);

    Page<Campaign> findByStoreStoreId(String storeId,Pageable pageable);
    
    Page<Campaign> findByStoreStoreIdAndCampaignStatus(String storeId,CampaignStatus status,Pageable pageable);

    Page<Campaign> findByCreatedByEmail(String email,Pageable pageable);
    
    List<Campaign>  findByDescription(String description);
    
    @Query("SELECT c FROM Campaign c WHERE c.campaignStatus IN ?1")
    Page<Campaign> findByCampaignStatusIn(List<CampaignStatus> statuses,Pageable pageable);

}
