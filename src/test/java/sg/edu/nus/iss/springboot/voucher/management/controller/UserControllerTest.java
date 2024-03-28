package sg.edu.nus.iss.springboot.voucher.management.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.springboot.voucher.management.dto.UserRequest;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;
import sg.edu.nus.iss.springboot.voucher.management.utility.EncryptionUtils;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "DB_USERNAME=admin",
        "DB_PASSWORD=RDS_12345",
        "AWS_ACCESS_KEY=AKIA47CRXTTV2EHMAA3S",
        "AWS_SECRET_KEY=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"
})
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	

	@InjectMocks
	private UserController userController;

	@MockBean
	private UserService userService;

	@MockBean
	private PasswordEncoder passwordEncoder;
	
	@Mock
    private EncryptionUtils encryptionUtils;
	

	User testUser;

	@BeforeEach
	void setUp() {
	
		testUser = new User("antonia@gmail.com", "Antonia", "Pwd@21212", RoleType.MERCHANT, true);
	}

	@Test
	public void testUpdateUser() throws Exception {
		
		Mockito.when(userService.findByEmail(testUser.getEmail())).thenReturn(testUser);
		
		Mockito.when(userService.update(testUser)).thenReturn(testUser);
		
		MockMultipartFile imageFile = new MockMultipartFile("image", "welcome.jpg", "image/jpg", "welcome".getBytes());

		MockMultipartFile user = new MockMultipartFile("user", "", "application/json",
				"{\"email\": \"antonia@gmail.com\",\"username\": \"Antonia\",\"password\":\"Pwd@21212\",\"role\": \"MERCHANT\",\"active\":true}"
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
		Mockito.when(userService.findByEmailAndStatus(userRequest.getEmail(),true)).thenReturn(testUser);
		Mockito.when(userService.update(testUser)).thenReturn(testUser);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/user/resetPassword").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequest))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Reset Password Completed.")).andDo(print());

	}

	@Test
	public void testGetAllUser() throws Exception {

		Mockito.when(userService.findByIsActiveTrue()).thenReturn(List.of(testUser));

		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/getAll"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.message").value("200 OK"));
	}
	

	@Test
	public void testUserLogin() throws Exception {
		
		UserRequest userRequest = new UserRequest(testUser.getEmail(), "Pwd@21212");
		Mockito.when(userService.validateUserLogin(userRequest.getEmail(), userRequest.getPassword())).thenReturn(testUser);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userRequest)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value(testUser.getEmail() + " login successfully"))
				.andExpect(jsonPath("$.result[0].username").value(testUser.getUsername()))
				.andExpect(jsonPath("$.result[0].email").value(testUser.getEmail()))
				.andExpect(jsonPath("$.result[0].role").value(testUser.getRole().toString())).andDo(print());
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

	}
	
	@Test
	public void testVerifyUser() throws Exception {
		String verificationCode = "4E5FCA157F8CEC4E6A351A349C08AC05896D21C97F102BBE318A70314B651E46BB23B575199E2A55720380070701C43D";
        String decodedVerificationCode = "7f03a9a9-d7a5-4742-bc85-68d52b2bee45";
        
        testUser.setVerified(false);
        testUser.setVerificationCode(verificationCode);
        Mockito.when(userService.create(testUser)).thenReturn(testUser);  

        Mockito.when(userService.verify(decodedVerificationCode)).thenReturn(DTOMapper.toUserDTO(testUser));

		
		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/verify").param("verifyid", verificationCode)
				.contentType(MediaType.APPLICATION_JSON))
		        .andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true))
				.andDo(print());
	}
	*/
}

