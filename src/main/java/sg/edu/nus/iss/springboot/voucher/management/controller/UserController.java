package sg.edu.nus.iss.springboot.voucher.management.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;

import sg.edu.nus.iss.springboot.voucher.management.configuration.VourcherManagementSecurityConfig;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.model.*;
import sg.edu.nus.iss.springboot.voucher.management.model.UserResponse.ResultItem;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.*;
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

	@GetMapping(value = "/getAll", produces = "application/json")
	public ResponseEntity<UserResponse> getAllUser() {
		logger.info("Call user getAll API...");
		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();

		UserResponse userResponse = new UserResponse();
		try {
			List<User> userList = userService.findByIsActiveTrue();

			if (userList.isEmpty()) {
				userResponse.setMessage("No User found.");
				userResponse.setResult(resultList);
				return new ResponseEntity<>(userResponse, HttpStatus.OK);
			} else {
				userResponse.setMessage(HttpStatus.OK + "");
				ImageUploadToS3 imgUpload = new ImageUploadToS3();

				for (User user : userList) {
					ResultItem userResp = new ResultItem();
					userResp.setEmail(user.getEmail());
					userResp.setUsername(user.getUsername());
					userResp.setRole(user.getRole());
					String imageUrl = GeneralUtility.makeNotNull(user.getImage());
					if (!imageUrl.equals("")) {

						String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

						boolean isImageExists = s3Client.doesObjectExist(securityConfig.getS3Bucket(),
								securityConfig.getS3ImagePrivateUsers().trim() + fileName.trim());

						if (isImageExists) {

							String presignedUrl = GeneralUtility
									.makeNotNull(imgUpload.generatePresignedUrl(s3Client, securityConfig, securityConfig.getS3ImagePrivateUsers().trim() + fileName.trim()));

							userResp.setImage(presignedUrl);
						}
					} else {
						userResp.setImage("");
					}
					resultList.add(userResp);
				}

				userResponse.setResult(resultList);

				return new ResponseEntity<>(userResponse, HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.error("Error, " + e.toString());
			userResponse.setMessage("Error," + e.toString());
			userResponse.setResult(resultList);
			return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<UserResponse> createUser(@RequestPart("user") User user,
			@RequestPart(value = "image", required = false) MultipartFile uploadFile) {
		logger.info("Call user create API...");

		String message = "";

		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();

		UserResponse userResponse = new UserResponse();
		ResultItem result = new ResultItem();
		try {
			if (!GeneralUtility.makeNotNull(user.getEmail()).equals("")) {
				User dbUser = userService.findByEmail(user.getEmail());
				String presignedUrl = "";
				if (GeneralUtility.makeNotNull(dbUser).equals("")) {

					// if User not found, will create

					if (!GeneralUtility.makeNotNull(uploadFile).equals("")) {
						logger.info("create user: " + user.getEmail() + "::" + uploadFile.getOriginalFilename());
						ImageUploadToS3 imgUpload = new ImageUploadToS3();
						presignedUrl = GeneralUtility.makeNotNull(
								imgUpload.generatePresignedUrlAndUploadObject(s3Client, securityConfig, uploadFile,securityConfig.getS3ImagePrivateUsers().trim()));
						if (!presignedUrl.equals("")) {
							String imageUrl = securityConfig.getS3ImageUrlPrefix().trim() + "/"+securityConfig.getS3ImagePrivateUsers().trim()
									+ uploadFile.getOriginalFilename().trim();
							user.setImage(imageUrl);
						}

					}

					User createdUser = userService.create(user);
					if (!GeneralUtility.makeNotNull(createdUser).equals("")) {

						message = "User created successfully.";
						logger.info(message + user.getEmail());
						result.setEmail(createdUser.getEmail());
						result.setUsername(createdUser.getUsername());
						result.setRole(createdUser.getRole());
						result.setImage(presignedUrl);
						resultList.add(result);
						userResponse.setMessage(message);
						userResponse.setResult(resultList);

						return new ResponseEntity<>(userResponse, HttpStatus.OK);

					} else {
						message = "Create user failed: Unable to create a new user account with email :"
								+ user.getEmail();
						logger.error(message);
						resultList.add(result);
						userResponse.setMessage(message);
						userResponse.setResult(new ArrayList<ResultItem>());
						return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
					}

				} else {

					message = "User already exists.";
					logger.error(message);
					userResponse.setMessage(message);
					userResponse.setResult(resultList);
					return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
				}
			} else {
				message = "Bad Request.";
				logger.error(message);
				userResponse.setMessage(message);
				userResponse.setResult(resultList);
				return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error, " + e.toString());
			userResponse.setMessage("Error, " + e.toString());
			userResponse.setResult(resultList);
			return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/update", produces = "application/json", consumes = { "application/json",
			"multipart/form-data" })
	public ResponseEntity<UserResponse> updateUser(@RequestPart("user") User user,
			@RequestPart(value = "image", required = false) MultipartFile uploadFile) {

		logger.info("Call user update API...");

		String message = "";

		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();

		UserResponse userResponse = new UserResponse();
		ResultItem result = new ResultItem();
		try {
			if (!GeneralUtility.makeNotNull(user.getEmail()).equals("")) {
				User dbUser = userService.findByEmail(user.getEmail());
				String presignedUrl = "";

				if (!GeneralUtility.makeNotNull(dbUser).equals("")) {

					if (!GeneralUtility.makeNotNull(uploadFile).equals("")) {

						logger.info("Update user : " + user.getEmail() + "::" + uploadFile.getOriginalFilename());

						ImageUploadToS3 imgUpload = new ImageUploadToS3();
						presignedUrl = GeneralUtility.makeNotNull(
								imgUpload.generatePresignedUrlAndUploadObject(s3Client, securityConfig, uploadFile,securityConfig.getS3ImagePrivateUsers().trim()));
						if (!presignedUrl.equals("")) {
							String imageUrl = securityConfig.getS3ImageUrlPrefix().trim() + "/"+securityConfig.getS3ImagePrivateUsers().trim()
									+ uploadFile.getOriginalFilename().trim();
							user.setImage(imageUrl);
						}

					}

					dbUser.setUsername(user.getUsername());
					dbUser.setPassword(user.getPassword());
					dbUser.setRole(user.getRole());
					dbUser.setActive(user.isActive());
					dbUser.setImage(user.getImage());

					User updatedUser = userService.update(dbUser);
					if (!GeneralUtility.makeNotNull(updatedUser).equals("")) {

						message = "User updated successfully.";
						logger.info(message + user.getEmail());
						result.setEmail(updatedUser.getEmail());
						result.setUsername(updatedUser.getUsername());
						result.setRole(updatedUser.getRole());
						result.setImage(presignedUrl);
						resultList.add(result);
						userResponse.setMessage(message);
						userResponse.setResult(resultList);
						return new ResponseEntity<>(userResponse, HttpStatus.OK);
					} else {
						message = "Update user failed: Changes could not be applied to the user with email :"
								+ user.getEmail();
						logger.error(message);
						resultList.add(result);
						userResponse.setMessage(message);
						userResponse.setResult(new ArrayList<ResultItem>());
						return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
					}

				} else {

					message = "User Not found.";
					logger.error(message);
					resultList.add(result);
					userResponse.setMessage(message);
					userResponse.setResult(new ArrayList<ResultItem>());
					return new ResponseEntity<>(userResponse, HttpStatus.NOT_FOUND);

				}
			} else {
				message = "Bad Request.";
				logger.error(message);
				userResponse.setMessage(message);
				userResponse.setResult(resultList);
				return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			logger.error("Error, " + e.toString());
			e.printStackTrace();
			userResponse.setMessage("Error, " + e.toString());
			userResponse.setResult(resultList);
			return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/resetPassword", produces = "application/json")
	public ResponseEntity<MessageResponse> resetPassword(@RequestBody ResetPasswordRequest resetPwdReq) {

		logger.info("Call user resetPassword API...");

		logger.info("Reset Password : " + resetPwdReq.getEmail());

		MessageResponse response = new MessageResponse();
		String message = "";
		try {

			if (!GeneralUtility.makeNotNull(resetPwdReq.getEmail()).equals("")) {
				User dbUser = userService.findByEmailAndStatus(resetPwdReq.getEmail(), true);
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
			} else {
				message = "Bad Request.";
				logger.error(message);
				response.setMessage(message);
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			}
		} catch (Exception e) {
			logger.error("Error, " + e.toString());
			e.printStackTrace();
			response.setMessage("Error, " + e.toString());

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/login", produces = "application/json")
	public boolean validateUserLogin(@RequestBody UserLoginRequest loginRequest) {
		try {
			return userService.validateUserLogin(loginRequest.getEmail(), loginRequest.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
