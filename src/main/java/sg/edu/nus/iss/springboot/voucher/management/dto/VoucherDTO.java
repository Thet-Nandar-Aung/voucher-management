package sg.edu.nus.iss.springboot.voucher.management.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import sg.edu.nus.iss.springboot.voucher.management.enums.VoucherStatus;

@Data
@AllArgsConstructor
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

    public String getVoucherId() {
        return this.voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public CampaignDTO getCampaign() {
        return this.campaign;
    }

    public void setCampaign(CampaignDTO campaign) {
        this.campaign = campaign;
    }

    public VoucherStatus getCampaignStatus() {
        return this.voucherStatus;
    }

    public void setVoucherStatus(VoucherStatus voucherStatus) {
        this.voucherStatus = voucherStatus;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getClaimTime() {
        return this.claimTime;
    }

    public void setClaimTime(LocalDateTime claimTime) {
        this.claimTime = claimTime;
    }

    public LocalDateTime getConsumedTime() {
        return this.consumedTime;
    }

    public void setConsumedTime(LocalDateTime consumedTime) {
        this.consumedTime = consumedTime;
    }

    public UserDTO getClaimedBy() {
        return this.claimedBy;
    }

    public void setClaimedBy(UserDTO claimedBy) {
        this.claimedBy = claimedBy;
    }

}
