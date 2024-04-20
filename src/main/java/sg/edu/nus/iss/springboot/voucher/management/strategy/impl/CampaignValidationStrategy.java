package sg.edu.nus.iss.springboot.voucher.management.strategy.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.springboot.voucher.management.dto.StoreDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.ValidationResult;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.CampaignService;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.StoreService;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;
import sg.edu.nus.iss.springboot.voucher.management.strategy.IAPIHelperValidationStrategy;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;

@Service
public class CampaignValidationStrategy implements IAPIHelperValidationStrategy<Campaign> {

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private UserService userService;

	@Override
	public ValidationResult validateCreation(Campaign campaign, MultipartFile val) {
		ValidationResult validationResult = new ValidationResult();
		if (campaign.getDescription() == null || campaign.getDescription().isEmpty()) {
			validationResult.setMessage("Description cannot be empty.");
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		if (campaign.getStore() == null || campaign.getStore().getStoreId().isEmpty()) {
			validationResult.setMessage("Store Id cannot be empty.");
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		if (campaign.getCreatedBy() == null || campaign.getCreatedBy().getEmail().isEmpty()) {
			validationResult.setMessage("CreatedBy cannot be empty.");
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		List<Campaign> dbCampaignList = campaignService.findByDescription(campaign.getDescription().trim());
		if (!dbCampaignList.isEmpty()) {
			validationResult.setMessage("Campaign already exists: " + campaign.getDescription());
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		StoreDTO storeDTO = storeService.findByStoreId(campaign.getStore().getStoreId());

		if (storeDTO == null || storeDTO.getStoreId() == null || storeDTO.getStoreId().isEmpty()) {
			validationResult.setMessage("Invalid store Id: " + campaign.getStore().getStoreId());
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		User user = userService.findByEmailAndStatus(campaign.getCreatedBy().getEmail(), true, true);

		if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
			validationResult.setMessage("Invalid User : " + campaign.getCreatedBy().getEmail());
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		validationResult.setValid(true);
		return validationResult;

	}

	@Override
	public ValidationResult validateUpdating(Campaign campaign, MultipartFile val) {
		ValidationResult validationResult = new ValidationResult();
		String campaignId = GeneralUtility.makeNotNull(campaign.getCampaignId()).trim();
		if (campaignId == null || campaignId.isEmpty()) {

			validationResult.setMessage("Campaign ID can not be blank.");
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		User user = userService.findByEmailAndStatus(campaign.getUpdatedBy().getEmail(), true, true);

		if (user == null || user.getEmail() == null || user.getEmail().isEmpty()) {
			validationResult.setMessage("Invalid User : " + campaign.getUpdatedBy().getEmail());
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		Optional<Campaign> dbCampaign = campaignService.findById(campaignId);
		if (dbCampaign.isEmpty()) {
			validationResult.setMessage("Campaign not found by campaignId: " + campaignId);
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		} else {
			if (!dbCampaign.get().getCampaignStatus().equals(CampaignStatus.CREATED)) {
				validationResult.setMessage(
						"Campaign status has to be CREATED.Requested status :" + dbCampaign.get().getCampaignStatus());
				validationResult.setStatus(HttpStatus.BAD_REQUEST);
				validationResult.setValid(false);
				return validationResult;
			}
		}

		validationResult.setValid(true);
		return validationResult;
	}

	@Override
	public ValidationResult validateObject(String campaignId) {
		ValidationResult validationResult = new ValidationResult();
		Optional<Campaign> dbCampaign = campaignService.findById(campaignId);
		LocalDateTime startDate = dbCampaign.get().getStartDate();
		LocalDateTime endDate = dbCampaign.get().getEndDate();

		if (startDate.isBefore(LocalDateTime.now()) && endDate.isBefore(LocalDateTime.now())) {
			validationResult.setMessage("StartDate " + startDate + " should not be less than current date ");
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		if (endDate.isBefore(LocalDateTime.now())) {
			validationResult.setMessage("EndDate " + endDate + " should not be less than current date ");
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		if (dbCampaign.get().getCampaignStatus().equals(CampaignStatus.PROMOTED)) {
			validationResult.setMessage("Campaign already promoted.");
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		validationResult.setValid(true);
		return validationResult;
	}

}
