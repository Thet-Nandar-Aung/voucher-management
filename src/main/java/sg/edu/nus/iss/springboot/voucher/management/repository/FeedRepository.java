package sg.edu.nus.iss.springboot.voucher.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Feed;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;

@Repository
public interface FeedRepository extends JpaRepository<Feed, String>{
    Feed save(Feed feed);
    
    @Query("SELECT s FROM Feed s WHERE s.targetUserId = ?1 AND s.campaignId=?2 AND s.isDeleted = ?3 AND s.isRead =?4")
    List<Feed>  findByTargetedUserAndStatus(User targetedUserId,Campaign campaignId, boolean isDeleted , boolean isRead);

}
