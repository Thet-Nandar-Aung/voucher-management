package sg.edu.nus.iss.springboot.voucher.management.dto;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ValidationResult {
	private boolean isValid;
	private String message;
	private String imageUrl;
	private HttpStatus status;

}
