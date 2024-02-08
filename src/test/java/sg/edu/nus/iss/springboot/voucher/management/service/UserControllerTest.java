package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.springboot.voucher.management.entity.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.model.ResetPasswordRequest;

@SpringBootTest
@AutoConfigureMockMvc

public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;


	User user = new User( "tester12@gmail.com", "Tester", "Pwd@123", RoleType.CUSTOMER, true);

	@Test
	public void testCreateUser() throws Exception {
		User user2 = new User( "John189@gmail.com", "John", "Pwd@123", RoleType.MERCHANT, true);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/user/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user2)))
		        .andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("User created successfully."))
				.andExpect(jsonPath("$.result[0].username").value(user2.getUsername()))
				.andExpect(jsonPath("$.result[0].email").value(user2.getEmail()))
				.andExpect(jsonPath("$.result[0].role").value(user2.getRole().toString())).andDo(print());
	}
	
	@Test
	public void testUpdateUser() throws Exception {


		mockMvc.perform(MockMvcRequestBuilders.put("/api/user/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user)))
		        .andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("User updated successfully."))
				.andExpect(jsonPath("$.result[0].username").value(user.getUsername()))
				.andExpect(jsonPath("$.result[0].email").value(user.getEmail()))
				.andExpect(jsonPath("$.result[0].role").value(user.getRole().toString())).andDo(print());
	}
	
	@Test
	public void testResetPassword() throws Exception {
        
		ResetPasswordRequest resetPwdReq= new ResetPasswordRequest("John189@gmail.com","newPwd@123");
		
		
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)                
                .content(objectMapper.writeValueAsString(resetPwdReq)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Reset Password Completed.")).andDo(print());
    
	}
	
	@Test
	public void testGetAllUser() throws Exception {


		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/getAll"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.message").value("200 OK"))
				.andExpect(jsonPath("$.result[0].username").value(user.getUsername()))
				.andExpect(jsonPath("$.result[0].email").value(user.getEmail()))
				.andExpect(jsonPath("$.result[0].role").value(user.getRole().toString()));
	}


}
