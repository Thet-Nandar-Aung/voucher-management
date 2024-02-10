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

import sg.edu.nus.iss.springboot.voucher.management.configuration.SecurityConfig;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.model.*;
import sg.edu.nus.iss.springboot.voucher.management.model.UserResponse.ResultItem;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;
import sg.edu.nus.iss.springboot.voucher.management.utility.ImageUploadToS3;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private AmazonS3 s3Client;

	@Autowired
	private SecurityConfig securityConfig;

	@GetMapping(value = "/getAll", produces = "application/json")
	public ResponseEntity<UserResponse> getAllUser() {
		logger.info("Call user getAll API..." );
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

				for (User user : userList) {
					ResultItem userResp = new ResultItem();
					userResp.setEmail(user.getEmail());
					userResp.setUsername(user.getUsername());
					userResp.setRole(user.getRole());
					userResp.setImage(GeneralUtility.makeNotNull(user.getImage()));
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

	@PostMapping(value = "/create", produces = "application/json", consumes = { "application/json",
			"multipart/form-data" })
	public ResponseEntity<UserResponse> createUser(@RequestPart("user") User user,
			@RequestPart("image") MultipartFile uploadFile) {
		logger.info("Call user create API..." );
		
		logger.info("create user: "+user.getEmail() +"::"+uploadFile.getOriginalFilename() );
		String message = "";

		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();

		UserResponse userResponse = new UserResponse();
		ResultItem result = new ResultItem();
		try {

			User dbUser = userService.findByEmail(user.getEmail());
			if (GeneralUtility.makeNotNull(dbUser).equals("")) {
				if (uploadFile.getSize() > 1) {
					//
					ImageUploadToS3 imgUpload = new ImageUploadToS3();
					boolean isUploaded = imgUpload.imageUpload(s3Client, uploadFile, securityConfig);
					//
					if (isUploaded) {
						String imageUrl = securityConfig.getS3imagePrefix().trim() + "/"
								+ uploadFile.getOriginalFilename().trim();
						user.setImage(imageUrl);
						//need to discuss
						// imgUpload.generatePresignedUrlAndUploadObject(s3Client, securityConfig,
						// securityConfig.getImageKey() +uploadFile.getOriginalFilename().trim());
					}
					logger.info("Image successfully uploaded. " + uploadFile);
				}

				// if User not found, will create
				User createdUser = userService.create(user);
				if (!GeneralUtility.makeNotNull(createdUser).equals("")) {

					message = "User created successfully.";
					logger.info(message+ user.getEmail());
					result.setEmail(createdUser.getEmail());
					result.setUsername(createdUser.getUsername());
					result.setRole(createdUser.getRole());
					result.setImage(createdUser.getImage());
					resultList.add(result);
					userResponse.setMessage(message);
					userResponse.setResult(resultList);
					return new ResponseEntity<>(userResponse, HttpStatus.OK);

				} else {
					message = "Create user failed: Unable to create a new user account with email :" + user.getEmail();
					logger.error(message);
					resultList.add(result);
					userResponse.setMessage(message);
					userResponse.setResult( new ArrayList<ResultItem>());
					return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
				}

			} else {
				
				message ="User already exists.";
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
			@RequestPart("image") MultipartFile uploadFile) {
		
        logger.info("Call user update API..." );
		
		logger.info("Update user : "+user.getEmail() +"::"+uploadFile.getOriginalFilename() );
		
		String message = "";

		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();

		UserResponse userResponse = new UserResponse();
		ResultItem result = new ResultItem();
		try {
			User dbUser = userService.findByEmail(user.getEmail());

			if (!GeneralUtility.makeNotNull(dbUser).equals("")) {

				if (uploadFile.getSize() > 1) {
					//
					String dbImage = GeneralUtility
							.makeNotNull(dbUser.getImage());
					if(!dbImage.equals("")) {
						dbImage=dbImage.replace(securityConfig.getS3imagePrefix() + "/", "");
					}
					if (!uploadFile.getOriginalFilename().trim().equalsIgnoreCase(dbImage)) {
						ImageUploadToS3 imgUpload = new ImageUploadToS3();
						boolean isUploaded = imgUpload.imageUpload(s3Client, uploadFile, securityConfig);
						//
						if (isUploaded) {
							String imageUrl = securityConfig.getS3imagePrefix().trim() + "/"
									+ uploadFile.getOriginalFilename().trim();
							user.setImage(imageUrl);
							//need to discuss
							// imgUpload.generatePresignedUrlAndUploadObject(s3Client, securityConfig,
							// securityConfig.getImageKey() +uploadFile.getOriginalFilename().trim());
						}
						logger.info("Image successfully uploaded. " + uploadFile);
					}

				}

				dbUser.setUsername(user.getUsername());
				dbUser.setPassword(user.getPassword());
				dbUser.setRole(user.getRole());
				dbUser.setActive(user.isActive());

				User updatedUser = userService.update(dbUser);
				if (!GeneralUtility.makeNotNull(updatedUser).equals("")) {

					message = "User updated successfully.";
					logger.info(message + user.getEmail());
					result.setEmail(updatedUser.getEmail());
					result.setUsername(updatedUser.getUsername());
					result.setRole(updatedUser.getRole());
					result.setImage(updatedUser.getImage());
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
					userResponse.setResult( new ArrayList<ResultItem>());
					return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
				}

			} else {

				message = "User Not found.";
				logger.error(message);
				resultList.add(result);
				userResponse.setMessage(message);
				userResponse.setResult( new ArrayList<ResultItem>());
				return new ResponseEntity<>(userResponse, HttpStatus.NOT_FOUND);

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
		
        logger.info("Call user resetPassword API..." );
		
		logger.info("Reset Password : "+resetPwdReq.getEmail());
		 
		MessageResponse response = new MessageResponse();
		String message = "";
		try {

			User dbUser = userService.findByEmailAndStatus(resetPwdReq.getEmail(), true);
			if (!GeneralUtility.makeNotNull(dbUser).equals("")) {

				dbUser.setPassword(resetPwdReq.getPassword());
				User modifiedUser = userService.update(dbUser);

				if (!GeneralUtility.makeNotNull(modifiedUser).equals("")) {
					message = "Reset Password Completed.";
					logger.info(message +  resetPwdReq.getEmail());
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

}
