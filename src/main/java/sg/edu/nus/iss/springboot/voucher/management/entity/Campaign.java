package sg.edu.nus.iss.springboot.voucher.management.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;

@Entity
public class Campaign {
	
	public Campaign() {
		super();
	}
	
	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private String campaignId;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	private String store;
	
	@Column(nullable = false)
	private CampaignStatus campaignStatus = CampaignStatus.CREATED;
	
	@Column(nullable = true)
	private String tagsJson;
	
	@Column(nullable = false)
	private int numberOfVouchers;
	
	@Column(nullable = false)
	private int numberOfLikes = 0;
	
	@Column(nullable = true)
	private String pin;

	@Column(nullable = true)
	private String condition1;
	
	@Column(nullable = true)
	private String condition2;
	
	@Column(nullable = false)
	private String createdBy;
	
	@Column(nullable = true)
	private String updatedBy;
	
	@Column(nullable = false, columnDefinition = "datetime default now()")
	private LocalDateTime createdDate;
	
	@Column(nullable = true, columnDefinition = "datetime")
	private LocalDateTime updatedDate;
	
	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public CampaignStatus getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(CampaignStatus campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	public String getTagsJson() {
		return tagsJson;
	}

	public void setTagsJson(String tagsJson) {
		this.tagsJson = tagsJson;
	}

	public int getNumberOfVouchers() {
		return numberOfVouchers;
	}

	public void setNumberOfVouchers(int numberOfVouchers) {
		this.numberOfVouchers = numberOfVouchers;
	}

	public int getNumberOfLikes() {
		return numberOfLikes;
	}

	public void setNumberOfLikes(int numberOfLikes) {
		this.numberOfLikes = numberOfLikes;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getCondition1() {
		return condition1;
	}

	public void setCondition1(String condition1) {
		this.condition1 = condition1;
	}

	public String getCondition2() {
		return condition2;
	}

	public void setCondition2(String condition2) {
		this.condition2 = condition2;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
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
}
