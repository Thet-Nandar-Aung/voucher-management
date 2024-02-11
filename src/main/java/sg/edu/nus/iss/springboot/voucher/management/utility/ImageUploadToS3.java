package sg.edu.nus.iss.springboot.voucher.management.utility;

import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
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
import sg.edu.nus.iss.springboot.voucher.management.entity.User;

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

	public String generatePresignedUrl(AmazonS3 s3Client, SecurityConfig securityConfig,
			String imageUrl) {
		String presignedUrl = "";
		if (imageUrl.startsWith("http")) {

			String bucketName = securityConfig.getS3Bucket().trim();

			// Set the expiry time
			Date expiration = new java.util.Date();
			long expTimeMillis = expiration.getTime();
			expTimeMillis += 1000 * 60 * 5;
			expiration.setTime(expTimeMillis); // URL valid for 5 minutes

			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName,
					imageUrl).withMethod(HttpMethod.GET).withExpiration(expiration);
			//
			URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
			presignedUrl = url.toString();
			logger.info("Pre-Signed URL: " + url.toString());

		}

		return presignedUrl;
	}

	public String generatePresignedUrlAndUploadObject(AmazonS3 s3Client, SecurityConfig securityConfig, MultipartFile uploadFile) {
		ImageUploadToS3 imgUpload = new ImageUploadToS3();
		String presignedUrl = "";
		boolean isExistsValidImage = false;
		// chekck Image already eixsts or not

		boolean isImageExists = s3Client.doesObjectExist(securityConfig.getS3Bucket(),
				securityConfig.getImageKey().trim() + uploadFile.getOriginalFilename().trim());

		logger.info("Image already uploaded to s3. " + isImageExists);

		if (!isImageExists) {

			boolean isUploaded = imgUpload.imageUpload(s3Client, uploadFile, securityConfig);
			if (isUploaded) {
				isExistsValidImage = true;
			}
			//

			logger.info("Image successfully uploaded. " + uploadFile);
		} else {
			isExistsValidImage = true;
		}

		if (isExistsValidImage) {

			presignedUrl = imgUpload.generatePresignedUrl(s3Client, securityConfig,
					securityConfig.getS3imagePrefix().trim() +"/"+ uploadFile.getOriginalFilename().trim());

			logger.info("presignedUrl: " + presignedUrl);

		}
		return presignedUrl;
	}

}
