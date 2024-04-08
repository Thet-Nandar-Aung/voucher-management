package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import sg.edu.nus.iss.springboot.voucher.management.dto.FeedDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;

public interface IFeedService {

	// Map<Long, List<FeedDTO>> findAllFeeds(Pageable pageable);

	// Map<Long, List<FeedDTO>> findAllActiveFeedsByCampaignId(String campaignId, Pageable pageable);

	// Map<Long, List<FeedDTO>> findAllReadFeeds(Pageable pageable);

	// Map<Long, List<FeedDTO>> findAllReadFeedsByCampaignId(String campaignId, Pageable pageable);
	
	Map<Long, List<FeedDTO>> findAllFeedsByEmail(String email, Pageable pageable);
	
	// Map<Long, List<FeedDTO>> findAllReadFeedsByEmail(String email, Pageable pageable);

	FeedDTO findByFeedId(String feedId);

	// boolean generateFeed();

	// void delete(String feedId);

	FeedDTO updateReadStatusById(String feedId);

	// Map<Long, List<FeedDTO>> updateReadStatusByEmail(String email, Pageable pageable);
}
