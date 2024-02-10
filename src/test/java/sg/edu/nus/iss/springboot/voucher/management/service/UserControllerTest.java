package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.model.ResetPasswordRequest;

@SpringBootTest
@AutoConfigureMockMvc

public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	User testUser = new User("tester1@gmail.com", "Tester", "Pwd@123", RoleType.CUSTOMER, true);

	@Test
	public void testCreateUser() throws Exception {
		// Assuming this is the actual user you expect to be created
		User user2 = new User("antonia@gmail.com", "Antonia", "Pwd@21212", RoleType.MERCHANT, true);

		// MockMultipartFile for image file
		MockMultipartFile imageFile = new MockMultipartFile("image", "welcome.jpg", "image/jpg", "welcome".getBytes());

		// MockMultipartFile for user JSON
		MockMultipartFile user = new MockMultipartFile("user", "", "application/json",
				"{\"email\": \"antonia@gmail.com\",\"username\": \"Antonia\",\"password\":\"Pwd@21212\",\"role\": \"MERCHANT\"}"
						.getBytes());

		// Perform the test using MockMvc
		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/user/create").file(user).file(imageFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("User created successfully."))
				.andExpect(jsonPath("$.result[0].username").value(user2.getUsername()))
				.andExpect(jsonPath("$.result[0].email").value(user2.getEmail()))
				.andExpect(jsonPath("$.result[0].role").value(user2.getRole().toString())).andDo(print());

	}

	@Test
	public void testUpdateUser() throws Exception {
		// Assuming this is the actual user you expect to be created
		User user2 = new User("antonia@gmail.com", "Antonia", "Pwd@21212", RoleType.MERCHANT, true);

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
				.andExpect(jsonPath("$.result[0].username").value(user2.getUsername()))
				.andExpect(jsonPath("$.result[0].email").value(user2.getEmail()))
				.andExpect(jsonPath("$.result[0].role").value(user2.getRole().toString())).andDo(print());
	}

	@Test
	public void testResetPassword() throws Exception {

		ResetPasswordRequest resetPwdReq = new ResetPasswordRequest("antonia@gmail.com", "newPwd@123");

		mockMvc.perform(MockMvcRequestBuilders.put("/api/user/resetPassword").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(resetPwdReq))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Reset Password Completed.")).andDo(print());

	}

	@Test
	public void testGetAllUser() throws Exception {

		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/getAll"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.message").value("200 OK"));
	}

}
