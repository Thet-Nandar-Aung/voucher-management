package sg.edu.nus.iss.springboot.voucher.management.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Feed;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;

@Repository
public interface FeedRepository extends JpaRepository<Feed, String> {
	
	// Page<Feed>findByIsDeletedFalse(Pageable pageable);

	@Query("SELECT s FROM Feed s WHERE s.targetUserId = ?1 AND s.campaignId=?2")
	Feed findByTargetedUserAndCampaign(User targetedUser, Campaign campaign);

	// @Query("SELECT s FROM Feed s WHERE s.campaignId=?1 AND s.isDeleted = ?2 ")
	// Page<Feed> findAllFeedsByCampaignId(Campaign campaignId, boolean isDeleted, Pageable pageable);

	// @Query("SELECT s FROM Feed s WHERE s.campaignId=?1 AND s.isDeleted = ?2 AND s.isRead =?3")
	// Page<Feed> findAllReadFeedsByCampaignId(Campaign campaignId, boolean isDeleted, boolean isRead, Pageable pageable);
	
	// Page<Feed>findByIsDeletedFalseAndIsReadTrue(Pageable pageable);
	
	// @Query("SELECT s FROM Feed s WHERE s.targetUserId=?1 AND s.isDeleted = ?2 AND s.isRead =?3")
	// Page<Feed> findActiveFeedByEmail(User targetedUserId, boolean isDeleted, boolean isRead, Pageable pageable);
	
	@Query("SELECT s FROM Feed s WHERE s.targetUserId=?1 AND s.isDeleted = ?2 ")
	Page<Feed> findAllFeedsByEmail(User targetedUserId, boolean isDeleted, Pageable pageable);

	@Query("SELECT s FROM Feed s WHERE s.targetUserId=?1 AND s.isDeleted = ?2 AND s.isRead =?3")
	Page<Feed> findAllReadFeedsByEmail(User targetedUserId, boolean isDeleted, boolean isRead, Pageable pageable);

}
