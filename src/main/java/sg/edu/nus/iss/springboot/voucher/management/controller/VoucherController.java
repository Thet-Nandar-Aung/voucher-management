package sg.edu.nus.iss.springboot.voucher.management.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.springboot.voucher.management.dto.APIResponse;
import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
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
	public ResponseEntity<APIResponse<List<VoucherDTO>>> findAllClaimedVouchersByEmail(@RequestBody UserRequest user,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "500") int size) {

		String email = GeneralUtility.makeNotNull(user.getEmail()).trim();
		long totalRecord = 0;
		try {
			logger.info("Calling get Voucher by email API with page={}, size={}", page, size);

			if (!email.equals("")) {

				Pageable pageable = PageRequest.of(page, size, Sort.by("claimTime").ascending());
				Map<Long, List<VoucherDTO>> resultMap = voucherService.findAllClaimedVouchersByEmail(email, pageable);

				if (resultMap.size() == 0) {
					String message = "Voucher not found by email: " + email;
					logger.error(message);
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
				}

				List<VoucherDTO> voucherDTOList = new ArrayList<VoucherDTO>();

				for (Map.Entry<Long, List<VoucherDTO>> entry : resultMap.entrySet()) {
					totalRecord = entry.getKey();
					voucherDTOList = entry.getValue();

					logger.info("totalRecord: " + totalRecord);
					logger.info("voucherDTO List: " + voucherDTOList);

				}

				if (voucherDTOList.size() > 0) {
					return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(voucherDTOList,
							"Successfully get claimed vouchers by email: " + email, totalRecord));

				} else {
					String message = "Voucher not found by email: " + email;
					logger.error(message);
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
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
			@RequestBody Campaign campaign, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "500") int size) {

		String campaignId = GeneralUtility.makeNotNull(campaign.getCampaignId()).trim();
		long totalRecord = 0;
		try {
			logger.info("Calling get Voucher by campaignId API...");

			if (!campaignId.equals("")) {
				Pageable pageable = PageRequest.of(page, size, Sort.by("claimTime").ascending());
				Map<Long, List<VoucherDTO>> resultMap = voucherService.findAllClaimedVouchersByCampaignId(campaignId,
						pageable);

				if (resultMap.size() == 0) {
					String message = "Voucher not found by campaignId: " + campaignId;
					logger.error(message);
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
				}

				List<VoucherDTO> voucherDTOList = new ArrayList<VoucherDTO>();

				for (Map.Entry<Long, List<VoucherDTO>> entry : resultMap.entrySet()) {
					totalRecord = entry.getKey();
					voucherDTOList = entry.getValue();

					logger.info("totalRecord: " + totalRecord);
					logger.info("voucherDTO List: " + voucherDTOList);

				}

				if (voucherDTOList.size() > 0) {

					return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(voucherDTOList,
							"Successfully get claimed vouchers by campaignId: " + campaignId, totalRecord));
				} else {
					String message = "Voucher not found by campaignId: " + campaignId;
					logger.error(message);
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));

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

					List<VoucherDTO> voucherDTOList = voucherService.findByCampaignIdAndClaimedBy(voucher.getCampaign(),
							voucher.getClaimedBy());
					if (voucherDTOList.size() > 0) {
						logger.error("Calling Voucher create API failed...");
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body(APIResponse.error("Voucher already claimed."));
					} else {

						List<Voucher> claimedVoucherList = voucherService.findByCampaignCampaignId(campaignId);

						logger.info("claimedVoucherList: " + claimedVoucherList);
						if (dbCampaign.get().getNumberOfVouchers() > claimedVoucherList.size()) {
							VoucherDTO voucherDTO = voucherService.claim(voucher);
							if (voucherDTO != null) {
								return ResponseEntity.status(HttpStatus.OK).body(APIResponse
										.success(voucherService.claim(voucher), "Voucher claimed sucessfully."));
							} else {
								logger.error("Calling Voucher create API failed...");
								return ResponseEntity.status(HttpStatus.BAD_REQUEST)
										.body(APIResponse.error("Voucher claim failed."));
							}
						} else {
							return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
									.body(APIResponse.error("Campaign is fully claimed."));
						}

					} //

				} else {
					logger.error("Calling Voucher create API failed...");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(
							"Campaign status should be PROMOTED. Status:" + dbCampaign.get().getCampaignStatus()));
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
								.body(APIResponse.error("Voucher consumed failed."));
					}
				} else {
					logger.error("Calling Voucher consume API failed...");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body(APIResponse.error("Voucher already consumed."));
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
