package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import sg.edu.nus.iss.springboot.voucher.management.dto.UserDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;

public interface IUserService {
	Map<Long, List<UserDTO>> findByIsActiveTrue(Pageable pageable);

	User findByEmail(String email);

	User findByEmailAndStatus(String email, boolean isActive,boolean isVerified);

	User create(User user);

	User update(User user);

	User validateUserLogin(String email, String password);
	
	Optional<User>  findById(String userId);
	
	UserDTO verify(String verifyId);
}
