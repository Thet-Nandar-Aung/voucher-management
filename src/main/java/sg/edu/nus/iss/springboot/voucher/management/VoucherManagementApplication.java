package sg.edu.nus.iss.springboot.voucher.management;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VoucherManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoucherManagementApplication.class, args);
		
	}

}
