package sg.edu.nus.iss.springboot.voucher.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.springboot.voucher.management.entity.User;

@Repository
public interface UserRepository  extends JpaRepository<User, Long>{
	User findByEmail(String email);
	Optional<User> findByUsername(String username);
	List<User> findByIsActiveTrue();
	User save(User user);

}
