package sg.edu.nus.iss.springboot.voucher.management.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.springboot.voucher.management.dto.*;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;
import sg.edu.nus.iss.springboot.voucher.management.utility.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@InjectMocks
	private UserController userController;

	@MockBean
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EncryptionUtils encryptionUtils;

	User testUser;

	private static List<UserDTO> mockUsers = new ArrayList<>();

	@BeforeEach
	void setUp() {
		//passwordEncoder = new BCryptPasswordEncoder();
		testUser = new User("antonia@gmail.com", "Antonia", "Pwd@21212", RoleType.MERCHANT, true);

		mockUsers.add(DTOMapper.toUserDTO(testUser));
	}

	@AfterEach
	public void tearDown() {
		testUser = new User();

	}
/*
	@Test
	public void testCreateUser() throws Exception {
		
		Mockito.when(userService.create(testUser)).thenReturn(testUser);
		

		MockMultipartFile imageFile = new MockMultipartFile("image", "welcome.jpg", "image/jpg", "welcome".getBytes());

		MockMultipartFile user = new MockMultipartFile("user", "", "application/json",
				"{\"email\": \"antonia@gmail.com\",\"username\": \"Antonia\",\"password\":\"Pwd@21212\",\"role\": \"MERCHANT\"}"
						.getBytes());

		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/user/create").file(user).file(imageFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("User created successfully."))
				.andExpect(jsonPath("$.result[0].username").value(testUser.getUsername()))
				.andExpect(jsonPath("$.result[0].email").value(testUser.getEmail()))
				.andExpect(jsonPath("$.result[0].role").value(testUser.getRole().toString())).andDo(print());

	}*/

	@Test
	public void testUpdateUser() throws Exception {

		Mockito.when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);

		Mockito.when(userService.update(testUser)).thenReturn(testUser);

		MockMultipartFile imageFile = new MockMultipartFile("image", "welcome.jpg", "image/jpg", "welcome".getBytes());

		MockMultipartFile user = new MockMultipartFile("user", "", "application/json",
				"{\"email\": \"antonia@gmail.com\",\"username\": \"Antonia\",\"password\":\"Pwd@261212\",\"role\": \"MERCHANT\",\"active\":true}"
						.getBytes());

		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/user/update").file(user).file(imageFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("User updated successfully."))
				.andExpect(jsonPath("$.result[0].username").value(testUser.getUsername()))
				.andExpect(jsonPath("$.result[0].email").value(testUser.getEmail()))
				.andExpect(jsonPath("$.result[0].role").value(testUser.getRole().toString())).andDo(print());
	}

	@Test
	public void testResetPassword() throws Exception {

		UserRequest userRequest = new UserRequest(testUser.getEmail(), "Pwd@21212");
		Mockito.when(userService.findByEmailAndStatus(userRequest.getEmail(), true, true)).thenReturn(testUser);
		Mockito.when(userService.update(testUser)).thenReturn(testUser);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/user/resetPassword").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequest))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Reset Password Completed.")).andDo(print());

	}

	@Test
	public void testGetAllUser() throws Exception {

		Pageable pageable = PageRequest.of(0, 10, Sort.by("username").ascending());
		Map<Long, List<UserDTO>> mockUserMap = new HashMap<>();
		mockUserMap.put(0L, mockUsers);

		Mockito.when(userService.findByIsActiveTrue(pageable)).thenReturn(mockUserMap);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/getAll").param("page", "0").param("size", "10")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Successfully get all active user.")).andDo(print());
	}

	@Test
	public void testUserLogin() throws Exception {
		testUser.setVerified(true);
		UserRequest userRequest = new UserRequest(testUser.getEmail(), "Pwd@21212");
		Mockito.when(userService.findByEmail(userRequest.getEmail())).thenReturn(testUser);

		Mockito.when(userService.validateUserLogin(userRequest.getEmail(), userRequest.getPassword()))
				.thenReturn(testUser);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequest))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value(testUser.getEmail() + " login successfully"))
				.andExpect(jsonPath("$.result[0].username").value(testUser.getUsername()))
				.andExpect(jsonPath("$.result[0].email").value(testUser.getEmail()))
				.andExpect(jsonPath("$.result[0].role").value(testUser.getRole().toString())).andDo(print());
	}

	@Test
	public void testVerifyUser() throws Exception {

		String decodedVerificationCode = "7f03a9a9-d7a5-4742-bc85-68d52b2bee45";
		String verificationCode = encryptionUtils.encrypt(decodedVerificationCode);
		testUser.setVerified(false);
		testUser.setActive(true);
		testUser.setVerificationCode(decodedVerificationCode);
		Mockito.when(userService.findByEmailAndStatus(testUser.getEmail(), true, true)).thenReturn(testUser);

		Mockito.when(userService.verify(verificationCode)).thenReturn(DTOMapper.toUserDTO(testUser));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/verify").param("verifyid", verificationCode)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true)).andDo(print());
	}
}