package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.model.ResetPasswordRequest;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserService userService;


	User testUser;

	@BeforeEach
	void setUp() {
		testUser = new User("antonia@gmail.com", "Antonia", "Pwd@21212", RoleType.MERCHANT, true);
	}


	@Test
	@Transactional
	public void testCreateUser() throws Exception {

		// MockMultipartFile for image file
		MockMultipartFile imageFile = new MockMultipartFile("image", "welcome.jpg", "image/jpg", "welcome".getBytes());

		// MockMultipartFile for user JSON
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
	@Transactional
	public void testUpdateUser() throws Exception {

		userService.create(testUser);

		// MockMultipartFile for image file
		MockMultipartFile imageFile = new MockMultipartFile("image", "welcome.jpg", "image/jpg", "welcome".getBytes());

		// MockMultipartFile for user JSON
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
	@Transactional
	public void testResetPassword() throws Exception {

		userService.create(testUser);

		ResetPasswordRequest resetPwdReq = new ResetPasswordRequest("antonia@gmail.com", "newPwd@123");

		mockMvc.perform(MockMvcRequestBuilders.put("/api/user/resetPassword").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(resetPwdReq))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Reset Password Completed.")).andDo(print());

	}

	@Test
	@Transactional
	public void testGetAllUser() throws Exception {

		userService.create(testUser);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/getAll"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.message").value("200 OK"));
	}

}
