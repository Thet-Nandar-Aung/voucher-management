package sg.edu.nus.iss.springboot.voucher.management.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Store {

	public Store() {
		super();
	}

	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private String storeId;

	@Column(nullable = false)
	private String storeName;

	@Column(nullable = true)
	private String description;

	@Column(nullable = true)
	private String image;

	@Column(nullable = true)
	private String tagsJson;

	@Column(nullable = true)
	private String address1;

	@Column(nullable = true)
	private String address2;

	@Column(nullable = true)
	private String address3;

	@Column(nullable = true)
	private String postalCode;

	@Column(nullable = true)
	private String city;

	@Column(nullable = true)
	private String state;

	@Column(nullable = true)
	private String country;

	@Column(nullable = true)
	private String contactNumber;

	@Column(nullable = false, columnDefinition = "datetime default now()")
	private LocalDateTime createdDate;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "createdBy")
	private User createdBy;

	@Column(nullable = true, columnDefinition = "datetime")
	private LocalDateTime updatedDate;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "updatedBy")
	private User updatedBy;

	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean isDeleted;

	@OneToMany(mappedBy = "campaignId")
	private List<Campaign> campaign;

	
}
