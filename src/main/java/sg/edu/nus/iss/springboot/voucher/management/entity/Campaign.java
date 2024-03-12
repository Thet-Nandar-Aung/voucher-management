package sg.edu.nus.iss.springboot.voucher.management.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;

@Entity
@AllArgsConstructor
public class Campaign {

	public Campaign() {
		super();
	}

	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private String campaignId;

	@Column(nullable = false)
	private String description;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "storeId")
	private Store store;

	@Enumerated(EnumType.STRING)
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

	@Column(nullable = true, columnDefinition = "datetime")
	private LocalDateTime startDate;

	@Column(nullable = true, columnDefinition = "datetime")
	private LocalDateTime endDate;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "createdBy")
	private User createdBy;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "updatedBy")
	private User updatedBy;

	@Column(nullable = true, columnDefinition = "datetime default now()")
	private LocalDateTime createdDate;

	@Column(nullable = true, columnDefinition = "datetime")
	private LocalDateTime updatedDate;

	@OneToMany(mappedBy = "voucherId")
	private List<Voucher> voucher;

	@OneToMany(mappedBy = "feedId")
	private List<Feed> feed;

	public List<Voucher> getVoucher() {
		return voucher;
	}

	public void setVoucher(List<Voucher> voucher) {
		this.voucher = voucher;
	}

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

	public Store getStore() {
		return this.store;
	}

	public void setStore(Store store) {
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

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
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

	public LocalDateTime getStartDate() {
		return this.startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return this.endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public List<Feed> getFeed() {
		return feed;
	}

	public void setFeed(List<Feed> feed) {
		this.feed = feed;
	}

}
