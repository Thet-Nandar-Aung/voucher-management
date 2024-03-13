package sg.edu.nus.iss.springboot.voucher.management.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.springboot.voucher.management.dto.*;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.FeedService;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;

@RestController
@Validated
@RequestMapping("/api/feed")
public class FeedController {
	private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

	@Autowired
	private FeedService feedService;

	@GetMapping(value = "/getAll", produces = "application/json")
	public ResponseEntity<APIResponse<List<FeedDTO>>> getAllFeed() {
		logger.info("Call getAll feed API...");
		try {
			List<FeedDTO> feedDTOList = feedService.findAllFeeds();
			if (feedDTOList.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(APIResponse.success(feedDTOList, "Successfully get all feed."));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Feed not found."));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Calling getAll API feed failed , " + e.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
		}

	}

	@GetMapping(value = "/getAllRead", produces = "application/json")
	public ResponseEntity<APIResponse<List<FeedDTO>>> getAllReadFeeds() {
		logger.info("Call getAllRead feed API...");
		try {
			List<FeedDTO> feedDTOList = feedService.findAllReadFeeds();
			if (feedDTOList.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(APIResponse.success(feedDTOList, "Successfully get all read feed."));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Feed (read) not found."));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Calling getAllRead feed API failed , " + e.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
		}

	}

	@GetMapping(value = "/getAllByCampaignId/{campaignId}", produces = "application/json")
	public ResponseEntity<APIResponse<List<FeedDTO>>> getAllByCampaignId(@PathVariable String campaignId) {
		logger.info("Call getAllByCampaignId feed API...");
		try {
			logger.info("campaignId: " + campaignId);
			String message = "";
			if (!GeneralUtility.makeNotNull(campaignId).equals("")) {
				List<FeedDTO> feedDTOList = feedService.findAllActiveFeedsByCampaignId(campaignId);
				if (feedDTOList.size() > 0) {

					return ResponseEntity.status(HttpStatus.OK)
							.body(APIResponse.success(feedDTOList, "Successfully get all feed by CampaignId."));

				} else {

					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.error("Feed not found by CampaignId: " + campaignId));
				}
			} else {
				message = "Bad Request:Campaign ID could not be blank.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Calling getAllByCampaignId feed API failed , " + e.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
		}

	}

	@GetMapping(value = "/getAllReadByCampaignId/{campaignId}", produces = "application/json")
	public ResponseEntity<APIResponse<List<FeedDTO>>> findAllReadFeedsByCampaignId(@PathVariable String campaignId) {
		logger.info("Call getAllReadByCampaignId feed API...");
		try {
			String message = "";
			logger.info("campaignId: " + campaignId);
			if (!GeneralUtility.makeNotNull(campaignId).equals("")) {
				List<FeedDTO> feedDTOList = feedService.findAllReadFeedsByCampaignId(campaignId);
				if (feedDTOList.size() > 0) {

					return ResponseEntity.status(HttpStatus.OK)
							.body(APIResponse.success(feedDTOList, "Successfully get all read feed by CampaignId."));

				} else {

					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.error("Feed (read) not found by CampaignId: " + campaignId));
				}
			} else {
				message = "Bad Request:Campaign ID could not be blank.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Calling getAllReadByCampaignId feed API failed , " + e.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
		}

	}

	@PostMapping(value = "/getAllByEmail", produces = "application/json")
	public ResponseEntity<APIResponse<List<FeedDTO>>> getAllFeedsByEmail(@RequestBody UserRequest userReq) {
		logger.info("Call getAllByEmail feed API...");
		try {
			String message = "";
			logger.info("UserRequest: " + userReq);

			String email = GeneralUtility.makeNotNull(userReq.getEmail()).trim();

			if (!email.equals("")) {

				logger.info("email: " + email);

				List<FeedDTO> feedDTOList = feedService.findAllFeedsByEmail(email);
				if (feedDTOList.size() > 0) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(APIResponse.success(feedDTOList, "Successfully get all feed."));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Feed not found."));
				}

			} else {
				message = "Bad Request:User could not be blank.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Calling getAllByEmail feed API failed , " + e.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
		}

	}

	@PostMapping(value = "/getAllReadFeedsByEmail", produces = "application/json")
	public ResponseEntity<APIResponse<List<FeedDTO>>> findAllReadFeedsByEmail(@RequestBody UserRequest userReq) {
		logger.info("Call findAllReadFeedsByEmail feed API...");
		try {
			String message = "";
			logger.info("UserRequest: " + userReq);

			String email = GeneralUtility.makeNotNull(userReq.getEmail()).trim();

			if (!email.equals("")) {

				logger.info("email: " + email);

				List<FeedDTO> feedDTOList = feedService.findAllReadFeedsByEmail(email);
				if (feedDTOList.size() > 0) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(APIResponse.success(feedDTOList, "Successfully get all feed."));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Feed not found."));
				}

			} else {
				message = "Bad Request:User could not be blank.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Calling findAllReadFeedsByEmail feed API failed , " + e.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
		}

	}

	@GetMapping(value = "/getById/{feedId}", produces = "application/json")
	public ResponseEntity<APIResponse<FeedDTO>> getFeedById(@PathVariable String feedId) {
		try {
			logger.info("Calling getById Feed API...");
			logger.info("feedId: " + feedId);
			String message = "";
			if (!GeneralUtility.makeNotNull(feedId).equals("")) {
				FeedDTO feedDTO = feedService.findByFeedId(feedId);

				if (!GeneralUtility.makeNotNull(feedDTO).equals("")) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(APIResponse.success(feedDTO, "FeedId get successfully: " + feedId));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.error("Invalid FeedId: " + feedId));
				}

			} else {
				message = "Bad Request:FeedId could not be blank.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Calling getById feed API failed , " + e.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
		}

	}

	@PostMapping(value = "/updateReadStatusById/{feedId}", produces = "application/json")
	public ResponseEntity<APIResponse<FeedDTO>> updateReadStatusById(@PathVariable String feedId) {
		try {
			logger.info("Calling updateReadStatusById Feed API...");
			logger.info("feedId: " + feedId);
			String message = "";
			if (!GeneralUtility.makeNotNull(feedId).equals("")) {
				FeedDTO feedDTO = feedService.updateReadStatusById(feedId);

				if (!GeneralUtility.makeNotNull(feedDTO).equals("")) {
					return ResponseEntity.status(HttpStatus.OK).body(
							APIResponse.success(feedDTO, "Read status updated successfully for FeedId: " + feedId));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.error("Invalid FeedId: " + feedId));
				}
			} else {
				message = "Bad Request:FeedId could not be blank.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Calling updateReadStatusById feed API failed , " + e.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
		}

	}

	@PostMapping(value = "/updateReadStatusByEmail", produces = "application/json")
	public ResponseEntity<APIResponse<List<FeedDTO>>> updateReadStatusByEmail(@RequestBody UserRequest userReq) {
		try {
			logger.info("Calling updateReadStatusByEmail Feed API...");

			String message = "";
			logger.info("UserRequest: " + userReq);

			String email = GeneralUtility.makeNotNull(userReq.getEmail()).trim();

			if (!email.equals("")) {

				logger.info("email: " + email);
				List<FeedDTO> feedDTOList = feedService.updateReadStatusByEmail(email);

				if (feedDTOList.size() > 0) {
					return ResponseEntity.status(HttpStatus.OK).body(
							APIResponse.success(feedDTOList, "Read status updated successfully for user: " + email));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Feed not found for user: "+email));
				}

			} else {
				message = "Bad Request:User could not be blank.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Calling updateReadStatusByEmail feed API failed , " + e.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
		}

	}

}
