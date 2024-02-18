package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.repository.*;
import sg.edu.nus.iss.springboot.voucher.management.service.IUserService;

@Service
public class UserService implements IUserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<User> findByIsActiveTrue() {
		return userRepository.findByIsActiveTrue();
	}


	@Override
	public User findByEmailAndStatus(String email, boolean isActive) {

		return userRepository.findByEmailAndStatus(email, isActive);
	}

	@Override
	public User create(User user) {
		try {

			user.setActive(true);
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setCreatedDate(LocalDateTime.now());
			return userRepository.save(user);

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
			User user = findByEmail(email);
			if (user != null && passwordEncoder.matches(password, user.getPassword())){
				return user;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
