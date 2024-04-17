package sg.edu.nus.iss.springboot.voucher.management.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;

@Getter
@Setter
public class UserDTO {

	private String email;
	private String username;
	private RoleType role;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
	private boolean isActive;
	private LocalDateTime lastLoginDate;
	private String image;
	private boolean isVerified;

    public UserDTO(){
    }
    
}
