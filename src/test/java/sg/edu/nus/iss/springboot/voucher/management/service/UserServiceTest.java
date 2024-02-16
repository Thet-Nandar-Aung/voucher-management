package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.repository.*;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "DB_USERNAME=admin",
        "DB_PASSWORD=RDS_12345",
        "AWS_ACCESS_KEY=AKIA47CRXTTV2EHMAA3S",
        "AWS_SECRET_KEY=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"
})
public class UserServiceTest {

	@Mock
	private UserRepository userRepo;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserService userService;
	
	User testUser;

	@BeforeEach
	void setUp() {
		passwordEncoder = new BCryptPasswordEncoder();
		userService = new UserService(userRepo, passwordEncoder);
		testUser = new User("admin12345@gmail.com", "Admin", "Pwd@123", RoleType.ADMIN, true);
	}

	@Test
	void getAllUser() {

		when(userRepo.findByIsActiveTrue()).thenReturn(List.of(testUser));
		var userList = userService.findByIsActiveTrue();
		assertThat(userList).isNotNull();
		assertThat(userList.size()).isEqualTo(1);
	}

	@Test
	void createUser() {

		String email = "";

		if (!testUser.getEmail().equals("admi@gmail.com")) {

			when(userRepo.save(testUser)).thenReturn(testUser);

			testUser.setPassword(testUser.getPassword());
			User createduser = userService.create(testUser);
			email = createduser.getEmail();
		} 

		assertThat(email).isNotNull();
		assertThat(email.equals("admin12345@gmail.com")).isTrue();
	}
	
	@Test
	void updateUser() {

		String email = "";

		if (testUser.getEmail().equals("admin12345@gmail.com")) {

			testUser.setActive(true);
			testUser.setUsername("test12");
			testUser.setUpdatedDate(LocalDateTime.now());
			when(userRepo.save(testUser)).thenReturn(testUser);
			User updatedUser = userService.update(testUser);
			email = updatedUser.getEmail();
		}
		assertThat(email).isNotNull();
		assertThat(email.equals("admin12345@gmail.com")).isTrue();
	}

	@Test
	void resetPassword() {

		when(userRepo.save(testUser)).thenReturn(testUser);

		testUser.setPassword("newPwd");

		User updatedUser = userService.update(testUser);

		assertThat(updatedUser.getPassword()).isNotNull();
		assertThat(passwordEncoder.matches("newPwd", passwordEncoder.encode("newPwd"))).isTrue();

	}

}
