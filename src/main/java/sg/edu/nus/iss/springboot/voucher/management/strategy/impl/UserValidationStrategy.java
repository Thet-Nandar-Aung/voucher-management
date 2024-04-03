package sg.edu.nus.iss.springboot.voucher.management.strategy.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;

import sg.edu.nus.iss.springboot.voucher.management.configuration.VourcherManagementSecurityConfig;
import sg.edu.nus.iss.springboot.voucher.management.dto.ValidationResult;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;
import sg.edu.nus.iss.springboot.voucher.management.strategy.IAPIHelperValidationStrategy;
import sg.edu.nus.iss.springboot.voucher.management.utility.ImageUploadToS3;

@Service
public class UserValidationStrategy implements IAPIHelperValidationStrategy<User> {

	private static final Logger logger = LoggerFactory.getLogger(UserValidationStrategy.class);

	@Autowired
	private UserService userService;

	@Autowired
	private AmazonS3 s3Client;

	@Autowired
	private VourcherManagementSecurityConfig securityConfig;

	@Override
	public ValidationResult validateCreation(User user, MultipartFile uploadFile) {
		ValidationResult validationResult = new ValidationResult();

		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			validationResult.setMessage("Email cannot be empty.");
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		User dbUser = userService.findByEmail(user.getEmail());
		if (dbUser != null) {
			validationResult.setMessage("User already exists.");
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		if (uploadFile != null && !uploadFile.isEmpty()) {
			logger.info("create user: " + user.getEmail() + "::" + uploadFile.getOriginalFilename());
			boolean isImageUploaded = ImageUploadToS3.checkImageExistBeforeUpload(s3Client, uploadFile, securityConfig,
					securityConfig.getS3ImagePrivateUsers().trim());
			if (isImageUploaded) {
				String imageUrl = securityConfig.getS3ImageUrlPrefix().trim() + "/"
						+ securityConfig.getS3ImagePrivateUsers().trim() + uploadFile.getOriginalFilename().trim();
				validationResult.setImageUrl(imageUrl);
			}
		}

		validationResult.setValid(true);
		return validationResult;
	}

	@Override
	public ValidationResult validateUpdating(User user, MultipartFile uploadFile) {
		ValidationResult validationResult = new ValidationResult();

		if (user.getEmail() == null || user.getEmail().isEmpty()) {
			validationResult.setMessage("Email cannot be empty.");
			validationResult.setStatus(HttpStatus.BAD_REQUEST);
			validationResult.setValid(false);
			return validationResult;
		}

		if (uploadFile != null && !uploadFile.isEmpty()) {
			logger.info("Update user: " + user.getEmail() + "::" + uploadFile.getOriginalFilename());
			boolean isImageUploaded = ImageUploadToS3.checkImageExistBeforeUpload(s3Client, uploadFile, securityConfig,
					securityConfig.getS3ImagePrivateUsers().trim());
			if (isImageUploaded) {
				String imageUrl = securityConfig.getS3ImageUrlPrefix().trim() + "/"
						+ securityConfig.getS3ImagePrivateUsers().trim() + uploadFile.getOriginalFilename().trim();
				validationResult.setImageUrl(imageUrl);
			}
		}

		validationResult.setValid(true);
		return validationResult;
	}

	@Override
	public ValidationResult validateObject(String email) {
		ValidationResult validationResult = new ValidationResult();
		User user = userService.findByEmail(email);

		if (user == null) {
			validationResult.setMessage("User account not found.");
			validationResult.setStatus(HttpStatus.UNAUTHORIZED);
			validationResult.setValid(false);
		} else if (!user.isActive()) {
			validationResult.setMessage("User account is deleted.");
			validationResult.setStatus(HttpStatus.UNAUTHORIZED);
			validationResult.setValid(false);
		} else if (!user.isVerified()) {
			validationResult.setMessage("Please verify the account first.");
			validationResult.setStatus(HttpStatus.UNAUTHORIZED);
			validationResult.setValid(false);
		} else {
			validationResult.setValid(true);
		}

		return validationResult;
	}

}
