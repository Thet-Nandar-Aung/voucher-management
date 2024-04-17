package sg.edu.nus.iss.springboot.voucher.management;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ServletInitializerTest {
	@Test
    void testConfigure() {
        ServletInitializer servletInitializer = new ServletInitializer();
        SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder();

        SpringApplicationBuilder result = servletInitializer.configure(applicationBuilder);

        // Verify that the result is the same instance of SpringApplicationBuilder
        assertEquals(applicationBuilder, result);
    }
}
