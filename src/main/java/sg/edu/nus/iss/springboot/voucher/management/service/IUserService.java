package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;
import java.util.Optional;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;

import sg.edu.nus.iss.springboot.voucher.management.dto.UserRequest;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;

public interface IUserService {
	List<User> findByIsActiveTrue();

	User findByEmail(String email);

	User findByEmailAndStatus(String email, boolean isActive);

	User create(AmazonSimpleEmailService client, User user,String from, String siteURL);

	User update(User user);

	User validateUserLogin(String email, String password);
	
	Optional<User>  findById(String userId);
}
