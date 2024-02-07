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

	@GetMapping(value = "/getAllUser", produces = "application/json")
	public ResponseEntity<UserResponse> getAllUser() {
		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();

		UserResponse userResponse = new UserResponse();
		try {
			List<User> userList = userService.findByIsActiveTrue();

			if (userList.isEmpty()) {
				userResponse.setMessage("No User found");
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

	@PostMapping(value = "/upsertUser", produces = "application/json")
	public ResponseEntity<UserResponse> upsertUser(@RequestBody User user) {
		String message = "";

		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();

		UserResponse userResponse = new UserResponse();
		ResultItem result = new ResultItem();
		try {
			// if User already exists , will update

			User upsertUser = userService.upsert(user);
			if (!GeneralUtility.makeNotNull(upsertUser).equals("")) {
				
				message = "User created(or)updated successfully.";
				result.setEmail(upsertUser.getEmail());
				result.setUsername(upsertUser.getUsername());
				result.setRole(upsertUser.getRole());
				resultList.add(result);
				userResponse.setMessage(message);
				userResponse.setResult(resultList);
				return new ResponseEntity<>(userResponse, HttpStatus.OK);


			} else {// if User not found, will create
				
				message = "Error occurred when user create(or)update.";
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

	@PutMapping(value = "/resetPassword/{id}", produces = "application/json")
	public ResponseEntity<MessageResponse> resetPassword( @PathVariable(name = "id") long id ,@RequestBody ResetPasswordRequest resetPassword) {
		MessageResponse response = new MessageResponse();
		try {
			User user = userService.resetPassword(id, resetPassword.getPassword());

			if (!GeneralUtility.makeNotNull(user).equals("")) {
				response.setMessage("Password updated successfully.");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage("User not found.");
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage("Error, " + e.toString());

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
