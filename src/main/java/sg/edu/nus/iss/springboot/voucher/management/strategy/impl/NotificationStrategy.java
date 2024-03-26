package sg.edu.nus.iss.springboot.voucher.management.strategy.impl;


import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Feed;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.FeedRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.strategy.IFeedStrategy;

@Component
public class NotificationStrategy implements IFeedStrategy {

    private static final Logger logger = LoggerFactory.getLogger(NotificationStrategy.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Override
    public void sendFeed(Campaign campaign) {
        // List of users for sending feed, to be optimised for using criteria, etc.
        List<User> userList = userRepository.findByIsActiveTrue();
        if (!userList.isEmpty()) {
            Iterator<User> userItr = userList.iterator();
            while (userItr.hasNext()) {
                User user = userItr.next();
                // check feed already generated or not
                List<Feed> dbFeed = feedRepository.findByTargetedUserAndStatus(user, campaign, false, false);
                if (dbFeed.size() == 0) {
                    Feed feed = new Feed();
                    feed.setCampaignId(campaignRepository.getById(campaign.getCampaignId()));
                    feed.setCreatedDate(LocalDateTime.now());
                    feed.setTargetUserId(user);
                    Feed createdFeed = feedRepository.save(feed);
                    if (createdFeed != null) {
                        logger.info(
                                "Campaign id " + campaign.getCampaignId() + " sent to user " + user.getEmail() + ".");
                    }
                } else {
                    logger.info("Campaign already promoted for Targeted User." + user.getEmail());
                }
            }
        }
    }

}