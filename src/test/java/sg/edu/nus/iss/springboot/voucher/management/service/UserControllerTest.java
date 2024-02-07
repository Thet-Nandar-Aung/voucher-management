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


	User user = new User((long) 1, "John189@gmail.com", "John", "Pwd@123", RoleType.MERCHANT, true);

	@Test
	public void testGetAllUser() throws Exception {


		mockMvc.perform(MockMvcRequestBuilders.get("/api/user/getAllUser"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.message").value("200 OK"))
				.andExpect(jsonPath("$.result[0].username").value(user.getUsername()))
				.andExpect(jsonPath("$.result[0].email").value(user.getEmail()))
				.andExpect(jsonPath("$.result[0].role").value(user.getRole().toString()));
	}

	@Test
	public void testUpsertUser() throws Exception {


		mockMvc.perform(MockMvcRequestBuilders.post("/api/user/upsertUser")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user)))
		        .andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("User created(or)updated successfully."))
				.andExpect(jsonPath("$.result[0].username").value(user.getUsername()))
				.andExpect(jsonPath("$.result[0].email").value(user.getEmail()))
				.andExpect(jsonPath("$.result[0].role").value(user.getRole().toString())).andDo(print());
	}
	
	
	@Test
	public void testResetPassword() throws Exception {
        
		ResetPasswordRequest resetPwdReq= new ResetPasswordRequest("newPwd@123");
		
		
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/resetPassword/1")
                .contentType(MediaType.APPLICATION_JSON)                
                .content(objectMapper.writeValueAsString(resetPwdReq)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Password updated successfully.")).andDo(print());
    
	}

}
