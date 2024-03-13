package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Override
	public List<FeedDTO> findAllFeeds() {
		logger.info("Getting all feeds");
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		List<Feed> feedList = feedRepository.findByIsDeletedFalse();
		logger.info("Found {}, converting to Feed DTOs...", feedList.size());
		if (!feedList.isEmpty()) {
			Iterator<Feed> feedItr = feedList.iterator();
			while (feedItr.hasNext()) {
				Feed feed = feedItr.next();
				feedDTOList.add(DTOMapper.toFeedDTO(feed));
			}
		}

		return feedDTOList;
	}

	@Override
	public FeedDTO findByFeedId(String feedId) {
		FeedDTO feedDTO = new FeedDTO();
		Optional<Feed> feed = feedRepository.findById(feedId);
		if (feed.isPresent()) {

			feedDTO = DTOMapper.toFeedDTO(feed.get());
		} else {
			logger.info("Feed not found for feedId {}...", feedId);
		}
		return feedDTO;

	}

	@Override
	public List<FeedDTO> save(String campaignId) {
		ArrayList<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		Optional<Campaign> campaign = campaignRepository.findById(campaignId);
		if (campaign.isPresent()) {

			// to amend targeted user logic
			List<User> userList = userRepository.findByIsActiveTrue();
			if (!userList.isEmpty()) {

				Iterator<User> userItr = userList.iterator();

				while (userItr.hasNext()) {
					User user = userItr.next();
					// check feed already generated or not
					List<Feed> dbFeed = feedRepository.findByTargetedUserAndStatus(user, campaign.get(), false, false);
					//
					if (dbFeed.size() == 0) {
						Feed feed = new Feed();
						feed.setCampaignId(campaign.get());
						feed.setCreatedDate(LocalDateTime.now());
						feed.setTargetUserId(user);
						Feed createdFeed = feedRepository.save(feed);
						if (createdFeed != null) {
							FeedDTO feedDTO = new FeedDTO();
							feedDTO = DTOMapper.toFeedDTO(createdFeed);
							feedDTOList.add(feedDTO);
						}

					} else {
						logger.info("Campaign already promoted for Targeted User." + user.getEmail());
					}

				}

			} else {
				logger.info("Targeted User not found.");
			}

		} else {
			logger.info("Campaign not found :" + campaignId);
		}

		return feedDTOList;
	}

	@Override
	public void delete(String feedId) {
		feedRepository.findById(feedId).ifPresent(feed -> feedRepository.delete(feed));
	}

	@Override
	public List<Feed> findByTargetedUserAndStatus(User targetedUser, Campaign campaignId) {
		// TODO Auto-generated method stub
		return feedRepository.findByTargetedUserAndStatus(targetedUser, campaignId, false, false);
	}

	@Override
	public List<FeedDTO> findAllActiveFeedsByCampaignId(String campaignId) {
		logger.info("Getting all feeds by CampaignId");
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		Optional<Campaign> campaign = campaignRepository.findById(campaignId);
		if (campaign.isPresent()) {
			List<Feed> feedList = feedRepository.findAllFeedsByCampaignId(campaign.get(), false);
			logger.info("Found {}, converting to Feed DTOs...", feedList.size());
			if (!feedList.isEmpty()) {
				Iterator<Feed> feedItr = feedList.iterator();
				while (feedItr.hasNext()) {
					Feed feed = feedItr.next();
					feedDTOList.add(DTOMapper.toFeedDTO(feed));
				}
			}
		} else {
			logger.info("Campaign not found :" + campaignId);
		}

		return feedDTOList;
	}

	@Override
	public List<FeedDTO> findAllReadFeeds() {
		logger.info("Getting all read feeds.");
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		List<Feed> feedList = feedRepository.findByIsDeletedFalseAndIsReadTrue();
		logger.info("Found {}, converting to Feed DTOs...", feedList.size());
		if (!feedList.isEmpty()) {
			Iterator<Feed> feedItr = feedList.iterator();
			while (feedItr.hasNext()) {
				Feed feed = feedItr.next();
				feedDTOList.add(DTOMapper.toFeedDTO(feed));
			}
		}

		return feedDTOList;
	}

	@Override
	public List<FeedDTO> findAllReadFeedsByCampaignId(String campaignId) {

		logger.info("Getting all feeds by CampaignId");
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		Optional<Campaign> campaign = campaignRepository.findById(campaignId);
		if (campaign.isPresent()) {
			List<Feed> feedList = feedRepository.findAllReadFeedsByCampaignId(campaign.get(), false, true);
			logger.info("Found {}, converting to Feed DTOs...", feedList.size());
			if (!feedList.isEmpty()) {
				Iterator<Feed> feedItr = feedList.iterator();
				while (feedItr.hasNext()) {
					Feed feed = feedItr.next();
					feedDTOList.add(DTOMapper.toFeedDTO(feed));
				}
			}
		} else {
			logger.info("Campaign not found :" + campaignId);
		}

		return feedDTOList;
	}
	
	

	@Override
	public FeedDTO updateReadStatusById(String feedId) {
		FeedDTO feedDTO = new FeedDTO();
		Optional<Feed> feed = feedRepository.findById(feedId);
		if (feed.isPresent()) {
			feed.get().setRead(true);
			feed.get().setReadTime(LocalDateTime.now());
			Feed updatedFeed = feedRepository.save(feed.get());
			feedDTO = DTOMapper.toFeedDTO(updatedFeed);
		} else {
			logger.info("Feed not found for feedId {}...", feedId);
		}
		return feedDTO;
	}

	@Override
	public List<FeedDTO> updateReadStatusByEmail(String email) {
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		FeedDTO feedDTO = new FeedDTO();
		User user = userRepository.findByEmail(email);
		if (user != null) {
			List<Feed> feedList = feedRepository.findActiveFeedByEmail(user, false, false);
			logger.info("Found {}, Feed for update read status...", feedList.size());
			if (!feedList.isEmpty()) {
				Iterator<Feed> feedItr = feedList.iterator();
				while (feedItr.hasNext()) {
					Feed feed = feedItr.next();
					feed.setRead(true);
					feed.setReadTime(LocalDateTime.now());
					Feed updatedFeed = feedRepository.save(feed);
					feedDTO = DTOMapper.toFeedDTO(updatedFeed);
					feedDTOList.add(feedDTO);
				}

			} else {
				logger.info("Feed not found for email {}...", email);
			}
		}

		return feedDTOList;
	}

	@Override
	public List<FeedDTO> findAllFeedsByEmail(String email) {
		logger.info("Getting all feeds by Email");
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		User user = userRepository.findByEmail(email);
		if (user != null) {
			List<Feed> feedList = feedRepository.findAllFeedsByEmail(user, false);
			logger.info("Found {}, converting to Feed DTOs...", feedList.size());
			if (!feedList.isEmpty()) {
				Iterator<Feed> feedItr = feedList.iterator();
				while (feedItr.hasNext()) {
					Feed feed = feedItr.next();
					feedDTOList.add(DTOMapper.toFeedDTO(feed));
				}
			}
		} else {
			logger.info("User not found :" + email);
		}

		return feedDTOList;
	}

	@Override
	public List<FeedDTO> findAllReadFeedsByEmail(String email) {
		logger.info("Getting all Read feeds by Email");
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		User user = userRepository.findByEmail(email);
		if (user != null) {
			List<Feed> feedList = feedRepository.findAllReadFeedsByEmail(user, false,true);
			logger.info("Found {}, converting to Feed DTOs...", feedList.size());
			if (!feedList.isEmpty()) {
				Iterator<Feed> feedItr = feedList.iterator();
				while (feedItr.hasNext()) {
					Feed feed = feedItr.next();
					feedDTOList.add(DTOMapper.toFeedDTO(feed));
				}
			}
		} else {
			logger.info("User not found :" + email);
		}

		return feedDTOList;
	}

}
