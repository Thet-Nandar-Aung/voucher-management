package sg.edu.nus.iss.springboot.voucher.management;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "DB_USERNAME=admin",
        "DB_PASSWORD=RDS_12345",
        "AWS_ACCESS_KEY=AKIA47CRXTTV2EHMAA3S",
        "AWS_SECRET_KEY=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"
})
class VoucherManagementApplicationTests {

	@Test
	void contextLoads() {
	}

}
