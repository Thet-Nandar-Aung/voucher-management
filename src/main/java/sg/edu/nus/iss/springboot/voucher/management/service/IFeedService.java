package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.springboot.voucher.management.dto.FeedDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;

public interface IFeedService {

    List<Feed> findAllFeeds();

    Optional<Feed> findByFeedId(String feedId);
    
    ArrayList <FeedDTO> save(String campaignId);

    void delete(String feedId);
    
    List<Feed>  findByTargetedUserAndStatus(User targetedUser,Campaign campaignId) ;

}
