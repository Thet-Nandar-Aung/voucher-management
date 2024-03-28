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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.springboot.voucher.management.dto.UserDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.*;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;
import sg.edu.nus.iss.springboot.voucher.management.utility.EncryptionUtils;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "DB_USERNAME=admin",
        "DB_PASSWORD=RDS_12345",
        "AWS_ACCESS_KEY=AKIA47CRXTTV2EHMAA3S",
        "AWS_SECRET_KEY=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"
})
public class UserServiceTest {

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private PasswordEncoder passwordEncoder;
	
	@Mock
    private EncryptionUtils encryptionUtils;

	@Autowired
	private UserService userService;

	private static User user = new User("admin12345@gmail.com", "Admin", "Pwd@123", RoleType.ADMIN, true);
	private static List<User> mockUsers = new ArrayList<>();

	@BeforeEach
	void setUp() {
		//passwordEncoder = new BCryptPasswordEncoder();
		//userService = new UserService(userRepository, passwordEncoder);
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
		assertThat(updatedUser.getEmail().equals("admin12345@gmail.com")).isTrue();
		

	}
	
	
	@Test
    public void verifyUser() throws Exception {
        String verificationCode = "4E5FCA157F8CEC4E6A351A349C08AC05896D21C97F102BBE318A70314B651E46BB23B575199E2A55720380070701C43D";
        String decodedVerificationCode = "7f03a9a9-d7a5-4742-bc85-68d52b2bee45";
       
        user.setVerified(false);

        Mockito.when(encryptionUtils.decrypt(verificationCode)).thenReturn(decodedVerificationCode);
        Mockito.when(userRepository.findByVerificationCode(decodedVerificationCode, false, true)).thenReturn(user);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        UserDTO userDTO = userService.verify(verificationCode);

        assertThat(user.isVerified()).isTrue();
        assertThat(userDTO).isNotNull();
    }

}
