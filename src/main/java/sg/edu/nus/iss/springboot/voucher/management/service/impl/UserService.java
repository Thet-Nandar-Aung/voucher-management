package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.entity.User;
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
	public User upsert(User user) {

		try {
			// if User already exists , will update

			User dbUser = userRepository.findByEmail(user.getEmail());
			if (!GeneralUtility.makeNotNull(dbUser).equals("")) {
				dbUser.setUsername(user.getUsername());
				dbUser.setRole(user.getRole());
				dbUser.setActive(user.isActive());
				dbUser.setUpdatedDate(LocalDateTime.now());
				return userRepository.save(dbUser);

			} else {// if User not found, will create
				user.setActive(true);
				user.setPassword(passwordEncoder.encode(user.getPassword()));
				user.setCreatedDate(LocalDateTime.now());
				return userRepository.save(user);

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return new User();
	}

	@Override
	public User findByEmail(String email) {

		return userRepository.findByEmail(email);
	}

	@Override
	public User resetPassword(long userid, String password) {
		try {
			Optional<User> user = findById(userid);
			if (user.isPresent()) {
				user.get().setPassword(passwordEncoder.encode(password));
				user.get().setUpdatedDate(LocalDateTime.now());
				User updatedUser = userRepository.save(user.get());
				return updatedUser;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new User();
	}

	@Override
	public Optional<User> findById(long userid) {
		// TODO Auto-generated method stub
		return userRepository.findById(userid);
	}

}
