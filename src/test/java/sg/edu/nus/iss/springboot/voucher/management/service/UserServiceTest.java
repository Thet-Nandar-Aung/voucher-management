package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.*;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;

@SpringBootTest
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	private static User user = new User("admin12345@gmail.com", "Admin", "Pwd@123", RoleType.ADMIN, true);
	private static List<User> mockUsers = new ArrayList<>();

	@BeforeEach
	void setUp() {
		passwordEncoder = new BCryptPasswordEncoder();
		userService = new UserService(userRepository, passwordEncoder);
		mockUsers.add(user);

	}

	@Test
	void getAllActiveStore() {
		Mockito.when(userRepository.findByIsActiveTrue()).thenReturn(mockUsers);
		List<User> userList = userService.findByIsActiveTrue();
		assertEquals(mockUsers.size(), userList.size());
		assertEquals(mockUsers.get(0).getEmail(), userList.get(0).getEmail());

	}

	@Test
	void createUser() {

		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		Mockito.when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		User createdUser = userService.create(user);
		assertThat(createdUser).isNotNull();
		assertThat(createdUser.getEmail().equals("admin12345@gmail.com")).isTrue();

	}

	@Test
	void updateUser() {
		
		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		Mockito.when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		user.setActive(true);
		user.setUsername("test12");
		user.setUpdatedDate(LocalDateTime.now());

		User updatedUser = userService.update(user);
		assertThat(updatedUser).isNotNull();
		assertThat(updatedUser.getEmail().equals("admin12345@gmail.com")).isTrue();

	}

	@Test
	void resetPassword() {

		Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
		Mockito.when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
		user.setPassword("newPwd");
		User updatedUser = userService.update(user);
		assertThat(updatedUser.getPassword()).isNotNull();
		assertThat(passwordEncoder.matches("newPwd", passwordEncoder.encode("newPwd"))).isTrue();

	}

}
