package sg.edu.nus.iss.springboot.voucher.management.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.springboot.voucher.management.dto.APIResponse;
import sg.edu.nus.iss.springboot.voucher.management.dto.UserRequest;
import sg.edu.nus.iss.springboot.voucher.management.dto.VoucherDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.enums.VoucherStatus;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.CampaignService;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.VoucherService;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;

@RestController
@Validated
@RequestMapping("/api/voucher")
public class VoucherController {

	private static final Logger logger = LoggerFactory.getLogger(VoucherController.class);

	@Autowired
	private VoucherService voucherService;

	@Autowired
	private CampaignService campaignService;

	@PostMapping(value = "/getByEmail", produces = "application/json")
	public ResponseEntity<APIResponse<List<VoucherDTO>>> findAllClaimedVouchersByEmail(@RequestBody UserRequest user) {

		String email = GeneralUtility.makeNotNull(user.getEmail()).trim();
		try {
			logger.info("Calling get Voucher by email API...");

			if (!email.equals("")) {

				List<VoucherDTO> voucherList = voucherService.findAllClaimedVouchersByEmail(email);

				if (voucherList.size() > 0) {

					return ResponseEntity.status(HttpStatus.OK).body(
							APIResponse.success(voucherList, "Successfully get claimed vouchers by email: " + email));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.error("Voucher not found by email: " + email));
				}

			} else {
				logger.error("Bad Request:Email could not be blank.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(APIResponse.error("Bad Request:Email could not be blank."));
			}
		} catch (Exception ex) {
			logger.error("Calling Voucher get Voucher by email API failed...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(APIResponse.error("Failed to get voucher for email " + email));
		}

	}

	@PostMapping(value = "/getByCampaignId", produces = "application/json")
	public ResponseEntity<APIResponse<List<VoucherDTO>>> findAllClaimedVouchersBycampaignId(
			@RequestBody Campaign campaign) {

		String campaignId = GeneralUtility.makeNotNull(campaign.getCampaignId()).trim();

		try {
			logger.info("Calling get Voucher by campaignId API...");

			if (!campaignId.equals("")) {

				List<VoucherDTO> voucherList = voucherService.findAllClaimedVouchersByCampaignId(campaignId);

				if (voucherList.size() > 0) {

					return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(voucherList,
							"Successfully get claimed vouchers by campaignId: " + campaignId));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.error("Voucher not found by campaignId: " + campaignId));

				}
			} else {
				logger.error("Bad Request:Campaign ID could not be blank.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(APIResponse.error("Bad Request:CampaignId could not be blank."));
			}

		} catch (Exception ex) {
			logger.error("Calling Voucher get Voucher by campaignId API failed...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(APIResponse.error("Failed to get voucher for campaignId " + campaignId));
		}

	}

	@PostMapping(value = "/getById", produces = "application/json")
	public ResponseEntity<APIResponse<VoucherDTO>> getByVoucherId(@RequestBody Voucher voucher) {
		String voucherId = GeneralUtility.makeNotNull(voucher.getVoucherId()).trim();
		try {
			logger.info("Calling get Voucher API...");

			if (!voucherId.equals("")) {

				VoucherDTO voucherDTO = voucherService.findByVoucherId(voucherId);
				if (voucherDTO.getVoucherId().equals(voucherId)) {

					return ResponseEntity.status(HttpStatus.OK)
							.body(APIResponse.success(voucherDTO, "Successfully get voucherId " + voucherId));
				} else {
					return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(APIResponse.error("Voucher not found by voucherId: " + voucherId));

				}
			} else {
				logger.error("Bad Request:Voucher ID could not be blank.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(APIResponse.error("Bad Request:Voucher could not be blank."));
			}

		} catch (Exception ex) {
			logger.error("Calling Voucher get Voucher API failed...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(APIResponse.error("Failed to get voucherId " + voucherId));
		}

	}

	@PostMapping(value = "/claim", produces = "application/json")
	public ResponseEntity<APIResponse<VoucherDTO>> createVoucher(@RequestBody Voucher voucher) {
		try {
			logger.info("Calling Voucher create API...");
			String campaignId = GeneralUtility.makeNotNull(voucher.getCampaign().getCampaignId()).trim();
			Optional<Campaign> dbCampaign = campaignService.findById(campaignId);
			if (dbCampaign.isPresent()) {

				if (dbCampaign.get().getCampaignStatus().equals(CampaignStatus.PROMOTED)) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(APIResponse.success(voucherService.claim(voucher), "Voucher claimed sucessfully."));
				} else {
					logger.error("Calling Voucher create API failed...");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse
							.error("Campaign status is invalid to claim. Status:" + dbCampaign.get().getCampaignStatus()));
				}
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(APIResponse.error("Campaign not found by campaignId: " + campaignId));

			}

		} catch (Exception ex) {
			logger.error("Calling Voucher create API failed...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error("Voucher claim failed."));
		}
	}

	@PostMapping(value = "/consume", produces = "application/json")
	public ResponseEntity<APIResponse<VoucherDTO>> updateVoucher(@RequestBody Voucher voucher) {
		String voucherId = GeneralUtility.makeNotNull(voucher.getVoucherId()).trim();
		try {
			logger.info("Calling Voucher consume API...");

			VoucherDTO voucherDTO = voucherService.findByVoucherId(voucherId);

			if (voucherDTO != null) {
				if (voucherDTO.getVoucherStatus().equals(VoucherStatus.CLAIMED)) {

					VoucherDTO updateVoucherDTO = voucherService.consume(voucher);

					if (updateVoucherDTO.getVoucherStatus().equals(VoucherStatus.CONSUMED)) {

						return ResponseEntity.status(HttpStatus.OK)
								.body(APIResponse.success(updateVoucherDTO, "Voucher consumed sucessfully."));
					} else {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								.body(APIResponse.error("Voucher consumed failed." ));
					}
				} else {
					logger.error("Calling Voucher consume API failed...");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse
							.error("Voucher has been already consumed."));
				}

			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(APIResponse.error("Voucher not found. Id: " + voucherId));
			}

		} catch (Exception ex) {
			logger.info("Calling Voucher consume API failed...");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error("Voucher consumed failed."));
		}
	}

}
