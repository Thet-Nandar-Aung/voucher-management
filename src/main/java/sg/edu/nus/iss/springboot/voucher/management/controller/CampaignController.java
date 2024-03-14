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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.springboot.voucher.management.dto.APIResponse;
import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.CampaignService;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;

@RestController
@Validated
@RequestMapping("/api/campaign")
public class CampaignController {

	private static final Logger logger = LoggerFactory.getLogger(CampaignController.class);

	@Autowired
	private CampaignService campaignService;

	@GetMapping(value = "/all/active", produces = "application/json")
	public ResponseEntity<APIResponse<List<CampaignDTO>>> getAllActiveCampaigns() {
		try {
			logger.info("Calling Campaign getAllActiveCampaigns API...");
			return ResponseEntity.status(HttpStatus.OK).body(APIResponse
					.success(campaignService.findAllActiveCampaigns(), "Successfully get all active campaigns"));
		} catch (Exception ex) {
			logger.info("Calling Campaign getAllActiveCampaigns API failed...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(APIResponse.error("Failed to get all active campaigns"));
		}
	}

	@GetMapping(value = "/store/{storeId}", produces = "application/json")
	public ResponseEntity<APIResponse<List<CampaignDTO>>> getAllCampaignsByStoreId(@PathVariable String storeId) {
		try {
			logger.info("Calling Campaign getAllCampaignsByStoreId API...");
			return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(
					campaignService.findAllCampaignsByStoreId(storeId), "Successfully get all active campaigns"));
		} catch (Exception ex) {
			logger.info("Calling Campaign getAllCampaignsByStoreId API failed...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(APIResponse.error("Failed to get all campaigns by store Id"));
		}
	}

	@GetMapping(value = "/user/email", produces = "application/json")
	public ResponseEntity<APIResponse<List<CampaignDTO>>> getAllCampaignsByEmail(@RequestParam("email") String email) {
		try {
			logger.info("Calling Campaign getAllCampaignsByEmail API...");
			return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(
					campaignService.findAllCampaignsByEmail(email), "Successfully get all campaigns by email"));
		} catch (Exception ex) {
			logger.info("Calling Campaign getAllCampaignsByEmail API failed...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(APIResponse.error("Failed to get all campaigns by email"));
		}
	}

	@GetMapping(value = "/campaign/{id}", produces = "application/json")
	public ResponseEntity<APIResponse<CampaignDTO>> getByCampaignId(@PathVariable String campaignId) {
		try {
			logger.info("Calling get Campaign API...");
			return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(
					campaignService.findByCampaignId(campaignId), "Successfully get campaignId " + campaignId));
		} catch (Exception ex) {
			logger.error("Calling Campaign get Campaign API failed...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(APIResponse.error("Failed to get campaignId " + campaignId));
		}

	}

	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<APIResponse<CampaignDTO>> createCampaign(@RequestPart("campaign") Campaign campaign) {
		try {
			logger.info("Calling Campaign create API...");
			return ResponseEntity.status(HttpStatus.OK)
					.body(APIResponse.success(campaignService.create(campaign), "Created sucessfully"));
		} catch (Exception ex) {
			logger.error("Calling Campaign create API failed...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error("Created failed"));
		}
	}

	@PostMapping(value = "/update", produces = "application/json")
	public ResponseEntity<APIResponse<CampaignDTO>> updateCampaign(@RequestPart("campaign") Campaign campaign) {
		try {
			logger.info("Calling Campaign update API...");
			return ResponseEntity.status(HttpStatus.OK)
					.body(APIResponse.success(campaignService.update(campaign), "Updated sucessfully"));
		} catch (Exception ex) {
			logger.info("Calling Campaign update API failed...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error("Updated failed"));
		}
	}

	@PostMapping(value = "/delete/{campaignId}", produces = "application/json")
	public ResponseEntity<APIResponse<Campaign>> deleteCampaign(@PathVariable String campaignId) {
		try {
			logger.info("Calling Campaign delete API...");
			campaignService.delete(campaignId);
			return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Deleted successfully"));
		} catch (Exception ex) {
			logger.error("Calling Campaign delete API failed...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error("Delete failed"));
		}
	}

	@PostMapping(value = "/promote/{campaignId}", produces = "application/json")
	public ResponseEntity<APIResponse<CampaignDTO>> promoteCampaign(@PathVariable String campaignId) {
		try {
			logger.info("Calling  Campaign Promote API...");
			String message = "";

			CampaignDTO campaignDTO = campaignService.promote(campaignId);

			if (GeneralUtility.makeNotNull(campaignDTO.getCampaignId()).equals(campaignId)) {

				return ResponseEntity.status(HttpStatus.OK)
						.body(APIResponse.success(campaignDTO, "Campaign promoted successfully"));

			} else {
				message = "Campaign Promote has failed: Campaign already promoted (or) Campaign Id is invalid";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Calling Promote Campaign API failed , " + e.toString());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(e.toString()));
		}

	}
}
