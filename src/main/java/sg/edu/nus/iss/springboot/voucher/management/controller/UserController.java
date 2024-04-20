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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;

import sg.edu.nus.iss.springboot.voucher.management.configuration.VourcherManagementSecurityConfig;
import sg.edu.nus.iss.springboot.voucher.management.dto.*;
import sg.edu.nus.iss.springboot.voucher.management.dto.UserResponse.ResultItem;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.*;
import sg.edu.nus.iss.springboot.voucher.management.strategy.impl.UserValidationStrategy;
import sg.edu.nus.iss.springboot.voucher.management.utility.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private AmazonS3 s3Client;

	@Autowired
	private VourcherManagementSecurityConfig securityConfig;

	@Autowired
	private UserValidationStrategy userValidationStrategy;

	@GetMapping(value = "/getAll", produces = "application/json")
	public ResponseEntity<APIResponse<List<UserDTO>>> getAllUser(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "500") int size) {
		logger.info("Call user getAll API with page={}, size={}", page, size);
		long totalRecord = 0;

		try {

			Pageable pageable = PageRequest.of(page, size, Sort.by("username").ascending());
			Map<Long, List<UserDTO>> resultMap = userService.findByIsActiveTrue(pageable);

			if (resultMap.size() == 0) {
				String message = "User not found.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
			}

			List<UserDTO> userDTOList = new ArrayList<UserDTO>();

			for (Map.Entry<Long, List<UserDTO>> entry : resultMap.entrySet()) {
				totalRecord = entry.getKey();
				userDTOList = entry.getValue();

				logger.info("totalRecord: " + totalRecord);
				logger.info("userDTO List: " + userDTOList);

			}

			if (userDTOList.size() > 0) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(APIResponse.success(userDTOList, "Successfully get all active user.", totalRecord));

			} else {
				String message = "User not found.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(message));
			}

		} catch (Exception e) {
			logger.error("Error, " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(APIResponse.error("Error: " + e.getMessage()));
		}
	}

	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<UserResponse> createUser(@RequestPart("user") User user,
			@RequestPart(value = "image", required = false) MultipartFile uploadFile) {
		logger.info("Call user create API...");
		UserResponse userResponse = new UserResponse();
		ArrayList<ResultItem> resultList = new ArrayList<>();
		String message;

		try {
			ValidationResult validationResult = userValidationStrategy.validateCreation(user, uploadFile);
			if (validationResult.isValid()) {
				user.setImage(GeneralUtility.makeNotNull(validationResult.getImageUrl()));
				User createdUser = userService.create(user);
				if (createdUser != null) {
					message = "User created successfully.";
					logger.info(message + user.getEmail());
					ResultItem result = new ResultItem();
					result.setEmail(createdUser.getEmail());
					result.setUsername(createdUser.getUsername());
					result.setRole(createdUser.getRole());
					resultList.add(result);
					userResponse.setMessage(message);
					userResponse.setResult(resultList);
					return new ResponseEntity<>(userResponse, HttpStatus.OK);
				} else {
					message = "Create user failed: Unable to create a new user with email: " + user.getEmail();
					logger.error(message);
				}
			} else {
				message = validationResult.getMessage();
				logger.error(message);
			}
		} catch (Exception e) {
			logger.error("Error: " + e.toString());
			message = "Error: " + e.toString();
		}

		userResponse.setMessage(message);
		userResponse.setResult(resultList);
		return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(value = "/update", produces = "application/json", consumes = { "application/json",
			"multipart/form-data" })
	public ResponseEntity<UserResponse> updateUser(@RequestPart("user") User user,
			@RequestPart(value = "image", required = false) MultipartFile uploadFile) {
		logger.info("Call user update API...");
		UserResponse userResponse = new UserResponse();
		ArrayList<ResultItem> resultList = new ArrayList<>();
		String message;

		try {
			ValidationResult validationResult = userValidationStrategy.validateUpdating(user, uploadFile);
			if (validationResult.isValid()) {
				User dbUser = userService.findByEmail(user.getEmail());
				if (dbUser != null) {
					dbUser.setImage(GeneralUtility.makeNotNull(validationResult.getImageUrl()));
					dbUser.setUsername(user.getUsername());
					dbUser.setPassword(user.getPassword());
					dbUser.setRole(user.getRole());
					dbUser.setActive(user.isActive());
					dbUser.setImage(user.getImage());

					User updatedUser = userService.update(dbUser);
					if (updatedUser != null) {
						message = "User updated successfully.";
						logger.info(message + user.getEmail());
						ResultItem result = new ResultItem();
						result.setEmail(updatedUser.getEmail());
						result.setUsername(updatedUser.getUsername());
						result.setRole(updatedUser.getRole());
						resultList.add(result);
						userResponse.setMessage(message);
						userResponse.setResult(resultList);
						return new ResponseEntity<>(userResponse, HttpStatus.OK);
					} else {
						message = "Update user failed: Changes could not be applied to the user with email: "
								+ user.getEmail();
						logger.error(message);
					}
				} else {
					message = "User not found.";
					logger.error(message);
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} else {
				message = validationResult.getMessage();
				logger.error(message);
			}
		} catch (Exception e) {
			logger.error("Error: " + e.toString());
			message = "Error: " + e.toString();
		}

		userResponse.setMessage(message);
		userResponse.setResult(resultList);
		return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(value = "/resetPassword", produces = "application/json")
	public ResponseEntity<MessageResponse> resetPassword(@RequestBody UserRequest resetPwdReq) {

		logger.info("Call user resetPassword API...");

		logger.info("Reset Password : " + resetPwdReq.getEmail());

		MessageResponse response = new MessageResponse();
		String message = "";
		try {
			
			ValidationResult validationResult = userValidationStrategy.validateObject(resetPwdReq.getEmail());
			if (!validationResult.isValid()) {
				response.setMessage(validationResult.getMessage());
				return new ResponseEntity<>(response, validationResult.getStatus());
			}

				User dbUser = userService.findByEmailAndStatus(resetPwdReq.getEmail(), true, true);
				if (!GeneralUtility.makeNotNull(dbUser).equals("")) {

					dbUser.setPassword(resetPwdReq.getPassword());
					User modifiedUser = userService.update(dbUser);

					if (!GeneralUtility.makeNotNull(modifiedUser).equals("")) {
						message = "Reset Password Completed.";
						logger.info(message + resetPwdReq.getEmail());
						response.setMessage(message);
						return new ResponseEntity<>(response, HttpStatus.OK);
					} else {
						message = "Reset Password failed: Unable to reset password for the user with email :"
								+ resetPwdReq.getEmail();
						logger.error(message);
						response.setMessage(message);
						return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

					}
				} else {
					message = "Reset Password failed: Unable to find the user with email :" + resetPwdReq.getEmail();
					logger.error(message);
					response.setMessage(message);
					return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
				}
			
		} catch (Exception e) {
			logger.error("Error, " + e.toString());
			e.printStackTrace();
			response.setMessage("Error, " + e.toString());

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/login", produces = "application/json")
	public ResponseEntity<UserResponse> validateUserLogin(@RequestBody UserRequest userRequest) {
		logger.info("Call user login API...");
		UserResponse userResponse = new UserResponse();
		ArrayList<ResultItem> resultList = new ArrayList<>();
		String message = "";

		try {
			ValidationResult validationResult = userValidationStrategy.validateObject(userRequest.getEmail());
			if (!validationResult.isValid()) {
				userResponse.setMessage(validationResult.getMessage());
				return new ResponseEntity<>(userResponse, validationResult.getStatus());
			}

			User user = userService.validateUserLogin(userRequest.getEmail(), userRequest.getPassword());
			if (user != null) {
				String imageUrl = "";
				ResultItem userResp = new ResultItem();
				userResp.setEmail(user.getEmail());
				userResp.setUsername(user.getUsername());
				userResp.setRole(user.getRole());
				if (user.getImage() != null && !user.getImage().isEmpty()) {
					imageUrl = ImageUploadToS3.generatePresignedUrl(s3Client, securityConfig, user.getImage());
				}
				userResp.setImage(imageUrl);
				resultList.add(userResp);
				message = user.getEmail() + " login successfully";
				userResponse.setMessage(message);
				userResponse.setResult(resultList);
				return new ResponseEntity<>(userResponse, HttpStatus.OK);
			} else {
				message = "Invalid credentials.";
				userResponse.setMessage(message);
				return new ResponseEntity<>(userResponse, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			logger.error("Error: " + e.toString());
			userResponse.setMessage("Error: " + e.toString());
			return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value = "/verify", produces = "application/json")
	public ResponseEntity<APIResponse<UserDTO>> verifyUser(@RequestParam String verifyid) {
		verifyid = GeneralUtility.makeNotNull(verifyid);
		logger.info("Call user verify API with verifyToken={}", verifyid);

		String message = "";
		try {
			if (!verifyid.equals("")) {
				UserDTO userDTO = userService.verify(verifyid);
				if (userDTO != null) {
					message = "User successfully verified.";

					return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success(userDTO, message));

				} else {

					message = "Vefriy user failed: Verfiy Id is invalid or already verified.";
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));
				}
			} else {

				message = "Vefriy Id could not be blank.";
				logger.error(message);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error(message));
			}
		} catch (Exception e) {
			message = "Error: " + e.toString();
			logger.error(message);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.error(message));
		}

	}

}
