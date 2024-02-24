package sg.edu.nus.iss.springboot.voucher.management.utility;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import sg.edu.nus.iss.springboot.voucher.management.VoucherManagementApplication;
import sg.edu.nus.iss.springboot.voucher.management.configuration.VourcherManagementSecurityConfig;

public class ImageUploadToS3 {

	private static final Logger logger = LoggerFactory.getLogger(ImageUploadToS3.class);

	public static boolean imageUpload(AmazonS3 s3Client, MultipartFile multipartFile,
			VourcherManagementSecurityConfig securityConfig, String keyPrefix) {

		String bucketName = securityConfig.getS3Bucket().trim();
		String uploadFileName = multipartFile.getOriginalFilename();

		try {
			if (!multipartFile.isEmpty()) {

				InputStream is = multipartFile.getInputStream();
				PutObjectResult putObjResult = s3Client.putObject(bucketName, keyPrefix + uploadFileName, is, null);
				logger.info("Object ETag:" + putObjResult.getETag());
				return true;
			}
		} catch (Exception e) {
			logger.error("Error while uploading image: " + e.getMessage());
			return false;
		}
		return false;
	}

	public static String generatePresignedUrl(AmazonS3 s3Client, VourcherManagementSecurityConfig securityConfig,
			String imageKey) {
		String presignedUrl = "";
		try {

			String bucketName = securityConfig.getS3Bucket().trim();

			// Set the expiry time
			Date expiration = new java.util.Date();
			long expTimeMillis = expiration.getTime();
			expTimeMillis += 1000 * 60 * 5;
			expiration.setTime(expTimeMillis); // URL valid for 5 minutes

			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName,
					imageKey).withMethod(HttpMethod.GET).withExpiration(expiration);
			//
			URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
			presignedUrl = url.toString();
			logger.info("Pre-Signed URL: " + url.toString());
		} catch (Exception e) {
			logger.error("Error while generatePresignedUrl : " + e.getMessage());

		}
		return presignedUrl;
	}

	public static String generatePresignedUrlAndUploadObject(AmazonS3 s3Client,
			VourcherManagementSecurityConfig securityConfig, MultipartFile uploadFile, String keyPrefix) {

		String presignedUrl = "";

		try {
			boolean isExistsValidImage = false;
			// chekck Image already eixsts or not

			boolean isImageExists = s3Client.doesObjectExist(securityConfig.getS3Bucket(),
					keyPrefix.trim() + uploadFile.getOriginalFilename().trim());

			logger.info("Image already uploaded to s3. " + isImageExists);

			if (!isImageExists) {

				boolean isUploaded = ImageUploadToS3.imageUpload(s3Client, uploadFile, securityConfig, keyPrefix);
				if (isUploaded) {
					isExistsValidImage = true;
				}
				//

				logger.info("Image successfully uploaded. " + uploadFile);
			} else {
				isExistsValidImage = true;
			}

			if (isExistsValidImage) {

				presignedUrl = ImageUploadToS3.generatePresignedUrl(s3Client, securityConfig,
						keyPrefix.trim() + uploadFile.getOriginalFilename().trim());

				logger.info("presignedUrl: " + presignedUrl);

			}
		} catch (Exception e) {
			logger.error("Error while generatePresignedUrlAndUploadObject : " + e.getMessage());

		}
		return presignedUrl;
	}

	public static boolean checkImageExistBeforeUpload(AmazonS3 s3Client, MultipartFile multipartFile,
			VourcherManagementSecurityConfig securityConfig, String keyPrefix) {
		try {
			boolean isImageExists = s3Client.doesObjectExist(securityConfig.getS3Bucket(),
					keyPrefix.trim() + multipartFile.getOriginalFilename().trim());

			logger.info("Image already uploaded to s3. " + isImageExists);

			if (!isImageExists) {
				boolean isUploaded  = false;
				
				 isUploaded = ImageUploadToS3.imageUpload(s3Client, multipartFile, securityConfig, keyPrefix);
				
				if (isUploaded) {
					return true;
				}
			} else {
				return true;
			}
		} catch (Exception e) {
			logger.error("Error while checkImageExistBeforeUpload : " + e.getMessage());

		}
		return false;
	}

	public static boolean imageUploadWithReadAccess(AmazonS3 s3Client, MultipartFile multipartFile,
			VourcherManagementSecurityConfig securityConfig, String keyPrefix) {

		String bucketName = securityConfig.getS3Bucket().trim();
		String uploadFileName = multipartFile.getOriginalFilename();

		try {
			if (!multipartFile.isEmpty()) {

				InputStream is = multipartFile.getInputStream();

				// Create a PutObjectRequest with public read access
				PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyPrefix + uploadFileName, is,
						null).withCannedAcl(CannedAccessControlList.PublicRead);

				// Upload the object with specified ACL
				PutObjectResult putObjResult = s3Client.putObject(putObjectRequest);
				logger.info("Object ETag:" + putObjResult.getETag());

				return true;
			}
		} catch (Exception e) {
			logger.error("Error while uploading image: " + e.getMessage());
			return false;
		}
		return false;
	}

}
