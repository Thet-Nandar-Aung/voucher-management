package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sg.edu.nus.iss.springboot.voucher.management.dto.*;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.repository.*;
import sg.edu.nus.iss.springboot.voucher.management.service.IFeedService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;

@Service
public class FeedService implements IFeedService {

	private static final Logger logger = LoggerFactory.getLogger(FeedService.class);

	@Autowired
	private FeedRepository feedRepository;

	@Autowired
	private CampaignRepository campaignRepository;

	@Autowired
	private UserRepository userRepository;

	// @Override
	// public Map<Long, List<FeedDTO>> findAllFeeds(Pageable pageable) {
	// logger.info("Getting all feeds");
	// Map<Long, List<FeedDTO>> result = new HashMap<>();
	// List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
	// try {
	// Page<Feed> feedPages = feedRepository.findByIsDeletedFalse(pageable);
	// long totalRecord = feedPages.getTotalElements();
	// if (totalRecord > 0) {
	// logger.info("Found {}, converting to Feed DTOs...", totalRecord);
	// for (Feed feed : feedPages.getContent()) {
	// FeedDTO feedDTO = DTOMapper.toFeedDTO(feed);
	// feedDTOList.add(feedDTO);
	// }
	// } else {
	// logger.info("No feed found...");
	// }
	// result.put(totalRecord, feedDTOList);
	// } catch (Exception ex) {
	// logger.error("findAllFeeds exception... {}", ex.toString());
	// }
	// return result;
	// }

	@Override
	public FeedDTO findByFeedId(String feedId) {
		FeedDTO feedDTO = new FeedDTO();
		try {
			Optional<Feed> feed = feedRepository.findById(feedId);
			if (feed.isPresent()) {
				feedDTO = DTOMapper.toFeedDTO(feed.get());
			} else {
				logger.info("Feed not found for feedId {}...", feedId);
			}
		} catch (Exception ex) {
			logger.error("findByFeedId exception... {}", ex.toString());
		}
		return feedDTO;
	}

	// @Override
	// @Transactional
	// public boolean generateFeed() {
	// boolean isPromoted = true;
	// try {

	// List<Campaign> campaignList = campaignRepository
	// .findByCampaignStatusIn(Arrays.asList(CampaignStatus.READYTOPROMOTE));
	// if (campaignList.size() > 0) {

	// Iterator<Campaign> campaignItr = campaignList.iterator();

	// while (campaignItr.hasNext()) {
	// Campaign campaign = campaignItr.next();
	// if (campaign.getCampaignStatus().equals(CampaignStatus.READYTOPROMOTE)) {
	// // to amend targeted user logic
	// List<User> userList = userRepository.findByIsActiveTrue();
	// if (!userList.isEmpty()) {

	// Iterator<User> userItr = userList.iterator();

	// while (userItr.hasNext()) {

	// User user = userItr.next();
	// // check feed already generated or not

	// List<Feed> dbFeed = feedRepository.findByTargetedUserAndStatus(user,
	// campaign, false,
	// false);

	// if (dbFeed.size() == 0) {
	// Feed feed = new Feed();
	// feed.setCampaignId(campaign);
	// feed.setCreatedDate(LocalDateTime.now());
	// feed.setTargetUserId(user);
	// Feed createdFeed = feedRepository.save(feed);
	// if (createdFeed == null) {
	// isPromoted = false;
	// }

	// } else {
	// logger.info("Campaign already promoted for Targeted User." +
	// user.getEmail());
	// isPromoted = false;
	// }

	// }

	// } else {
	// logger.info("Targeted User not found.");
	// isPromoted = false;
	// }

	// if (isPromoted) {
	// campaign.setCampaignStatus(CampaignStatus.PROMOTED);
	// campaign.setUpdatedDate(LocalDateTime.now());
	// Campaign updatedCampaign = campaignRepository.save(campaign);
	// logger.info("Feed Generated successfully {}...",
	// updatedCampaign.getCampaignId());
	// }
	// }
	// }
	// }

	// } catch (Exception ex) {
	// logger.error("Feed generating exception... {}", ex.toString());
	// isPromoted = false;
	// }

	// return isPromoted;
	// }

	// @Override
	// public void delete(String feedId) {
	// feedRepository.findById(feedId).ifPresent(feed ->
	// feedRepository.delete(feed));
	// }

