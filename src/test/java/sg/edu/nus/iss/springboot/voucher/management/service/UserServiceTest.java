package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.repository.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@Mock
	private UserRepository userRepo;
	
	@InjectMocks
	private UserService userService;
	
	@Test
	void getAllUser() {
		User user = new User ((long)1,"admin@gmail.com","Admin","Pwd@123","Admin");
		given(userRepo.findAll())
	      .willReturn(List.of(user));
		var userList = userService.getAllUsers();
		assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(1);
	}
	

		
}
