package sg.edu.nus.iss.springboot.voucher.management.strategy;

import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.springboot.voucher.management.dto.ValidationResult;

public interface IAPIHelperValidationStrategy<T> {

	ValidationResult validateCreation(T data,MultipartFile val) ;
	ValidationResult validateUpdating(T data,MultipartFile val) ;
	ValidationResult validateObject(String data);
}
