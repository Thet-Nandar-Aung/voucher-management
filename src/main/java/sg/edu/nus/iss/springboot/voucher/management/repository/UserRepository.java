package sg.edu.nus.iss.springboot.voucher.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	User findByEmail(String email);

	Optional<User> findByUsername(String username);

	List<User> findByIsActiveTrue();

	User save(User user);

	@Query("SELECT u FROM User u WHERE u.email = ?1 AND u.isActive = ?2")
	public User findByEmailAndStatus(String email, boolean isActive,boolean isVerified);
	
	@Query("SELECT u FROM User u WHERE u.verificationCode = ?1 AND u.isVerified = ?2 AND u.isActive = ?3")
	User findByVerificationCode(String verificationCode,boolean isVerified,boolean isActive);
	
	Page<User> findByIsActiveTrue(Pageable pageable);

	@Query("SELECT u FROM User u WHERE u.role = ?1 AND u.isVerified = ?2 AND u.isActive = ?3")
	List<User> findByRoleAndIsActiveAndIsVerifiedUsers(RoleType roleType, boolean isActive, boolean isVerified);
}
