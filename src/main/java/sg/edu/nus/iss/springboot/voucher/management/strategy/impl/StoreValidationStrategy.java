package sg.edu.nus.iss.springboot.voucher.management.strategy.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.dto.StoreDTO;
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
	public String validateCreation(Store store) {
		
		if (store.getCreatedBy() == null || store.getCreatedBy().getEmail() == null
				|| store.getCreatedBy().getEmail().isEmpty()) {
			return "Bad Request: Store Create user field could not be blank.";
		}
		
		String userEmail = GeneralUtility.makeNotNull(store.getCreatedBy().getEmail());
		String validUserMsg = validateUser(userEmail);
		
		if(!validUserMsg.equals("")) {
			return validUserMsg;
		}
		

		if (store.getStoreName() == null || store.getStoreName().isEmpty()) {
			return "Bad Request: Store name could not be blank.";
		}

		StoreDTO storeDTO = storeService.findByStoreName(store.getStoreName());
		if (storeDTO == null) {
			return "Store already exists.";
		}

		return "";
	}


	@Override
	public String validateUpdating(Store store) {
		
		// Validate store ID
		String storeId = GeneralUtility.makeNotNull(store.getStoreId());
		if (storeId.isEmpty()) {
			return "Bad Request: Store ID could not be blank.";
		}

		// Retrieve store from database
		Optional<Store> optionalStore = storeRepository.findById(storeId);
		if (optionalStore.isEmpty()) {
			return "Unable to find the store with ID: " + storeId;
		}

		// Check for updated by user
		String updatedByEmail = GeneralUtility.makeNotNull(store.getUpdatedBy().getEmail());
		if (updatedByEmail.isEmpty()) {
			return "Bad Request: Store Update user field could not be blank.";
		}
		

		String validUserMsg = validateUser(updatedByEmail);
		
		if(!validUserMsg.equals("")) {
			return validUserMsg;
		}

		return "";
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

}
