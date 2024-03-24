package sg.edu.nus.iss.springboot.voucher.management.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;

import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@AllArgsConstructor
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

	@OneToMany(mappedBy = "createdBy")
	private List<Store> createdStores;

	@OneToMany(mappedBy = "updatedBy")
	private List<Store> updatedStores;

	@OneToMany(mappedBy = "createdBy")
	private List<Campaign> createdCampaign;

	@OneToMany(mappedBy = "updatedBy")
	private List<Campaign> updatedCampaign;

	@OneToMany(mappedBy = "targetUserId")
	private List<Feed> feedTargetUser;


}
