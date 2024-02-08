package sg.edu.nus.iss.springboot.voucher.management.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.model.*;
import sg.edu.nus.iss.springboot.voucher.management.model.UserResponse.ResultItem;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping(value = "/getAll", produces = "application/json")
	public ResponseEntity<UserResponse> getAllUser() {
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
					resultList.add(userResp);
				}

				userResponse.setResult(resultList);
				return new ResponseEntity<>(userResponse, HttpStatus.OK);
			}

		} catch (Exception e) {
			userResponse.setMessage("Error," + e.toString());
			userResponse.setResult(resultList);
			return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<UserResponse> createUser(@RequestBody User user) {
		String message = "";

		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();

		UserResponse userResponse = new UserResponse();
		ResultItem result = new ResultItem();
		try {

			User dbUser = userService.findByEmail(user.getEmail());
			if (GeneralUtility.makeNotNull(dbUser).equals("")) {
				// if User not found, will create
				User upsertUser = userService.create(user);
				if (!GeneralUtility.makeNotNull(upsertUser).equals("")) {

					message = "User created successfully.";
					result.setEmail(upsertUser.getEmail());
					result.setUsername(upsertUser.getUsername());
					result.setRole(upsertUser.getRole());
					resultList.add(result);
					userResponse.setMessage(message);
					userResponse.setResult(resultList);
					return new ResponseEntity<>(userResponse, HttpStatus.OK);

				} else {
					message = "Create user failed: Unable to create a new user account with email :" + user.getEmail();
					resultList.add(result);
					userResponse.setMessage(message);
					userResponse.setResult(resultList);
					return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
				}

			} else {
				userResponse.setMessage("User already exists.");
				userResponse.setResult(resultList);
				return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			userResponse.setMessage("Error, " + e.toString());
			userResponse.setResult(resultList);
			return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/update", produces = "application/json")
	public ResponseEntity<UserResponse> updateUser(@RequestBody User user) {
		String message = "";

		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();

		UserResponse userResponse = new UserResponse();
		ResultItem result = new ResultItem();
		try {
			User dbUser = userService.findByEmail(user.getEmail());

			if (!GeneralUtility.makeNotNull(dbUser).equals("")) {
				
				dbUser.setUsername(user.getUsername());
				dbUser.setPassword(user.getPassword());
				dbUser.setRole(user.getRole());				
				dbUser.setActive(user.isActive());
				
				User upsertUser = userService.update(dbUser);
				if (!GeneralUtility.makeNotNull(upsertUser).equals("")) {

					message = "User updated successfully.";
					result.setEmail(upsertUser.getEmail());
					result.setUsername(upsertUser.getUsername());
					result.setRole(upsertUser.getRole());
					resultList.add(result);
					userResponse.setMessage(message);
					userResponse.setResult(resultList);
					return new ResponseEntity<>(userResponse, HttpStatus.OK);
				} else {
					message = "Update user failed: Changes could not be applied to the user with email :"
							+ user.getEmail();
					resultList.add(result);
					userResponse.setMessage(message);
					userResponse.setResult(resultList);
					return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
				}

			} else {

				message = "User Not found.";
				resultList.add(result);
				userResponse.setMessage(message);
				userResponse.setResult(resultList);
				return new ResponseEntity<>(userResponse, HttpStatus.NOT_FOUND);

			}

		} catch (Exception e) {
			e.printStackTrace();
			userResponse.setMessage("Error, " + e.toString());
			userResponse.setResult(resultList);
			return new ResponseEntity<>(userResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/resetPassword", produces = "application/json")
	public ResponseEntity<MessageResponse> resetPassword(@RequestBody ResetPasswordRequest resetPwdReq) {
		MessageResponse response = new MessageResponse();
		String message = "";
		try {

			User dbUser = userService.findByEmailAndStatus(resetPwdReq.getEmail(), true);
			if (!GeneralUtility.makeNotNull(dbUser).equals("")) {

				dbUser.setPassword(resetPwdReq.getPassword());
				User modifiedUser = userService.update(dbUser);

				if (!GeneralUtility.makeNotNull(modifiedUser).equals("")) {
					message =  "Reset Password Completed.";
					response.setMessage(message);
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					message = "Reset Password failed: Unable to reset password for the user with email :"
							+ resetPwdReq.getEmail();
					response.setMessage(message);
					return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

				}
			} else {
				message = "Reset Password failed: Unable to find the user with email :"
						+ resetPwdReq.getEmail();
				response.setMessage(message);
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Error, " + e.toString());

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
