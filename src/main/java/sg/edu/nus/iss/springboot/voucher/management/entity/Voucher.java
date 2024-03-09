package sg.edu.nus.iss.springboot.voucher.management.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import sg.edu.nus.iss.springboot.voucher.management.enums.VoucherStatus;

@Entity
public class Voucher {
	
	public Voucher() {
		super();
	}
	
	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private String voucherId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;

	@Column(nullable = false)
	private VoucherStatus campaignStatus = VoucherStatus.CLAIMED;
	
	@Column(nullable = false)
	private double amount;
	
	@Column(nullable = false, columnDefinition = "datetime")
	private LocalDateTime validThrough;
	
	@Column(nullable = true, columnDefinition = "datetime default now()")
	private LocalDateTime claimTime;
	
	@Column(nullable = true, columnDefinition = "datetime")
	private LocalDateTime consumedTime;
		
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "claimedBy")
	private User claimedBy;

	public String getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(String voucherId) {
		this.voucherId = voucherId;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaignId(Campaign campaign) {
		this.campaign = campaign;
	}

	public VoucherStatus getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(VoucherStatus campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDateTime getValidThrough() {
		return validThrough;
	}

	public void setValidThrough(LocalDateTime validThrough) {
		this.validThrough = validThrough;
	}

	public LocalDateTime getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(LocalDateTime claimTime) {
		this.claimTime = claimTime;
	}

	public LocalDateTime getConsumedTime() {
		return consumedTime;
	}

	public void setConsumedTime(LocalDateTime consumedTime) {
		this.consumedTime = consumedTime;
	}

	public User getClaimedBy() {
		return claimedBy;
	}

	public void setClaimedBy(User claimedBy) {
		this.claimedBy = claimedBy;
	}
	
	
	
}
