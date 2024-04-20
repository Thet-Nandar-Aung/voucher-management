package sg.edu.nus.iss.springboot.voucher.management.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.VoucherManagementUserDetailService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class VoucherManagementUserDetailServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VoucherManagementUserDetailService userDetailService;

    
    private static User user = new User("1","test@email.com", "testUser", "pwd", RoleType.CUSTOMER, null, null, true, null, null, null, null, null, null,null, null, false);
    
    
    @Test
    public void testLoadUserByUsername_UserFound() {
    	
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailService.loadUserByUsername("testUser");

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        verify(userRepository, times(1)).findByUsername("testUser");
    }

}
