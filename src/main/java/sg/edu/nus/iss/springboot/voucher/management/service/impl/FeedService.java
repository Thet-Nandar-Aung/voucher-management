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
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import sg.edu.nus.iss.springboot.voucher.management.dto.*;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
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
		try {

			List<Feed> feedList = feedRepository.findByIsDeletedFalse();
			logger.info("Found {}, converting to Feed DTOs...", feedList.size());
			if (!feedList.isEmpty()) {
				Iterator<Feed> feedItr = feedList.iterator();
				while (feedItr.hasNext()) {
					Feed feed = feedItr.next();
					feedDTOList.add(DTOMapper.toFeedDTO(feed));
				}
			}

		} catch (Exception ex) {
			logger.error("findAllFeeds exception... {}", ex.toString());

		}
		return feedDTOList;
	}

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

	@Override
	@Transactional
	public boolean generateFeed() {
		boolean isGenerated = true;
		try {

			List<Campaign> campaignList = campaignRepository.findByCampaignStatus(CampaignStatus.PROMOTED);
			if (campaignList.size() > 0) {

				Iterator<Campaign> campaignItr = campaignList.iterator();

				while (campaignItr.hasNext()) {
					Campaign campaign = campaignItr.next();
					if (campaign.getCampaignStatus().equals(CampaignStatus.PROMOTED)) {
						// to amend targeted user logic
						List<User> userList = userRepository.findByIsActiveTrue();
						if (!userList.isEmpty()) {

							Iterator<User> userItr = userList.iterator();

							while (userItr.hasNext()) {

								User user = userItr.next();
								// check feed already generated or not

								List<Feed> dbFeed = feedRepository.findByTargetedUserAndStatus(user, campaign, false,
										false);
								

								if (dbFeed.size() == 0) {
									Feed feed = new Feed();
									feed.setCampaignId(campaign);
									feed.setCreatedDate(LocalDateTime.now());
									feed.setTargetUserId(user);
									Feed createdFeed = feedRepository.save(feed);
									if (createdFeed == null) {
										isGenerated = false;
									}

								} else {
									logger.info("Campaign already promoted for Targeted User." + user.getEmail());
									isGenerated = false;
								}

							}

						} else {
							logger.info("Targeted User not found.");
							isGenerated = false;
						}

						if (isGenerated) {
							campaign.setCampaignStatus(CampaignStatus.FEEDGENERATED);
							campaign.setUpdatedDate(LocalDateTime.now());
							Campaign updatedCampaign = campaignRepository.save(campaign);
							logger.info("Feed Generated successfully {}...", updatedCampaign.getCampaignId());
						}
					}
				}
			}

		} catch (Exception ex) {
			logger.error("Feed generating exception... {}", ex.toString());
			isGenerated = false;
		}

		return isGenerated;
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
		try {
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
		} catch (Exception ex) {
			logger.error("Find Feed by CampaignId exception... {}", ex.toString());

		}

		return feedDTOList;
	}

	@Override
	public List<FeedDTO> findAllReadFeeds() {
		logger.info("Getting all read feeds.");
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		try {
			List<Feed> feedList = feedRepository.findByIsDeletedFalseAndIsReadTrue();
			logger.info("Found {}, converting to Feed DTOs...", feedList.size());
			if (!feedList.isEmpty()) {
				Iterator<Feed> feedItr = feedList.iterator();
				while (feedItr.hasNext()) {
					Feed feed = feedItr.next();
					feedDTOList.add(DTOMapper.toFeedDTO(feed));
				}
			}
		} catch (Exception ex) {
			logger.error("Find read Feed exception... {}", ex.toString());

		}

		return feedDTOList;
	}

	@Override
	public List<FeedDTO> findAllReadFeedsByCampaignId(String campaignId) {

		logger.info("Getting all feeds by CampaignId");
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		try {
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
		} catch (Exception ex) {
			logger.error("Find read Feed by CampaignId exception... {}", ex.toString());

		}
		return feedDTOList;
	}

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

	@Override
	public List<FeedDTO> updateReadStatusByEmail(String email) {
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		try {
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
		} catch (Exception ex) {
			logger.error("Updating Feed Status by Email exception... {}", ex.toString());

		}

		return feedDTOList;
	}

	@Override
	public List<FeedDTO> findAllFeedsByEmail(String email) {
		logger.info("Getting all feeds by Email");
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		try {
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
		} catch (Exception ex) {
			logger.error("Find read Feed by Email exception... {}", ex.toString());

		}
		return feedDTOList;
	}

	@Override
	public List<FeedDTO> findAllReadFeedsByEmail(String email) {
		logger.info("Getting all Read feeds by Email");
		List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		try {
			User user = userRepository.findByEmail(email);
			if (user != null) {
				List<Feed> feedList = feedRepository.findAllReadFeedsByEmail(user, false, true);
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
		} catch (Exception ex) {
			logger.error("Find read Feed  by Email exception... {}", ex.toString());

		}

		return feedDTOList;
	}

}
