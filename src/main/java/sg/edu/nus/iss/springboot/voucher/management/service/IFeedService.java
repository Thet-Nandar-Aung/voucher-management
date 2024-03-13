package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;

import sg.edu.nus.iss.springboot.voucher.management.dto.FeedDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;

public interface IFeedService {

	List<FeedDTO> findAllFeeds();

	List<FeedDTO> findAllActiveFeedsByCampaignId(String campaignId);

	List<FeedDTO> findAllReadFeeds();

	List<FeedDTO> findAllReadFeedsByCampaignId(String campaignId);
	
	List<FeedDTO> findAllFeedsByEmail(String email);
	
	List<FeedDTO> findAllReadFeedsByEmail(String email);

	FeedDTO findByFeedId(String feedId);

	List<FeedDTO> save(String campaignId);

	void delete(String feedId);

	List<Feed> findByTargetedUserAndStatus(User targetedUser, Campaign campaignId);

	FeedDTO updateReadStatusById(String feedId);

	List<FeedDTO> updateReadStatusByEmail(String email);
}
