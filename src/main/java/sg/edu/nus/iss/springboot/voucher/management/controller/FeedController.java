package sg.edu.nus.iss.springboot.voucher.management.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import sg.edu.nus.iss.springboot.voucher.management.dto.*;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Feed;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.FeedService;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;

@RestController
@Validated
@RequestMapping("/api/feed")
public class FeedController {
	private static final Logger logger = LoggerFactory.getLogger(FeedController.class);

	@Autowired
	private FeedService feedService;

	// @GetMapping(value = "/getAll", produces = "application/json")
	// public ResponseEntity<APIResponse<List<FeedDTO>>>
	// getAllActiveFeed(@RequestParam(defaultValue = "0") int page,
	// @RequestParam(defaultValue = "500") int size) {
	// logger.info("Call getAll feed API...");
	// try {
	// Pageable pageable = PageRequest.of(page, size,
	// Sort.by("createdDate").descending());
	// Map<Long, List<FeedDTO>> resultMap = feedService.findAllFeeds(pageable);
	// long totalRecord = 0;
	// if (resultMap.size() == 0) {
	// String mesasge = "Feed not found";
	// logger.error(mesasge);
	// return
	// ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(mesasge));
	// }
	// List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
	// for (Map.Entry<Long, List<FeedDTO>> entry : resultMap.entrySet()) {
	// totalRecord = entry.getKey();
	// feedDTOList = entry.getValue();
	// logger.info("totalRecord: " + totalRecord);
	// logger.info("FeedDTO List: " + feedDTOList);
	// }
	// return ResponseEntity.status(HttpStatus.OK)
	// .body(APIResponse.success(feedDTOList, "Successfully get all feeds.",
	// totalRecord));
	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.error("Calling getAll feed API failed , " + e.toString());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
	// }
	// }

	// @GetMapping(value = "/getAllRead", produces = "application/json")
	// public ResponseEntity<APIResponse<List<FeedDTO>>>
	// getAllReadFeeds(@RequestParam(defaultValue = "0") int page,
	// @RequestParam(defaultValue = "500") int size) {
	// logger.info("Call getAllRead feed API...");
	// try {
	// Pageable pageable = PageRequest.of(page, size,
	// Sort.by("createdDate").descending());
	// Map<Long, List<FeedDTO>> resultMap = feedService.findAllReadFeeds(pageable);
	// long totalRecord = 0;
	// if (resultMap.size() == 0) {
	// String mesasge = "No feed found";
	// logger.error(mesasge);
	// return
	// ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(mesasge));
	// }
	// List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
	// for (Map.Entry<Long, List<FeedDTO>> entry : resultMap.entrySet()) {
	// totalRecord = entry.getKey();
	// feedDTOList = entry.getValue();
	// logger.info("totalRecord: " + totalRecord);
	// logger.info("FeedDTO List: " + feedDTOList);
	// }
	// return ResponseEntity.status(HttpStatus.OK)
	// .body(APIResponse.success(feedDTOList, "Successfully get all read feeds.",
	// totalRecord));
	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.error("Calling getAllRead feed API failed, " + e.toString());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
	// }
	// }

	// @PostMapping(value = "/getAllByCampaignId", produces = "application/json")
	// public ResponseEntity<APIResponse<List<FeedDTO>>>
	// getAllByCampaignId(@RequestBody Campaign campaign,
	// @RequestParam(defaultValue = "0") int page,
	// @RequestParam(defaultValue = "500") int size) {
	// logger.info("Call getAllByCampaignId feed API...");
	// try {
	// String campaignId =
	// GeneralUtility.makeNotNull(campaign.getCampaignId()).trim();
	// logger.info("campaignId: " + campaignId);
	// String message = "";
	// if (!GeneralUtility.makeNotNull(campaignId).equals("")) {
	// Pageable pageable = PageRequest.of(page, size,
	// Sort.by("createdDate").descending());
	// Map<Long, List<FeedDTO>> resultMap =
	// feedService.findAllActiveFeedsByCampaignId(campaignId, pageable);
	// long totalRecord = 0;
	// if (resultMap.size() == 0) {
	// String mesasge = "No feed found for campain id ";
	// logger.error(mesasge);
	// return
	// ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(mesasge +
	// campaignId));
	// }
	// List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
	// for (Map.Entry<Long, List<FeedDTO>> entry : resultMap.entrySet()) {
	// totalRecord = entry.getKey();
	// feedDTOList = entry.getValue();
	// logger.info("totalRecord: " + totalRecord);
	// logger.info("FeedDTO List: " + feedDTOList);
	// }
	// return ResponseEntity.status(HttpStatus.OK)
	// .body(APIResponse.success(feedDTOList, "Successfully get all active feeds for
	// campaignId.",
	// totalRecord));
	// } else {
	// message = "Bad Request:Campaign ID could not be blank.";
	// logger.error(message);
	// return
	// ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.error("Calling getAllByCampaignId feed API failed , " + e.toString());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
	// }

