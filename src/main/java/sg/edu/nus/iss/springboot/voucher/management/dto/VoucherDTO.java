package sg.edu.nus.iss.springboot.voucher.management.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import sg.edu.nus.iss.springboot.voucher.management.enums.VoucherStatus;

@Getter
@Setter
public class VoucherDTO {

    private String voucherId;
	private CampaignDTO campaign;
	private VoucherStatus voucherStatus = VoucherStatus.CLAIMED;
	private double amount;
	private LocalDateTime claimTime;
	private LocalDateTime consumedTime;
	private UserDTO claimedBy;

    public VoucherDTO(){
    	
    }

}
