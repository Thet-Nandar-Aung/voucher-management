package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.springboot.voucher.management.entity.User;

public interface IUserService {
    List<User> findByIsActiveTrue();
    User findByEmail(String email);
    Optional<User> findById(long userid);
    User upsert(User user);
    User resetPassword(long userid, String password);
    
}
