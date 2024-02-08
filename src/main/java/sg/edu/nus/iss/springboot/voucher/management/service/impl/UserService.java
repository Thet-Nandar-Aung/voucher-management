package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.model.ResetPasswordRequest;
import sg.edu.nus.iss.springboot.voucher.management.repository.*;
import sg.edu.nus.iss.springboot.voucher.management.service.IUserService;
import sg.edu.nus.iss.springboot.voucher.management.utility.GeneralUtility;

@Service
public class UserService implements IUserService {

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
			e.printStackTrace();

		}

		return new User();
	}

	@Override
	public User findByEmail(String email) {
		
		return userRepository.findByEmail(email);
	}

	
}
