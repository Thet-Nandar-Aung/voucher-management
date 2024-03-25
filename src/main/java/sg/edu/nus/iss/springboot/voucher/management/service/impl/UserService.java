package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.repository.*;
import sg.edu.nus.iss.springboot.voucher.management.service.IUserService;
import sg.edu.nus.iss.springboot.voucher.management.utility.AmazonSES;

@Service
public class UserService implements IUserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	
	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private  PasswordEncoder passwordEncoder;
/*
	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}*/

	@Override
	public List<User> findByIsActiveTrue() {
		return userRepository.findByIsActiveTrue();
	}

	@Override
	public User findByEmailAndStatus(String email, boolean isActive) {

		return userRepository.findByEmailAndStatus(email, isActive);
	}

	@Override
	public User create(AmazonSimpleEmailService client, User user,String from, String clientURL) {
		try {
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			String token = UUID.randomUUID().toString();
			user.setVerificationToken(token);
			user.setVerified(false);
			user.setActive(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setCreatedDate(LocalDateTime.now());

			User createdUser = userRepository.save(user);
			
			return createdUser;

		} catch (Exception e) {
			logger.error("Error occurred while user creating, " + e.toString());
			e.printStackTrace();

		}

		return new User();
	}

	@Override
	public User update(User user) {
		try {

			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setUpdatedDate(LocalDateTime.now());
			return userRepository.save(user);

		} catch (Exception e) {
			logger.error("Error occurred while user updating, " + e.toString());
			e.printStackTrace();

		}

		return new User();
	}

	@Override
	public User findByEmail(String email) {

		return userRepository.findByEmail(email);
	}

	@Override
	public User validateUserLogin(String email, String password) {
		try {
			User user = userRepository.findByEmail(email);
			if (user != null && passwordEncoder.matches(password, user.getPassword())) {
				return user;
			}
		} catch (Exception e) {
			logger.error("Error occurred while validateUserLogin, " + e.toString());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Optional<User> findById(String userId) {
		// TODO Auto-generated method stub
		return userRepository.findById(userId);
	}

	private void sendVerificationEmail(AmazonSimpleEmailService client, User user,String from,String clientURL) {

		try {
			
			String to = user.getEmail();

			String subject = "Please verify your registration";
			String body = "Dear [[name]],<br>" 
				        + "Please click the link below to verify your registration:<br>"
						+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" 
				        + "Thank you" + "<br><br>"
						+ "<i>(This is an auto-generated email, please do not reply)</i>";

			body = body.replace("[[name]]", user.getUsername());
			String verifyURL = clientURL + "/components/register/verify?verifyid=" + passwordEncoder.encode(user.getVerificationToken());

			body = body.replace("[[URL]]", verifyURL);

			AmazonSES.sendEmail(client, from, Arrays.asList(to), subject, body);
		} catch (Exception e) {
			logger.error("Error occurred while sendVerificationEmail, " + e.toString());
			e.printStackTrace();
		}
	}
}
