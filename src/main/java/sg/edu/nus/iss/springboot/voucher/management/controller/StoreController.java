package sg.edu.nus.iss.springboot.voucher.management.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.*;
import sg.edu.nus.iss.springboot.voucher.management.dto.*;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.enums.*;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.*;
import sg.edu.nus.iss.springboot.voucher.management.strategy.impl.*;
import sg.edu.nus.iss.springboot.voucher.management.utility.*;

@RestController
@Validated
@RequestMapping("/api/store")
public class StoreController {

	private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

	@Autowired
	private StoreService storeService;

	@Autowired
	private UserService userService;

	@Autowired
	private StoreValidationStrategy storeValidationStrategy;

	@GetMapping(value = "/getAll", produces = "application/json")
	public ResponseEntity<APIResponse<List<StoreDTO>>> getAllActiveStore(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "200") int size) {
		logger.info("Call store getAll API with page={}, size={}", page, size);

		long totalRecord = 0;
		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by("storeName").ascending());
			Map<Long, List<StoreDTO>> resultMap = storeService.findByIsDeletedFalse(pageable);

			if (resultMap.size() == 0) {
				String message = "No Store found.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
			}

			List<StoreDTO> storeDTOList = new ArrayList<StoreDTO>();
			for (Map.Entry<Long, List<StoreDTO>> entry : resultMap.entrySet()) {
				totalRecord = entry.getKey();
				storeDTOList = entry.getValue();

				logger.info("totalRecord: " + totalRecord);
				logger.info("StoreDTO List: " + storeDTOList);

			}
			return ResponseEntity.status(HttpStatus.OK)
					.body(APIResponse.success(storeDTOList, "Successfully get all active store.", totalRecord));

		} catch (Exception e) {
			logger.error("Error: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(APIResponse.error("Error: " + e.getMessage()));
		}

	}

	@PostMapping(value = "/getAllByUser", produces = "application/json")
	public ResponseEntity<APIResponse<List<StoreDTO>>> getAllStoreByUser(@RequestBody UserRequest userReq,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "200") int size) {

		logger.info("Call store getAllByUser API with page={}, size={}", page, size);
		long totalRecord = 0;
		String message = "";

		try {
			logger.info("UserRequest: " + userReq);

			String email = GeneralUtility.makeNotNull(userReq.getEmail()).trim();

			if (!email.equals("")) {
				logger.info("email: " + email);
				User user = userService.findByEmail(email);

				if (user != null) {
					String userRole = GeneralUtility.makeNotNull(user.getRole()).trim();

					logger.info("user role: " + userRole);

					if (userRole.equals(RoleType.MERCHANT.toString())) {
						Pageable pageable = PageRequest.of(page, size, Sort.by("storeName").ascending());
						Map<Long, List<StoreDTO>> resultMap = storeService.findAllByUserAndStatus(user, false,
								pageable);

						if (resultMap.size() == 0) {
							message = "No Store found.";
							logger.error(message);
							return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
						}

						List<StoreDTO> storeDTOList = new ArrayList<StoreDTO>();
						for (Map.Entry<Long, List<StoreDTO>> entry : resultMap.entrySet()) {
							totalRecord = entry.getKey();
							storeDTOList = entry.getValue();

							logger.info("totalRecord: " + totalRecord);
							logger.info("StoreDTO List: " + storeDTOList);

						}

						return ResponseEntity.status(HttpStatus.OK).body(
								APIResponse.success(storeDTOList, "Successfully get all active store.", totalRecord));

					} else {
						message = "Invalid user role.";
						logger.error(message);
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
					}
				} else {
					message = "Store retrieving failed: Invalid User :" + email;
					logger.error(message);
					return ResponseEntity.notFound().build();
				}
			} else {
				message = "User email cannot be blank.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
			}
		} catch (Exception e) {
			message = "Error: " + e.toString();
			logger.error(message);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));

		}
	}

	@PostMapping(value = "/getById", produces = "application/json")
	public ResponseEntity<APIResponse<StoreDTO>> getStoreById(@RequestBody Store store) {
		logger.info("Call store getStoreById API...");

		String message = "";
		StoreDTO storeDTO = new StoreDTO();

		try {
			String storeId = GeneralUtility.makeNotNull(store.getStoreId()).trim();
			logger.info("storeId: " + storeId);

			if (storeId.isEmpty()) {
				message = "Bad Request: Store Id could not be blank.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
			}

			storeDTO = storeService.findByStoreId(storeId);

			if (storeDTO ==null) {
				message = "Unable to find the store with id: " + storeId;
				logger.error(message);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
			}

			return ResponseEntity.status(HttpStatus.OK)
					.body(APIResponse.success(storeDTO, "Successfully get store Id: " + storeId));

		}

		catch (Exception e) {
			message = "Error: " + e.toString();
			logger.error(message);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));

		}

	}

	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<APIResponse<StoreDTO>> createStore(@RequestPart("store") Store store,
			@RequestPart(value = "image", required = false) MultipartFile uploadFile) {
		logger.info("Call store create API...");
		String message = "";
		StoreDTO storeDTO = new StoreDTO();
		try {

			message = storeValidationStrategy.validateCreation(store);
			if (!message.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));

			}

			storeDTO = storeService.create(store, uploadFile);
			if (!GeneralUtility.makeNotNull(storeDTO.getStoreId()).equals("")) {
				message = "Store created successfully.";

				return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(storeDTO, message));

			} else {
				message = "Create store failed: Unable to create a new store.";
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));
			}
		} catch (Exception e) {
			message = "Error: " + e.toString();
			logger.error(message);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));
		}
	}

	@PostMapping(value = "/update", produces = "application/json")
	public ResponseEntity<APIResponse<StoreDTO>> updateStore(@RequestPart("store") Store store,
			@RequestPart(value = "image", required = false) MultipartFile uploadFile) {
		logger.info("Call store create API...");
		String message = "";
		StoreDTO storeDTO = new StoreDTO();
		try {

			message = storeValidationStrategy.validateUpdating(store);
			if (!message.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));

			}

			storeDTO = storeService.update(store, uploadFile);
			if (!GeneralUtility.makeNotNull(storeDTO.getStoreId()).equals("")) {
				message = "Store updated successfully.";

				return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(storeDTO, message));

			} else {
				message = "Update store failed: Changes could not be applied to the store :" + store.getStoreName();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));
			}
		} catch (Exception e) {
			message = "Error: " + e.toString();
			logger.error(message);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));
		}
	}

}
