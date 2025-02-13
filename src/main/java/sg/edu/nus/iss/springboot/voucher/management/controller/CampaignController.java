package sg.edu.nus.iss.springboot.voucher.management.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.springboot.voucher.management.dto.*;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.CampaignService;
import sg.edu.nus.iss.springboot.voucher.management.strategy.impl.CampaignValidationStrategy;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;

@RestController
@Validated
@RequestMapping("/api/campaign")
public class CampaignController {

	private static final Logger logger = LoggerFactory.getLogger(CampaignController.class);

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private CampaignValidationStrategy campaignValidationStrategy;

	@GetMapping(value = "/all/active", produces = "application/json")
	public ResponseEntity<APIResponse<List<CampaignDTO>>> getAllActiveCampaigns(
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "500") int size) {
		logger.info("Calling Campaign getAllActiveCampaigns API with page={}, size={}", page, size);

		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").ascending());
			Map<Long, List<CampaignDTO>> resultMap = campaignService.findAllActiveCampaigns(pageable);

			if (resultMap.isEmpty()) {
				String message = "Campaign not found.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
			}

			long totalRecord = resultMap.keySet().stream().findFirst().orElse(0L);

			List<CampaignDTO> campaignDTOList = resultMap.getOrDefault(totalRecord, new ArrayList<>());

			logger.info("Total record: {}", totalRecord);
			logger.info("CampaignDTO List: {}", campaignDTOList);

			if (campaignDTOList.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK).body(
						APIResponse.success(campaignDTOList, "Successfully get all active campaigns.", totalRecord));

			} else {
				String message = "Campaign not found.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
			}

		} catch (Exception ex) {
			logger.error("An error occurred while processing getAllActiveCampaigns API.", ex);
			throw ex;
		}
	}

	@PostMapping(value = "/getAllByStoreId", produces = "application/json")
	public ResponseEntity<APIResponse<List<CampaignDTO>>> getAllCampaignsByStoreId(@RequestBody Store store,
			@RequestParam(defaultValue = "") String status, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "500") int size) {
		logger.info("Calling Campaign getAllCampaignsByStoreId API with page={}, size={}", page, size);

		try {
			String storeId = GeneralUtility.makeNotNull(store.getStoreId()).trim();
			if (storeId.isEmpty()) {
				logger.error("Bad Request: Store ID could not be blank.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(APIResponse.error("Bad Request: Store ID could not be blank."));
			}

			Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").ascending());
			Map<Long, List<CampaignDTO>> resultMap;

			if (status.isEmpty()) {
				resultMap = campaignService.findAllCampaignsByStoreId(storeId, pageable);
			} else {
				try {
					CampaignStatus campaignStatus = CampaignStatus.valueOf(status);
					resultMap = campaignService.findByStoreIdAndStatus(storeId, campaignStatus, pageable);
				} catch (IllegalArgumentException ex) {
					logger.error("Failed to get all campaigns by store Id. Campaign Status is invalid.", ex);
					throw ex;
				}
			}

			if (resultMap.isEmpty()) {
				String message = "Campaign not found by storeId: " + storeId;
				logger.error(message);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
			}

			long totalRecord = resultMap.keySet().stream().findFirst().orElse(0L);

			List<CampaignDTO> campaignDTOList = resultMap.getOrDefault(totalRecord, new ArrayList<>());

			logger.info("Total record: {}", totalRecord);
			logger.info("CampaignDTO List: {}", campaignDTOList);
			if (campaignDTOList.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK).body(
						APIResponse.success(campaignDTOList, "Successfully get all active campaigns", totalRecord));

			} else {
				String message = "Campaign not found by storeId: " + storeId;
				logger.error(message);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
			}

		} catch (Exception ex) {
			logger.error(
					"Calling Campaign getAllCampaignsByStoreId API failed. Failed to get all campaigns by store Id.",
					ex);
			throw ex;
		}
	}

	@PostMapping(value = "/getAllByEmail", produces = "application/json")
	public ResponseEntity<APIResponse<List<CampaignDTO>>> getAllCampaignsByEmail(@RequestBody UserRequest user,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "500") int size) {
		long totalRecord = 0;
		try {
			logger.info("Calling Campaign getAllCampaignsByEmail API with page={}, size={}", page, size);

			String email = GeneralUtility.makeNotNull(user.getEmail()).trim();

			if (!email.equals("")) {
				Pageable pageable = PageRequest.of(page, size, Sort.by("startDate").ascending());

				Map<Long, List<CampaignDTO>> resultMap = campaignService.findAllCampaignsByEmail(email, pageable);

				if (resultMap.size() == 0) {
					String message = "Campign not found.";
					logger.error(message);
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
				}

				List<CampaignDTO> campaignDTOList = new ArrayList<CampaignDTO>();

				for (Map.Entry<Long, List<CampaignDTO>> entry : resultMap.entrySet()) {
					totalRecord = entry.getKey();
					campaignDTOList = entry.getValue();

					logger.info("totalRecord: " + totalRecord);
					logger.info("CampaignDTO List: " + campaignDTOList);

				}

				if (campaignDTOList.size() > 0) {

					return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(campaignDTOList,
							"Successfully get all campaigns by email", totalRecord));

				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.error("Campaign not found by email: " + email));
				}
			} else {
				logger.error("Bad Request:Email could not be blank.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(APIResponse.error("Bad Request:Email could not be blank."));
			}

		} catch (Exception ex) {
			logger.info("Calling Campaign getAllCampaignsByEmail API failed...Failed to get all campaigns by email");
			throw ex;
		}
	}

	@PostMapping(value = "/getById", produces = "application/json")
	public ResponseEntity<APIResponse<CampaignDTO>> getByCampaignId(@RequestBody Campaign campaign) {
		try {
			logger.info("Calling get Campaign API...");

			String campaignId = GeneralUtility.makeNotNull(campaign.getCampaignId()).trim();

			if (!campaignId.equals("")) {

				CampaignDTO campaignDTO = campaignService.findByCampaignId(campaignId);

				if (campaignDTO.getCampaignId().equals(campaignId)) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(APIResponse.success(campaignDTO, "Successfully get campaignId " + campaignId));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.error("Campaign not found by campaignId: " + campaignId));

				}

			} else {
				logger.error("Bad Request:Campaign ID could not be blank.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(APIResponse.error("Bad Request:CampaignId could not be blank."));
			}

		} catch (Exception ex) {
			logger.error("Calling Campaign get Campaign API failed...");
			throw ex;
		}

	}

	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<APIResponse<CampaignDTO>> createCampaign(@RequestBody Campaign campaign) {
		try {
			logger.info("Calling Campaign create API...");
			String message = "";
			ValidationResult validationResult = campaignValidationStrategy.validateCreation(campaign, null);
			if (validationResult.isValid()) {

				CampaignDTO campaignDTO = campaignService.create(campaign);
				if (campaignDTO != null && !campaignDTO.getCampaignId().isEmpty()) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(APIResponse.success(campaignDTO, "Created successfully"));
				} else {
					logger.error("Failed to create campaign.");
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(APIResponse.error("Create Campaign failed."));
				}

			} else {
				message = validationResult.getMessage();
				logger.error(message);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));
			}

		} catch (Exception ex) {
			logger.error("An error occurred while processing createCampaign API.", ex);
			throw ex;
		}
	}

	@PostMapping(value = "/update", produces = "application/json")
	public ResponseEntity<APIResponse<CampaignDTO>> updateCampaign(@RequestBody Campaign campaign) {
		try {
			logger.info("Calling Campaign update API...");
			String message = "";
			ValidationResult validationResult = campaignValidationStrategy.validateUpdating(campaign, null);
			if (validationResult.isValid()) {
				CampaignDTO campaignDTO = campaignService.update(campaign);
				if (campaignDTO != null && !campaignDTO.getCampaignId().isEmpty()) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(APIResponse.success(campaignDTO, "Updated sucessfully"));
				} else {
					logger.error("Calling Campaign create API failed...");
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
							APIResponse.error("Update Campaign failed:  campaignId: " + campaign.getCampaignId()));
				}

			} else {
				message = validationResult.getMessage();
				logger.error(message);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));
			}

		} catch (Exception ex) {
			logger.info("Calling Campaign update API failed..." + ex.toString());
			throw ex;
		}
	}
