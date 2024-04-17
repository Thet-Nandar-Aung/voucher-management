package sg.edu.nus.iss.springboot.voucher.management.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;

@Getter
@Setter
public class CampaignDTO {

    private String campaignId;
	private String description;
    private StoreDTO store;
	private CampaignStatus campaignStatus;
	private String tagsJson;
	private int numberOfVouchers;
	private int numberOfLikes;
	private String pin;
	private String tandc;
	private double amount;
    private LocalDateTime startDate;
	private LocalDateTime endDate;
    private UserDTO createdBy;
    private UserDTO updatedBy;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
    private int numberOfClaimedVouchers;

    public CampaignDTO(){
    }
   
}
