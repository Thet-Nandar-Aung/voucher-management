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
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.CampaignService;

@RestController
@Validated
@RequestMapping("/api/campaign")
public class CampaignController {
    	
    private static final Logger logger = LoggerFactory.getLogger(CampaignController.class);

    @Autowired
    private CampaignService campaignService;

    @GetMapping(value = "/getAll", produces = "application/json")
    public ResponseEntity<APIResponse<List<Campaign>>> getAllActiveStore() {
        logger.info("Calling Campaign getALL API...");
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(campaignService.findAllCampaigns(), null));
    }
    
    @PostMapping(value = "/create", produces = "application/json")
    public ResponseEntity<APIResponse<Campaign>> createCampaign(@RequestPart("campaign") Campaign campaign){
        logger.info("Calling Campaign create API...");
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(campaignService.create(campaign)));
    }

    @PostMapping(value = "/update", produces = "application/json")
    public ResponseEntity<APIResponse<Campaign>> updateCampaign(@RequestPart("campaign") Campaign campaign){
        logger.info("Calling Campaign update API...");
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(campaignService.update(campaign)));
    }

    @PostMapping(value = "/delete/{campaignId}", produces = "application/json")
    public ResponseEntity<APIResponse<Campaign>> deleteCampaign(@PathVariable String campaignId){
        logger.info("Calling Campaign delete API...");
        campaignService.delete(campaignId);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Deleted successfully"));
    }
}
  