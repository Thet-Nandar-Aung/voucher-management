package sg.edu.nus.iss.springboot.voucher.management.Scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import sg.edu.nus.iss.springboot.voucher.management.service.impl.FeedService;

@Component
public class FeedGenerateWatcherHandler {

	private static final Logger logger = LoggerFactory.getLogger(FeedGenerateWatcherHandler.class);

	@Autowired
	private FeedService feedService;


	@Scheduled(fixedDelay = 300000) // 5 minutes = 300,000 milliseconds
	public void run() {

		logger.info("Star Run FeedGenerateWatcherHandler...");

		try {
            feedService.generateFeed();

		} catch (Exception ex) {
			logger.error("FeedGenerateWatcherHandler exception... {}", ex.toString());
			ex.printStackTrace();
		}
	}

}