	// }

	// @PostMapping(value = "/getAllReadByCampaignId", produces =
	// "application/json")
	// public ResponseEntity<APIResponse<List<FeedDTO>>>
	// findAllReadFeedsByCampaignId(@RequestBody Campaign campaign,
	// @RequestParam(defaultValue = "0") int page,
	// @RequestParam(defaultValue = "500") int size) {
	// logger.info("Call getAllReadByCampaignId feed API...");
	// try {
	// String message = "";
	// String campaignId =
	// GeneralUtility.makeNotNull(campaign.getCampaignId()).trim();
	// logger.info("campaignId: " + campaignId);
	// if (!GeneralUtility.makeNotNull(campaignId).equals("")) {
	// Pageable pageable = PageRequest.of(page, size,
	// Sort.by("createdDate").descending());
	// Map<Long, List<FeedDTO>> resultMap =
	// feedService.findAllReadFeedsByCampaignId(campaignId, pageable);
	// long totalRecord = 0;
	// if (resultMap.size() == 0) {
	// String mesasge = "No feed found for campain id ";
	// logger.error(mesasge);
	// return
	// ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(mesasge +
	// campaignId));
	// }
	// List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
	// for (Map.Entry<Long, List<FeedDTO>> entry : resultMap.entrySet()) {
	// totalRecord = entry.getKey();
	// feedDTOList = entry.getValue();
	// logger.info("totalRecord: " + totalRecord);
	// logger.info("FeedDTO List: " + feedDTOList);
	// }
	// return ResponseEntity.status(HttpStatus.OK)
	// .body(APIResponse.success(feedDTOList, "Successfully get all active feeds for
	// campaignId.",
	// totalRecord));
	// } else {
	// message = "Bad Request:Campaign ID could not be blank.";
	// logger.error(message);
	// return
	// ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.error("Calling getAllReadByCampaignId feed API failed , " +
	// e.toString());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
	// }
	// }

