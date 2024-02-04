package sg.edu.nus.iss.springboot.voucher.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private  UserService userService;
    
	@GetMapping(value = "/getAllUser" , produces="application/json")
	public ResponseEntity<List<User>> getAllStore (){
		return ResponseEntity.ok().body(userService.getAllUsers());
		
	}
	

}
