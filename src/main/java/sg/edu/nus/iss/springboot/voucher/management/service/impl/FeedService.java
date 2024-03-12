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

import sg.edu.nus.iss.springboot.voucher.management.dto.FeedDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Feed;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.FeedRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.IFeedService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;

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
	public List<Feed> findAllFeeds() {
		return feedRepository.findAll();
	}

	@Override
	public Optional<Feed> findByFeedId(String feedId) {
		return feedRepository.findById(feedId);
	}

	@Override
	public ArrayList<FeedDTO> save(String campaignId) {
		ArrayList<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		Optional<Campaign> campaign = campaignRepository.findById(campaignId);
		if (campaign.isPresent()) {

			// to amend targeted user logic
			List<User> userList = userRepository.findByIsActiveTrue();
			if (!userList.isEmpty()) {

				Iterator<User> user = userList.iterator();

				while (user.hasNext()) {
					User val = user.next();
					// check feed already generated or not
					 List<Feed>  dbFeed = feedRepository.findByTargetedUserAndStatus(val, campaign.get(), false, false);
					//
					if (dbFeed.size() == 0 ) {
						Feed feed = new Feed();
						feed.setCampaignId(campaign.get());
						feed.setCreatedDate(LocalDateTime.now());
						feed.setTargetUserId(val);
						Feed createdFeed = feedRepository.save(feed);
						if (createdFeed != null) {
							FeedDTO feedDTO = new FeedDTO();
							feedDTO = DTOMapper.toFeedDTO(createdFeed);
							feedDTOList.add(feedDTO);
						}

					} else {
						logger.info("Campaign already promoted for Targeted User." + val.getEmail());
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
	public  List<Feed>  findByTargetedUserAndStatus(User targetedUser, Campaign campaignId) {
		// TODO Auto-generated method stub
		return feedRepository.findByTargetedUserAndStatus(targetedUser, campaignId, false, false);
	}

}
