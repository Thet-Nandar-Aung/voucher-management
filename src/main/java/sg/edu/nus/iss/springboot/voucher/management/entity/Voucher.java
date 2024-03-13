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
import sg.edu.nus.iss.springboot.voucher.management.enums.VoucherStatus;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Voucher {

	public Voucher() {
	}

	@Id
	@UuidGenerator(style = UuidGenerator.Style.AUTO)
	private String voucherId;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;

	@Column(nullable = false)
	private VoucherStatus voucherStatus = VoucherStatus.CLAIMED;

	@Column(nullable = true, columnDefinition = "datetime default now()")
	private LocalDateTime claimTime;

	@Column(nullable = true, columnDefinition = "datetime")
	private LocalDateTime consumedTime;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "claimedBy")
	private User claimedBy;

}
