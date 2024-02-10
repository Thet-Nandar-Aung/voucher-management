package sg.edu.nus.iss.springboot.voucher.management.utility;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import sg.edu.nus.iss.springboot.voucher.management.configuration.SecurityConfig;
import sg.edu.nus.iss.springboot.voucher.management.controller.UserController;


public class ImageUploadToS3 {
	
	private static final Logger logger = LoggerFactory.getLogger(ImageUploadToS3.class);

	public boolean imageUpload(AmazonS3 s3Client, MultipartFile multipartFile, SecurityConfig securityConfig) {

		String bucketName = securityConfig.getS3Bucket().trim();
		String keyPrefix = securityConfig.getImageKey().trim();
		String uploadFileName = multipartFile.getOriginalFilename();

		try {
			if (!multipartFile.isEmpty()) {

				InputStream is = multipartFile.getInputStream();
				PutObjectResult putObjResult = s3Client.putObject(bucketName, keyPrefix + uploadFileName, is, null);
				
				return true;
			}
		} catch (Exception e) {
			logger.error("Error while uploading image: " + e.getMessage());
			return false;
		}
		return false;
	}

	public String generatePresignedUrlAndUploadObject(AmazonS3 s3Client, SecurityConfig securityConfig, String imageUrl) {

		String bucketName = securityConfig.getS3Bucket().trim();

		// Set the expiry time
		Date expiration = new java.util.Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 60;
		expiration.setTime(expTimeMillis);

		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, imageUrl)
				.withMethod(HttpMethod.GET).withExpiration(expiration);
		//
		URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
		logger.info("Pre-Signed URL: " + url.toString());

		return url.toString();
	}
	

}