	// @Override
	// public List<Feed> findByTargetedUserAndStatus(User targetedUser, Campaign
	// campaign) {
	// logger.info("Getting all feeds by user and statuses");
	// List<Feed> feedList = new ArrayList<Feed>();
	// try {
	// Optional<Campaign> campaignInDB =
	// campaignRepository.findById(campaign.getCampaignId());
	// if (campaignInDB.isPresent()) {
	// feedList = feedRepository.findByTargetedUserAndStatus(targetedUser, campaign,
	// false,
	// false);
	// long totalRecord = feedList.size();
	// if (totalRecord > 0) {
	// logger.info("Found {} feed(s) for User {} and Campaign...", totalRecord,
	// targetedUser.getEmail(),
	// campaign.getCampaignId());
	// } else {
	// logger.info("No feed found...");
	// }
	// } else {
	// logger.info("Campaign not found :" + campaign.getCampaignId());
	// }
	// } catch (Exception ex) {
	// logger.error("Find Feed by CampaignId exception... {}", ex.toString());
	// }
	// return feedList;
	// }

	// @Override
	// public Map<Long, List<FeedDTO>> findAllActiveFeedsByCampaignId(String
	// campaignId, Pageable pageable) {
	// logger.info("Getting all feeds by CampaignId");
	// Map<Long, List<FeedDTO>> result = new HashMap<>();
	// List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
	// try {
	// Optional<Campaign> campaign = campaignRepository.findById(campaignId);
	// if (campaign.isPresent()) {
	// Page<Feed> feedPages =
	// feedRepository.findAllFeedsByCampaignId(campaign.get(), false, pageable);
	// long totalRecord = feedPages.getTotalElements();
	// if (totalRecord > 0) {
	// logger.info("Found {}, converting to Feed DTOs...", totalRecord);
	// for (Feed feed : feedPages.getContent()) {
	// FeedDTO feedDTO = DTOMapper.toFeedDTO(feed);
	// feedDTOList.add(feedDTO);
	// }
	// } else {
	// logger.info("No feed found...");
	// }
	// } else {
	// logger.info("Campaign not found :" + campaignId);
	// }
	// } catch (Exception ex) {
	// logger.error("Find Feed by CampaignId exception... {}", ex.toString());
	// }
	// return result;
	// }

	// @Override
	// public Map<Long, List<FeedDTO>> findAllReadFeeds(Pageable pageable) {
	// logger.info("Getting all read feeds.");
	// Map<Long, List<FeedDTO>> result = new HashMap<>();
	// List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
	// try {
	// Page<Feed> feedPages =
	// feedRepository.findByIsDeletedFalseAndIsReadTrue(pageable);
	// long totalRecord = feedPages.getTotalElements();
	// if (totalRecord > 0) {
	// logger.info("Found {}, converting to Feed DTOs...", totalRecord);
	// for (Feed feed : feedPages.getContent()) {
	// FeedDTO feedDTO = DTOMapper.toFeedDTO(feed);
	// feedDTOList.add(feedDTO);
	// }
	// } else {
	// logger.info("No feed found...");
	// }
	// } catch (Exception ex) {
	// logger.error("Find read Feed exception... {}", ex.toString());
	// }
	// return result;
	// }

	// @Override
	// public Map<Long, List<FeedDTO>> findAllReadFeedsByCampaignId(String
	// campaignId, Pageable pageable) {
	// logger.info("Getting all feeds by CampaignId");
	// Map<Long, List<FeedDTO>> result = new HashMap<>();
	// List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
	// try {
	// Optional<Campaign> campaign = campaignRepository.findById(campaignId);
	// if (campaign.isPresent()) {
	// Page<Feed> feedPages =
	// feedRepository.findAllReadFeedsByCampaignId(campaign.get(), false, true,
	// pageable);
	// long totalRecord = feedPages.getTotalElements();
	// if (totalRecord > 0) {
	// logger.info("Found {}, converting to Feed DTOs...", totalRecord);
	// for (Feed feed : feedPages.getContent()) {
	// FeedDTO feedDTO = DTOMapper.toFeedDTO(feed);
	// feedDTOList.add(feedDTO);
	// }
	// } else {
	// logger.info("No feed found...");
	// }
	// } else {
	// logger.info("Campaign not found :" + campaignId);
	// }
	// } catch (Exception ex) {
	// logger.error("Find read Feed by CampaignId exception... {}", ex.toString());
	// }
	// return result;
	// }

