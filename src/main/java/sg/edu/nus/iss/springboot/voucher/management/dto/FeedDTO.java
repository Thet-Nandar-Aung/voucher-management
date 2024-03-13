package sg.edu.nus.iss.springboot.voucher.management.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class FeedDTO {

	private String feedId;
	private String campaignId;
	private boolean isRead = false;
	private LocalDateTime readTime;
	private UserDTO targetUserId;

	public FeedDTO() {
	}

}
