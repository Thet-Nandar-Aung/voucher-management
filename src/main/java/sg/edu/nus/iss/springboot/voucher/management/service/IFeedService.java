package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.springboot.voucher.management.entity.Feed;

public interface IFeedService {

    List<Feed> findAllFeeds();

    Optional<Feed> findByFeedId(String feedId);
    
    Feed save(Feed feed);

    void delete(String feedId);

}
