package sg.edu.nus.iss.springboot.voucher.management.exception;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import sg.edu.nus.iss.springboot.voucher.management.dto.APIResponse;
import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.UserResponse;
import sg.edu.nus.iss.springboot.voucher.management.dto.UserResponse.ResultItem;

@ControllerAdvice
public class MaxUploadSizeExceededExceptionHandler {

	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxFileSize;

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	public ResponseEntity<UserResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		String message = "Your file exceeds the allowed limit of " + maxFileSize
				+ ". Please select a file within this limit and try again.";
		UserResponse userResponse = new UserResponse();
		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();
		userResponse.setMessage(message);
		userResponse.setResult(resultList);
		return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);

	}
}