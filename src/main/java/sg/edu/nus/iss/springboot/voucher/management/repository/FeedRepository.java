package sg.edu.nus.iss.springboot.voucher.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Feed;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;

@Repository
public interface FeedRepository extends JpaRepository<Feed, String> {
	Feed save(Feed feed);
	
	List<Feed>findByIsDeletedFalse();

	@Query("SELECT s FROM Feed s WHERE s.targetUserId = ?1 AND s.campaignId=?2 AND s.isDeleted = ?3 AND s.isRead =?4")
	List<Feed> findByTargetedUserAndStatus(User targetedUserId, Campaign campaignId, boolean isDeleted, boolean isRead);

	@Query("SELECT s FROM Feed s WHERE s.campaignId=?1 AND s.isDeleted = ?2 ")
	List<Feed> findAllFeedsByCampaignId(Campaign campaignId, boolean isDeleted);

	@Query("SELECT s FROM Feed s WHERE s.campaignId=?1 AND s.isDeleted = ?2 AND s.isRead =?3")
	List<Feed> findAllReadFeedsByCampaignId(Campaign campaignId, boolean isDeleted, boolean isRead);
	
	List<Feed>findByIsDeletedFalseAndIsReadTrue();
	
	@Query("SELECT s FROM Feed s WHERE s.targetUserId=?1 AND s.isDeleted = ?2 AND s.isRead =?3")
	List<Feed> findActiveFeedByEmail(User targetedUserId, boolean isDeleted, boolean isRead);
	
	@Query("SELECT s FROM Feed s WHERE s.targetUserId=?1 AND s.isDeleted = ?2 ")
	List<Feed> findAllFeedsByEmail(User targetedUserId, boolean isDeleted);

	@Query("SELECT s FROM Feed s WHERE s.targetUserId=?1 AND s.isDeleted = ?2 AND s.isRead =?3")
	List<Feed> findAllReadFeedsByEmail(User targetedUserId, boolean isDeleted, boolean isRead);

}
