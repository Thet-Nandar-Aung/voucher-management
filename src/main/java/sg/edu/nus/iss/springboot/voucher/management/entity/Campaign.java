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
	private String tandc;

	@Column(nullable = false)
	private double amount;

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

	public String getTandc() {
		return this.tandc;
	}

	public void setTandc(String tandc) {
		this.tandc = tandc;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
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

    public Campaign(String string, String string2, Store store2, CampaignStatus created, Object object, int i, int j,
            Object object2, Object object3, Object object4, Object object5, Object object6, User user, User user2,
            Object object7, Object object8, Object object9, Object object10) {
        //TODO Auto-generated constructor stub
    }

}
