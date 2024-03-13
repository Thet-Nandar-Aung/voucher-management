package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import sg.edu.nus.iss.springboot.voucher.management.dto.FeedDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Feed;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.FeedRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.FeedService;

@SpringBootTest
@TestPropertySource(properties = { "DB_USERNAME=admin", "DB_PASSWORD=RDS_12345" })

public class FeedServiceTest {

	@Mock
	private FeedRepository feedRepository;

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private FeedService feedService;

	private static Feed feed;
	private static User user;
	private static List<User> mockUsers = new ArrayList<>();
	private static Store store;
	private static Campaign campaign1;

	@BeforeAll
	static void setUp() {

		user = new User("1", "admin12345@gmail.com", "Admin", "Pwd@123", RoleType.MERCHANT, null, null, true, null,
				null, null, null, null, null, null);

		store = new Store("MUJI",
				"MUJI offers a wide variety of good quality items from stationery to household items and apparel.",
				"Test", "#04-36/40 Paragon Shopping Centre", "290 Orchard Rd", "", "238859", "Singapore", "Singapore",
				"Singapore", false);
		campaign1 = new Campaign("1", "new campaign 1", store, CampaignStatus.CREATED, null, 10,
				0,
				null, null, 10, LocalDateTime.now(), LocalDateTime.now(), user, user, LocalDateTime.now(),
				LocalDateTime.now(), null);

		mockUsers.add(user);
		feed = new Feed("1", campaign1, false, false, null, user, null);
	}

	@Test
	void promoteCampaign() {
		Mockito.when(feedRepository.save(Mockito.any(Feed.class))).thenReturn(feed);
		Mockito.when(campaignRepository.findById(campaign1.getCampaignId())).thenReturn(Optional.of(campaign1));
		Mockito.when(userRepository.findByIsActiveTrue()).thenReturn(mockUsers);

		ArrayList<FeedDTO> feedDTOList = new ArrayList<FeedDTO>();
		feedDTOList = feedService.save(campaign1.getCampaignId());

		assertThat(feedDTOList).isNotNull();
		assertThat(feedDTOList.get(0).getFeedId().equals("1")).isTrue();
	}
}
