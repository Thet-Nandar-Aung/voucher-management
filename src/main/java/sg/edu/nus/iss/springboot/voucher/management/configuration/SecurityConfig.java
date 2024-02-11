package sg.edu.nus.iss.springboot.voucher.management.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import sg.edu.nus.iss.springboot.voucher.management.service.impl.VoucherManagementUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final String[] SECURED_URLs = { "/api/**" };

	@Value("${aws.region}")
	private String awsRegion;

	@Value("${aws.accesskey}")
	private String awsAccessKey;

	@Value("${aws.secretkey}")
	private String awsSecretKey;

	@Value("${aws.s3.bucket}")
	private String s3Bucket;

	@Value("${aws.s3.image.prefix}")
	private String s3imagePrefix;

	@Value("${aws.s3.bucket.image}")
	private String imageKey;

	@Bean
	public String getAwsRegion() {
		return awsRegion;
	}

	@Bean
	public String getAwsAccessKey() {
		return awsAccessKey;
	}

	@Bean
	public String getAwsSecretKey() {
		return awsSecretKey;
	}

	@Bean
	public String getS3Bucket() {
		return s3Bucket;
	}

	@Bean
	public String getS3imagePrefix() {
		return s3imagePrefix;
	}

	@Bean
	public String getImageKey() {
		return imageKey;
	}

	@Autowired
	private VoucherManagementUserDetailService userDetailsComp;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		var authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsComp);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
						auth -> auth.requestMatchers(SECURED_URLs).permitAll().anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider()).build();

	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public AmazonS3 s3Client() {
		AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
		AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(awsRegion).build();
		return amazonS3Client;
	}

}
