package sg.edu.nus.iss.springboot.voucher.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;

import java.util.List;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;


public interface CampaignRepository extends JpaRepository<Campaign, String> {

    
    @Query("SELECT c FROM Campaign c WHERE c.campaignStatus IN ?1")
    List<Campaign> findByCampaignStatusIn(List<CampaignStatus> statuses);

    List<Campaign> findByStoreStoreId(String storeId);

    List<Campaign> findByCreatedByEmail(String email);
    
    List<Campaign>  findByDescription(String description);

}
