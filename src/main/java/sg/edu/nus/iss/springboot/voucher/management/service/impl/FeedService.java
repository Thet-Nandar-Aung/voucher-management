package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.entity.Feed;
import sg.edu.nus.iss.springboot.voucher.management.repository.FeedRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.IFeedService;

@Service
public class FeedService implements IFeedService        {

    private static final Logger logger = LoggerFactory.getLogger(FeedService.class);

    @Autowired
    private FeedRepository feedRepository;

    @Override
    public List<Feed> findAllFeeds() {
        return feedRepository.findAll();
    }

    @Override
    public Optional<Feed> findByFeedId(String feedId) {
       return feedRepository.findById(feedId);
    }

    @Override
    public Feed save(Feed feed) {
        return feedRepository.save(feed);
    }

	@Override
	public void delete(String feedId) {
		feedRepository.findById(feedId).ifPresent(feed -> feedRepository.delete(feed));
	}

}
