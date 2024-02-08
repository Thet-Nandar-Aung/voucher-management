package sg.edu.nus.iss.springboot.voucher.management.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import org.hibernate.annotations.UuidGenerator;

@Entity
public class User {

	public User() {
		super();
	}

	public User(String email, String username, String password, RoleType role, boolean isActive) {
		super();
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
		this.isActive = isActive;
	}

	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private String userId;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RoleType role;

	@Column(nullable = false, columnDefinition = "datetime default now()")
	private LocalDateTime createdDate;

	@Column(nullable = true, columnDefinition = "datetime")
	private LocalDateTime updatedDate;

	@Column(nullable = false, columnDefinition = "boolean default true")
	private boolean isActive;

	@Column(nullable = true, columnDefinition = "datetime")
	private LocalDateTime lastLoginDate;

	@Column(nullable = true)
	private String image;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(LocalDateTime lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
