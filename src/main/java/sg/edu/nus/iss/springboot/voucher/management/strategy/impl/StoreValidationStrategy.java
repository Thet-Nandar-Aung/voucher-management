package sg.edu.nus.iss.springboot.voucher.management.strategy.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.springboot.voucher.management.dto.StoreDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.ValidationResult;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.*;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.StoreService;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;
import sg.edu.nus.iss.springboot.voucher.management.strategy.IAPIHelperValidationStrategy;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;

@Service
public class StoreValidationStrategy implements IAPIHelperValidationStrategy<Store> {

	@Autowired
	private StoreService storeService;

	@Autowired
	private UserService userService;

	@Autowired
	private StoreRepository storeRepository;

	@Override
	public ValidationResult validateCreation(Store store, MultipartFile val) {
		ValidationResult validationResult = new ValidationResult();
		if (store.getCreatedBy() == null || store.getCreatedBy().getEmail() == null
				|| store.getCreatedBy().getEmail().isEmpty()) {
			validationResult.setMessage("Bad Request: Store Create user field could not be blank.");
			validationResult.setValid(false);
			return validationResult;
		}

		String userEmail = GeneralUtility.makeNotNull(store.getCreatedBy().getEmail());
		String validUserMsg = validateUser(userEmail);

		if (!validUserMsg.equals("")) {

			validationResult.setMessage(validUserMsg);
			validationResult.setValid(false);
			return validationResult;

		}

		if (store.getStoreName() == null || store.getStoreName().isEmpty()) {
			validationResult.setMessage("Bad Request: Store name could not be blank.");
			validationResult.setValid(false);
			return validationResult;

		}

		StoreDTO storeDTO = storeService.findByStoreName(store.getStoreName());
		try {
			if (GeneralUtility.makeNotNull(storeDTO.getStoreName()).equals(store.getStoreName())) {
				validationResult.setMessage("Store already exists.");
				validationResult.setValid(false);
				return validationResult;

			}
		} catch (Exception ex) {
			if (storeDTO != null) {

				validationResult.setMessage("Store already exists.");
				validationResult.setValid(false);
				return validationResult;

			}
		}
		validationResult.setValid(true);
		return validationResult;
	}

	@Override
	public ValidationResult validateUpdating(Store store, MultipartFile val) {
		ValidationResult validationResult = new ValidationResult();
		// Validate store ID
		String storeId = GeneralUtility.makeNotNull(store.getStoreId());
		if (storeId.isEmpty()) {
			validationResult.setMessage("Bad Request: Store ID could not be blank.");
			validationResult.setValid(false);
			return validationResult;

		}

		// Retrieve store from database
		Optional<Store> optionalStore = storeRepository.findById(storeId);
		if (optionalStore.isEmpty()) {
			validationResult.setMessage("Unable to find the store with ID: " + storeId);
			validationResult.setValid(false);
			return validationResult;
		}

		// Check for updated by user
		String updatedByEmail = GeneralUtility.makeNotNull(store.getUpdatedBy().getEmail());
		if (updatedByEmail.isEmpty()) {
			validationResult.setMessage("Bad Request: Store Update user field could not be blank.");
			validationResult.setValid(false);
			return validationResult;
		}

		String validUserMsg = validateUser(updatedByEmail);

		if (!validUserMsg.equals("")) {
			validationResult.setMessage(validUserMsg);
			validationResult.setValid(false);
			return validationResult;

		}
		validationResult.setValid(true);
		return validationResult;
	}

	private String validateUser(String userEmail) {
		//
		User user = userService.findByEmail(userEmail);
		if (user == null) {
			return "Store create failed: Invalid User: " + userEmail;
		}

		// Check user role
		String userRole = GeneralUtility.makeNotNull(user.getRole());
		if (!userRole.equals(RoleType.MERCHANT.toString())) {
			return "Bad Request: Invalid user role.";
		}
		return "";
	}

	@Override
	public ValidationResult validateObject(String data) {
		// TODO Auto-generated method stub
		return null;
	}

}
