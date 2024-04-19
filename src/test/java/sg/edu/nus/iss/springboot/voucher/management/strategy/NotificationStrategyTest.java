package sg.edu.nus.iss.springboot.voucher.management.strategy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.FeedRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.strategy.impl.NotificationStrategy;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class NotificationStrategyTest {

    @Autowired
    private NotificationStrategy notificationStrategy;

    @MockBean
    private FeedRepository feedRepository;

    @MockBean
    private CampaignRepository campaignRepository;

    @MockBean
    private UserRepository userRepository;

    private static User user;
    private static List<User> mockUsers = new ArrayList<>();
    private static Store store;
    private static Campaign campaign1;

    @BeforeAll
    static void setUp() {

        user = new User("1", "admin12345@gmail.com", "Admin", "Pwd@123", RoleType.MERCHANT, null, null, true, null,
                null, null, null, null, null, null, null, false);

        store = new Store("1", "MUJI",
                "MUJI offers a wide variety of good quality items from stationery to household items and apparel.",
                "Test", "#04-36/40 Paragon Shopping Centre", "290 Orchard Rd", "", "238859", "Singapore", "Singapore",
                "Singapore", null, null, null, user, null, user, false, null);
        campaign1 = new Campaign("1", "new campaign 1", store, CampaignStatus.CREATED, null, 0, 0, null, null, 0, null,
                null, user, user, null, null, null, false);
        mockUsers.add(user);
    }

    @Test
    public void testSendFeed() {

         // Mock the user repository to return the list of users
         when(userRepository.findByRoleAndIsActiveAndIsVerifiedUsers(any(), anyBoolean(), anyBoolean())).thenReturn(mockUsers);

         // Mock the feed repository to return null for the first call and a feed object for the second call
         when(feedRepository.findByTargetedUserAndCampaign(any(), any())).thenReturn(null);
 
         // Call the sendFeed function
         notificationStrategy.sendFeed(campaign1);
 
         // Verify that the feed repository's save method was called
         verify(feedRepository, times(1)).save(any());
 
    }

}
