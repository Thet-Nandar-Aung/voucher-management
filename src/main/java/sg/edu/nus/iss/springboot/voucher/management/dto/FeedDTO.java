package sg.edu.nus.iss.springboot.voucher.management.dto;

import java.time.LocalDateTime;

public class FeedDTO {

	private String feedId;
	private String campaignId;
	private boolean isRead = false;
	private LocalDateTime readTime;
	private UserDTO targetUserId;

	public String getFeedId() {
		return feedId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}

	public String getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(String campaignId) {
		this.campaignId = campaignId;
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

	public UserDTO getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(UserDTO targetUserId) {
		this.targetUserId = targetUserId;
	}

}
