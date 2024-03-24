package sg.edu.nus.iss.springboot.voucher.management.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

}
