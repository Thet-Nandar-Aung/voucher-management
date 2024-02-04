package sg.edu.nus.iss.springboot.voucher.management.exception;

public class UserNotFoundException extends RuntimeException {
	 public UserNotFoundException(String message) {
	        super(message);
	    }
}
