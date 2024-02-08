package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.management.relation.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import sg.edu.nus.iss.springboot.voucher.management.configuration.SecurityConfig;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.repository.*;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepo;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	void setUp() {
		passwordEncoder = new BCryptPasswordEncoder();
		userService = new UserService(userRepo, passwordEncoder);
	}

	User user = new User("admin@gmail.com", "Admin", "Pwd@123", RoleType.ADMIN, true);

	@Test
	void getAllUser() {

		when(userRepo.findByIsActiveTrue()).thenReturn(List.of(user));
		var userList = userService.findByIsActiveTrue();
		assertThat(userList).isNotNull();
		assertThat(userList.size()).isEqualTo(1);
	}

	@Test
	void createUser() {

		String email = "";

		if (!user.getEmail().equals("admi@gmail.com")) {

			when(userRepo.save(user)).thenReturn(user);

			user.setPassword(user.getPassword());
			User createduser = userService.create(user);
			email = createduser.getEmail();
		} 

		assertThat(email).isNotNull();
		assertThat(email.equals("admin@gmail.com")).isTrue();
	}
	
	@Test
	void updateUser() {

		String email = "";

		if (user.getEmail().equals("admin@gmail.com")) {

			user.setActive(true);
			user.setUsername("test12");
			user.setUpdatedDate(LocalDateTime.now());
			when(userRepo.save(user)).thenReturn(user);
			User updatedUser = userService.update(user);
			email = updatedUser.getEmail();
		}
		assertThat(email).isNotNull();
		assertThat(email.equals("admin@gmail.com")).isTrue();
	}

	@Test
	void resetPassword() {

		when(userRepo.save(user)).thenReturn(user);

		user.setPassword("newPwd");

		User updatedUser = userService.update(user);

		assertThat(updatedUser.getPassword()).isNotNull();
		assertThat(passwordEncoder.matches("newPwd", passwordEncoder.encode("newPwd"))).isTrue();

	}

}
