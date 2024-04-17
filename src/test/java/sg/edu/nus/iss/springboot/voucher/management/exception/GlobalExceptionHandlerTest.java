package sg.edu.nus.iss.springboot.voucher.management.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import sg.edu.nus.iss.springboot.voucher.management.dto.APIResponse;
import sg.edu.nus.iss.springboot.voucher.management.dto.UserResponse;
import sg.edu.nus.iss.springboot.voucher.management.dto.UserResponse.ResultItem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

@SpringBootTest
@ActiveProfiles("test")
public class GlobalExceptionHandlerTest {
	
	@Autowired
    private GlobalExceptionHandler globalExceptionHandler;


	@Test
    void testHandleUserNotFoundException() {
        UserNotFoundException ex = mock(UserNotFoundException.class);
        String errorMessage = "User not found";
        when(ex.getMessage()).thenReturn(errorMessage);

        ResponseEntity<String> responseEntity = globalExceptionHandler.handleUserNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody());
    }
	
	@Test
    void testHandleMaxUploadSizeExceededException() {
	     String maxFileSize = "5MB";
        MaxUploadSizeExceededException ex = mock(MaxUploadSizeExceededException.class);

        ResponseEntity<UserResponse> responseEntity = globalExceptionHandler.handleMaxUploadSizeExceededException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        UserResponse userResponse = responseEntity.getBody();
        assertEquals("Your file exceeds the allowed limit of " + maxFileSize +
                ". Please select a file within this limit and try again.", userResponse.getMessage());
        assertEquals(new ArrayList<ResultItem>(), userResponse.getResult());
    }
	
	@Test
    void testHandleObjectNotFoundException() {
        Exception ex = new Exception("Test exception message");

        ResponseEntity<APIResponse> responseEntity = globalExceptionHandler.handleObjectNotFoundException(ex);

        // Verify the result
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed to get data. Test exception message", responseEntity.getBody().getMessage());
    }
	
	@Test
    void testIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Test illegal argument message");

        ResponseEntity<APIResponse> responseEntity = globalExceptionHandler.illegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid data: Test illegal argument message", responseEntity.getBody().getMessage());
    }
}
