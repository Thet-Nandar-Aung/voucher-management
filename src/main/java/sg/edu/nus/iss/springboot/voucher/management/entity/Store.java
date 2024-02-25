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

@Entity
public class Store {

	public Store() {
		super();
	}

	public Store(String storeName, String description, String tagsJson, String address1, String address2,
			String address3, String postalCode, String city, String state, String country, boolean isDeleted) {
		super();
		this.storeName = storeName;
		this.description = description;
		this.tagsJson = tagsJson;
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.postalCode = postalCode;
		this.city = city;
		this.state = state;
		this.country = country;
		this.isDeleted = isDeleted;
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

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTagsJson() {
		return tagsJson;
	}

	public void setTagsJson(String tagsJson) {
		this.tagsJson = tagsJson;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public List<Campaign> getCampaign() {
		return campaign;
	}

	public void setCampaign(List<Campaign> campaign) {
		this.campaign = campaign;
	}

}