	@PostMapping(value = "/getAllByEmail", produces = "application/json")
	public ResponseEntity<APIResponse<List<FeedDTO>>> getAllFeedsByEmail(@RequestBody UserRequest userReq,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "500") int size) {
		logger.info("Call getAllByEmail feed API...");
		try {
			String message = "";
			logger.info("UserRequest: " + userReq);
			String email = GeneralUtility.makeNotNull(userReq.getEmail()).trim();
			if (!email.equals("")) {
				logger.info("email: " + email);
				Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
				Map<Long, List<FeedDTO>> resultMap = feedService.findAllFeedsByEmail(email, pageable);
				List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
				long totalRecord = 0;
				if (resultMap.size() == 0) {
					String mesasge = "No feed found for email: ";
					logger.error(mesasge);
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.success(feedDTOList, mesasge + email, totalRecord));
				}
				for (Map.Entry<Long, List<FeedDTO>> entry : resultMap.entrySet()) {
					totalRecord = entry.getKey();
					feedDTOList = entry.getValue();
					logger.info("totalRecord: " + totalRecord);
					logger.info("FeedDTO List: " + feedDTOList);
				}
				return ResponseEntity.status(HttpStatus.OK)
						.body(APIResponse.success(feedDTOList, "Successfully get all feeds for email: " + email,
								totalRecord));
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

	// @PostMapping(value = "/getAllActiveByEmail", produces = "application/json")
	// public ResponseEntity<APIResponse<List<FeedDTO>>>
	// getAllActiveByEmail(@RequestBody UserRequest userReq,
	// @RequestParam(defaultValue = "0") int page,
	// @RequestParam(defaultValue = "500") int size) {
	// logger.info("Call getAllByEmail feed API...");
	// try {
	// String message = "";
	// logger.info("UserRequest: " + userReq);

	// String email = GeneralUtility.makeNotNull(userReq.getEmail()).trim();

	// if (!email.equals("")) {
	// logger.info("email: " + email);
	// Pageable pageable = PageRequest.of(page, size,
	// Sort.by("createdDate").descending());
	// Map<Long, List<FeedDTO>> resultMap = feedService.findActiveFeedByEmail(email,
	// pageable);
	// long totalRecord = 0;
	// if (resultMap.size() == 0) {
	// String mesasge = "No feed found for email: ";
	// logger.error(mesasge);
	// return
	// ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(mesasge +
	// email));
	// }
	// List<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
	// for (Map.Entry<Long, List<FeedDTO>> entry : resultMap.entrySet()) {
	// totalRecord = entry.getKey();
	// feedDTOList = entry.getValue();
	// logger.info("totalRecord: " + totalRecord);
	// logger.info("FeedDTO List: " + feedDTOList);
	// }
	// return ResponseEntity.status(HttpStatus.OK)
	// .body(APIResponse.success(feedDTOList, "Successfully get all feeds for email:
	// " + email, totalRecord));

	// List<FeedDTO> feedDTOList = feedService.findAllFeedsByEmail(email);
	// if (feedDTOList.size() > 0) {
	// return ResponseEntity.status(HttpStatus.OK)
	// .body(APIResponse.success(feedDTOList, "All feed get Successfully."));
	// } else {
	// return
	// ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Feed not
	// found."));
	// }

	// } else {
	// message = "Bad Request:User could not be blank.";
	// logger.error(message);
	// return
	// ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
	// }

	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.error("Calling getAllByEmail feed API failed , " + e.toString());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
	// }

	// }

	// @PostMapping(value = "/getAllReadFeedsByEmail", produces =
	// "application/json")
	// public ResponseEntity<APIResponse<List<FeedDTO>>>
	// findAllReadFeedsByEmail(@RequestBody UserRequest userReq,
	// @RequestParam(defaultValue = "0") int page,
	// @RequestParam(defaultValue = "500") int size) {
	// logger.info("Call findAllReadFeedsByEmail feed API...");
	// try {
	// String message = "";
	// logger.info("UserRequest: " + userReq);

	// String email = GeneralUtility.makeNotNull(userReq.getEmail()).trim();

	// if (!email.equals("")) {
	// Pageable pageable = PageRequest.of(page, size,
	// Sort.by("createdDate").descending());
	// logger.info("email: " + email);
	// List<FeedDTO> feedDTOList = feedService.findAllReadFeedsByEmail(email);
	// if (feedDTOList.size() > 0) {
	// return ResponseEntity.status(HttpStatus.OK)
	// .body(APIResponse.success(feedDTOList, "All feed get Successfully."));
	// } else {
	// return
	// ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Feed not
	// found."));
	// }

	// } else {
	// message = "Bad Request:User could not be blank.";
	// logger.error(message);
	// return
	// ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
	// }

	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.error("Calling findAllReadFeedsByEmail feed API failed , " +
	// e.toString());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
	// }

	// }

	@PostMapping(value = "/getById", produces = "application/json")
	public ResponseEntity<APIResponse<FeedDTO>> getFeedById(@RequestBody Feed feed) {
		try {
			logger.info("Calling getById Feed API...");
			String message = "";
			String feedId = GeneralUtility.makeNotNull(feed.getFeedId());
			logger.info("feedId: " + feedId);
			if (!GeneralUtility.makeNotNull(feedId).equals("")) {
				FeedDTO feedDTO = feedService.findByFeedId(feedId);
				if (GeneralUtility.makeNotNull(feedDTO.getFeedId()).equals(feedId)) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(APIResponse.success(feedDTO, "Feed get successfully."));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.error("Feed not found for Id: " + feedId));
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

	@PostMapping(value = "/updateReadStatusById", produces = "application/json")
	public ResponseEntity<APIResponse<FeedDTO>> updateReadStatusById(@RequestBody Feed feed) {
		try {
			logger.info("Calling updateReadStatusById Feed API...");
			String feedId = GeneralUtility.makeNotNull(feed.getFeedId());
			logger.info("feedId: " + feedId);
			String message = "";
			if (!GeneralUtility.makeNotNull(feedId).equals("")) {
				FeedDTO feedDTO = feedService.updateReadStatusById(feedId);

				if (GeneralUtility.makeNotNull(feedDTO.getFeedId()).equals(feedId)) {
					return ResponseEntity.status(HttpStatus.OK).body(
							APIResponse.success(feedDTO, "Read status updated successfully for FeedId."));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.error("Feed not found for Id: " + feedId));
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

	// @PostMapping(value = "/updateReadStatusByEmail", produces =
	// "application/json")
	// public ResponseEntity<APIResponse<List<FeedDTO>>>
	// updateReadStatusByEmail(@RequestBody UserRequest userReq,
	// @RequestParam(defaultValue = "0") int page,
	// @RequestParam(defaultValue = "500") int size) {
	// try {
	// logger.info("Calling updateReadStatusByEmail Feed API...");

	// String message = "";
	// logger.info("UserRequest: " + userReq);

	// String email = GeneralUtility.makeNotNull(userReq.getEmail()).trim();

	// if (!email.equals("")) {
	// Pageable pageable = PageRequest.of(page, size,
	// Sort.by("createdDate").descending());
	// logger.info("email: " + email);
	// List<FeedDTO> feedDTOList = feedService.updateReadStatusByEmail(email);
	// if (feedDTOList.size() > 0) {
	// return ResponseEntity.status(HttpStatus.OK).body(
	// APIResponse.success(feedDTOList, "Read status updated successfully."));
	// } else {
	// return ResponseEntity.status(HttpStatus.NOT_FOUND)
	// .body(APIResponse.error("Feed not found for user: " + email));
	// }

	// } else {
	// message = "Bad Request:User could not be blank.";
	// logger.error(message);
	// return
	// ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
	// }

	// } catch (Exception e) {
	// e.printStackTrace();
	// logger.error("Calling updateReadStatusByEmail feed API failed , " +
	// e.toString());
	// return
	// ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
	// }

	// }

}