/*
	@PostMapping(value = "/delete", produces = "application/json")

	public ResponseEntity<APIResponse<Campaign>> deleteCampaign(@RequestBody Campaign campaign) {
		try {

			logger.info("Calling Campaign delete API...");
			String campaignId = GeneralUtility.makeNotNull(campaign.getCampaignId()).trim();
			if (!GeneralUtility.makeNotNull(campaignId).equals("")) {

				Optional<Campaign> dbCampaign = campaignService.findById(campaign.getCampaignId());

				if (dbCampaign.isPresent()) {
					if (dbCampaign.get().getCampaignStatus().equals(CampaignStatus.CREATED)) {
						dbCampaign.get().setUpdatedBy(campaign.getUpdatedBy());
						boolean isDeleted = campaignService.delete(dbCampaign.get());
						if (isDeleted) {
							return ResponseEntity.status(HttpStatus.OK)
									.body(APIResponse.success("Deleted successfully"));
						} else {
							return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse
									.error("Delete Campaign failed:  campaignId: " + campaign.getCampaignId()));
						}

					} else {
						logger.error("Calling Campaign delete API failed...");
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(
								"Campaign status is invalid to delete: " + dbCampaign.get().getCampaignStatus()));
					}
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.error("Campaign not found by campaignId: " + campaignId));

				}
			}

			else {
				logger.error("Calling Campaign delete API failed...");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(APIResponse.error("Campaign Id could not be blank."));
			}

		} catch (Exception ex) {

			logger.error("Calling Campaign delete API failed...");

			throw ex;

		}

	}*/

	@PostMapping(value = "/promote", produces = "application/json")
	public ResponseEntity<APIResponse<CampaignDTO>> promoteCampaign(@RequestBody Campaign campaign) {
		try {
			logger.info("Calling Campaign Promote API...");

			ValidationResult validationResult = campaignValidationStrategy.validateUpdating(campaign, null);
			if (!validationResult.isValid()) {
				String message = validationResult.getMessage();
				logger.error(message);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));
			}

			validationResult = campaignValidationStrategy.validateObject(campaign.getCampaignId());
			if (!validationResult.isValid()) {
				String message = validationResult.getMessage();
				logger.error(message);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));
			}

			CampaignDTO campaignDTO = campaignService.promote(campaign);
			if (campaignDTO != null && !campaignDTO.getCampaignId().isEmpty()) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(APIResponse.success(campaignDTO, "Campaign promoted successfully"));
			} else {
				String message = "Campaign Promotion has failed.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));
			}

		} catch (Exception ex) {
			logger.error("Calling Promote Campaign API failed", ex);
			throw ex;
		}
	}

}
