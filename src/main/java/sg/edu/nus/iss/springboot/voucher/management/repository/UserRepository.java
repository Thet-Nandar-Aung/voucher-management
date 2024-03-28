package sg.edu.nus.iss.springboot.voucher.management.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.springboot.voucher.management.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	User findByEmail(String email);

	Optional<User> findByUsername(String username);

	List<User> findByIsActiveTrue();

	User save(User user);

	@Query("SELECT u FROM User u WHERE u.email = ?1 AND u.isActive = ?2")
	public User findByEmailAndStatus(String email, boolean isActive);
	
	@Query("SELECT u FROM User u WHERE u.verificationCode = ?1 AND u.isVerified = ?2 AND u.isActive = ?3")
	User findByVerificationCode(String verificationCode,boolean isVerified,boolean isActive);

}
