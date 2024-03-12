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
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class Feed {
	
	public Feed() {
		super();
	}
	
	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private String feedId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "campaignId")
	private Campaign campaignId;

	@Column(nullable = false)
	private boolean isDeleted = false;
	
	@Column(nullable = false)
	private boolean isRead = false;
	
	@Column(nullable = true, columnDefinition = "datetime")
	private LocalDateTime readTime;
		
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "targetUserId")
	private User targetUserId;

	@Column(nullable = false, columnDefinition = "datetime default now()")
	private LocalDateTime createdDate;

	public String getFeedId() {
		return feedId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}

	public Campaign getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Campaign campaignId) {
		this.campaignId = campaignId;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public LocalDateTime getReadTime() {
		return readTime;
	}

	public void setReadTime(LocalDateTime readTime) {
		this.readTime = readTime;
	}

	public User getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(User targetUserId) {
		this.targetUserId = targetUserId;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	
	
	
}
