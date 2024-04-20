package sg.edu.nus.iss.springboot.voucher.management.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedDTO {

	private String feedId;
	private CampaignDTO campaign;
	private boolean isRead = false;
	private LocalDateTime readTime;
	private UserDTO targetUserId;

	public FeedDTO() {
	}

}