	@Override
	public FeedDTO updateReadStatusById(String feedId) {
		FeedDTO feedDTO = new FeedDTO();
		try {
			Optional<Feed> feed = feedRepository.findById(feedId);
			if (feed.isPresent()) {
				feed.get().setRead(true);
				feed.get().setReadTime(LocalDateTime.now());
				Feed updatedFeed = feedRepository.save(feed.get());
				feedDTO = DTOMapper.toFeedDTO(updatedFeed);
			} else {
				logger.info("Feed not found for feedId {}...", feedId);
			}
		} catch (Exception ex) {
			logger.error("Updating Feed Status by feedId exception... {}", ex.toString());

		}
		return feedDTO;
	}

	// @Override
	// public Map<Long, List<FeedDTO>> updateReadStatusByEmail(String email,
	// Pageable pageable) {
	// Map<Long, List<FeedDTO>> result = new HashMap<>();
	// List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
	// try {
	// FeedDTO feedDTO = new FeedDTO();
	// User user = userRepository.findByEmail(email);
	// if (user != null) {
	// Page<Feed> feedPages = feedRepository.findActiveFeedByEmail(user, false,
	// false, pageable);
	// long totalRecord = feedPages.getTotalElements();
	// if (totalRecord > 0) {
	// logger.info("Found {}, Feed for update read status...", totalRecord);
	// Iterator<Feed> feedItr = feedPages.iterator();
	// while (feedItr.hasNext()) {
	// Feed feed = feedItr.next();
	// feed.setRead(true);
	// feed.setReadTime(LocalDateTime.now());
	// Feed updatedFeed = feedRepository.save(feed);
	// feedDTO = DTOMapper.toFeedDTO(updatedFeed);
	// feedDTOList.add(feedDTO);
	// }
	// } else {
	// logger.info("Feed not found for email {}...", email);
	// }
	// }
	// } catch (Exception ex) {
	// logger.error("Updating Feed Status by Email exception... {}", ex.toString());
	// }
	// return result;
	// }

	@Override
	public Map<Long, List<FeedDTO>> findAllFeedsByEmail(String email, Pageable pageable) {
		logger.info("Getting all feeds by Email");
		Map<Long, List<FeedDTO>> result = new HashMap<>();
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		try {
			User user = userRepository.findByEmail(email);
			if (user != null) {
				Page<Feed> feedPages = feedRepository.findAllFeedsByEmail(user, false, pageable);
				long totalRecord = feedPages.getTotalElements();
				if (totalRecord > 0) {
					logger.info("Found {}, converting to Feed DTOs...", totalRecord);
					for (Feed feed : feedPages.getContent()) {
						FeedDTO feedDTO = DTOMapper.toFeedDTO(feed);
						feedDTOList.add(feedDTO);
					}
				} else {
					logger.info("No feed found...");
				}
				result.put(totalRecord, feedDTOList);
			} else {
				logger.info("User not found :" + email);
			}
		} catch (Exception ex) {
			logger.error("Find read Feed by Email exception... {}", ex.toString());
		}
		return result;
	}

	// @Override
	// public Map<Long, List<FeedDTO>> findAllReadFeedsByEmail(String email,
	// Pageable pageable) {
	// logger.info("Getting all Read feeds by Email");
	// Map<Long, List<FeedDTO>> result = new HashMap<>();
	// List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
	// try {
	// User user = userRepository.findByEmail(email);
	// if (user != null) {
	// Page<Feed> feedPages = feedRepository.findAllReadFeedsByEmail(user, false,
	// true, pageable);
	// long totalRecord = feedPages.getTotalElements();
	// if (totalRecord > 0) {
	// logger.info("Found {}, converting to Feed DTOs...", totalRecord);
	// for (Feed feed : feedPages.getContent()) {
	// FeedDTO feedDTO = DTOMapper.toFeedDTO(feed);
	// feedDTOList.add(feedDTO);
	// }
	// } else {
	// logger.info("No feed found...");
	// }
	// } else {
	// logger.info("User not found :" + email);
	// }
	// } catch (Exception ex) {
	// logger.error("Find read Feed by Email exception... {}", ex.toString());
	// }
	// return result;
	// }

}
