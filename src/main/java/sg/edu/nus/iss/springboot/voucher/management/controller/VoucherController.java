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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.springboot.voucher.management.dto.APIResponse;
import sg.edu.nus.iss.springboot.voucher.management.dto.VoucherDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.VoucherService;

@RestController
@Validated
@RequestMapping("/api/voucher")
public class VoucherController {

    private static final Logger logger = LoggerFactory.getLogger(VoucherController.class);

    @Autowired
    private VoucherService voucherService;

    @GetMapping(value = "/getByEmail/{email}", produces = "application/json")
    public ResponseEntity<APIResponse<List<VoucherDTO>>> findAllClaimedVouchersByEmail(@PathVariable String email) {
        try {
            logger.info("Calling get Voucher by email API...");
            return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success(voucherService.findAllClaimedVouchersByEmail(email), "Successfully get claimed vouchers by email: " + email));
        } catch (Exception ex) {
            logger.error("Calling Voucher get Voucher by email API failed...");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.error("Failed to get voucher for email "+ email));
        }
        
    }

    @GetMapping(value = "/getByCampaignId/{campaignId}", produces = "application/json")
    public ResponseEntity<APIResponse<List<VoucherDTO>>> findAllClaimedVouchersBycampaignId(@PathVariable String campaignId) {
        try {
            logger.info("Calling get Voucher by campaignId API...");
            return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success(voucherService.findAllClaimedVouchersByCampaignId(campaignId), "Successfully get claimed vouchers by campaignId: " + campaignId));
        } catch (Exception ex) {
            logger.error("Calling Voucher get Voucher by campaignId API failed...");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.error("Failed to get voucher for campaignId "+ campaignId));
        }
        
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<APIResponse<VoucherDTO>> getByVoucherId(@PathVariable String voucherId) {
        try {
            logger.info("Calling get Voucher API...");
            return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success(voucherService.findByVoucherId(voucherId), "Successfully get voucherId "+ voucherId));
        } catch (Exception ex) {
            logger.error("Calling Voucher get Voucher API failed...");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.error("Failed to get voucherId "+ voucherId));
        }
        
    }

    @PostMapping(value = "/claim", produces = "application/json")
    public ResponseEntity<APIResponse<VoucherDTO>> createVoucher(@RequestPart("voucher") Voucher voucher) {
        try {
            logger.info("Calling Voucher create API...");
            return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(voucherService.claim(voucher), "Voucher claimed sucessfully."));
        } catch (Exception ex) {
            logger.error("Calling Voucher create API failed...");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error("Voucher claim failed."));
        }
    }

    @PostMapping(value = "/consume", produces = "application/json")
    public ResponseEntity<APIResponse<VoucherDTO>> updateVoucher(@RequestPart("voucher") Voucher voucher) {
        try {
            logger.info("Calling Voucher update API...");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(APIResponse.success(voucherService.consume(voucher), "Voucher consumed sucessfully."));
        } catch (Exception ex) {
            logger.info("Calling Voucher update API failed...");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.error("Voucher consumed failed."));
        }
    }

}
