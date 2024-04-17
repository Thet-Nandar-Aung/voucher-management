package sg.edu.nus.iss.springboot.voucher.management.exception;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import sg.edu.nus.iss.springboot.voucher.management.dto.APIResponse;
import sg.edu.nus.iss.springboot.voucher.management.dto.UserResponse;
import sg.edu.nus.iss.springboot.voucher.management.dto.UserResponse.ResultItem;

@RestControllerAdvice
public class GlobalExceptionHandler {

	 private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	
	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxFileSize;

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {

		ex.printStackTrace();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	public ResponseEntity<UserResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
		 ex.printStackTrace();
		String message = "Your file exceeds the allowed limit of " + maxFileSize
				+ ". Please select a file within this limit and try again.";
		UserResponse userResponse = new UserResponse();
		ArrayList<ResultItem> resultList = new ArrayList<ResultItem>();
		userResponse.setMessage(message);
		userResponse.setResult(resultList);
		return new ResponseEntity<>(userResponse, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	ResponseEntity<APIResponse> handleObjectNotFoundException(Exception ex){
		String message = "Failed to get data. " + ex.getMessage();
		 logger.error(message);
		  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(APIResponse.error(message));
	}
	
	
	@ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<APIResponse> illegalArgumentException(IllegalArgumentException ex) {
        String message = "Invalid data: " + ex.getMessage();
        logger.error(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.error(message));
    }

}
