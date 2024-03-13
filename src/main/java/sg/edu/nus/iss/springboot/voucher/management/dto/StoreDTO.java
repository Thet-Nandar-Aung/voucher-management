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
public class StoreDTO {

    private String storeId;
	private String storeName;
	private String description;
	private String image;
	private String tagsJson;
	private String address1;
	private String address2;
	private String address3;
	private String postalCode;
	private String city;
	private String state;
	private String country;
	private String contactNumber;
	private LocalDateTime createdDate;
	private UserDTO createdBy;
	private LocalDateTime updatedDate;
	private UserDTO updatedBy;
	private boolean isDeleted;

    public StoreDTO() {
    }
    
}
