package sg.edu.nus.iss.springboot.voucher.management.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;

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
