package sg.edu.nus.iss.springboot.voucher.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;

import java.util.List;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;


public interface CampaignRepository extends JpaRepository<Campaign, String> {

    List<Campaign> findByCampaignStatus(CampaignStatus campaignStatus);

    List<Campaign> findByStoreStoreId(String storeId);

}
