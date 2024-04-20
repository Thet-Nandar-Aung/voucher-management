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
	private UserRepository userRepository;

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

}
