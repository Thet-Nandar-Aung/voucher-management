package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.springboot.voucher.management.dto.FeedDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.FeedRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.FeedService;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FeedServiceTest {

	@MockBean
	private FeedRepository feedRepository;

	@MockBean
	private CampaignRepository campaignRepository;

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private FeedService feedService;

	private static Feed feed1;
	private static Feed feed2;
	private static User user;
	private static List<User> mockUsers = new ArrayList<>();
	private static Store store;
	private static Campaign campaign1;
	private static Campaign campaign2;
	private static List<Feed> mockFeeds = new ArrayList<>();

	@BeforeAll
	static void setUp() {

		user = new User("1", "admin12345@gmail.com", "Admin", "Pwd@123", RoleType.MERCHANT, null, null, true, null,
				null, null, null, null, null, null, null, false);

		store = new Store("1","MUJI",
				"MUJI offers a wide variety of good quality items from stationery to household items and apparel.",
				"Test", "#04-36/40 Paragon Shopping Centre", "290 Orchard Rd", "", "238859", "Singapore", "Singapore",
				"Singapore", null, null, null, user, null, user, false, null);
		campaign1 = new Campaign("1", "new campaign 1", store, CampaignStatus.CREATED, null, 0, 0, null, null, 0, null, null, user, user, null, null, null,false);
		campaign2 = new Campaign("2", "new campaign 2", store, CampaignStatus.CREATED, null, 0, 0, null, null, 0, null, null, user, user, null, null, null,false);
		   

		mockUsers.add(user);
		feed1 = new Feed("1", campaign1, false, false, null, user, null);
		feed2 = new Feed("2", campaign2, false, false, null, user, null);
		mockFeeds.add(feed1);
		mockFeeds.add(feed2);
	}

	@Test
	void generateFeeds() {
		Mockito.when(feedRepository.save(Mockito.any(Feed.class))).thenReturn(feed1);
		Mockito.when(campaignRepository.findById(campaign1.getCampaignId())).thenReturn(Optional.of(campaign1));
		Mockito.when(userRepository.findByIsActiveTrue()).thenReturn(mockUsers);

		boolean isGenerated = feedService.generateFeed();

		assertEquals(isGenerated,true);
	}

	@Test
	void findAllReadFeeds() {
		Mockito.when(feedRepository.findByIsDeletedFalseAndIsReadTrue()).thenReturn(mockFeeds);
		List<FeedDTO> feedDTOs = feedService.findAllReadFeeds();
		assertEquals(mockFeeds.size(), feedDTOs.size());
		assertEquals(mockFeeds.get(0).getFeedId(), feedDTOs.get(0).getFeedId());
		assertEquals(mockFeeds.get(1).getFeedId(), feedDTOs.get(1).getFeedId());
	}

	@Test
	void findAllActiveFeedsByCampaignId() {
		Mockito.when(campaignRepository.findById(campaign1.getCampaignId())).thenReturn(Optional.of(campaign1));
		Mockito.when(feedRepository.findAllFeedsByCampaignId(campaign1, false)).thenReturn(mockFeeds);
		List<FeedDTO> feedDTOs = feedService.findAllActiveFeedsByCampaignId(campaign1.getCampaignId());
		assertEquals(mockFeeds.size(), feedDTOs.size());
		assertEquals(mockFeeds.get(0).getFeedId(), feedDTOs.get(0).getFeedId());
		assertEquals(mockFeeds.get(1).getFeedId(), feedDTOs.get(1).getFeedId());
	}

	@Test
	void findAllFeeds() {
		Mockito.when(feedRepository.findByIsDeletedFalse()).thenReturn(mockFeeds);
		List<FeedDTO> feedDTOs = feedService.findAllFeeds();
		assertEquals(mockFeeds.size(), feedDTOs.size());
		assertEquals(mockFeeds.get(0).getFeedId(), feedDTOs.get(0).getFeedId());
		assertEquals(mockFeeds.get(1).getFeedId(), feedDTOs.get(1).getFeedId());
	}

	@Test
	void findAllReadFeedsByCampaignId() {
		Mockito.when(campaignRepository.findById(campaign1.getCampaignId())).thenReturn(Optional.of(campaign1));
		Mockito.when(feedRepository.findAllReadFeedsByCampaignId(campaign1, false, true)).thenReturn(mockFeeds);
		List<FeedDTO> feedDTOs = feedService.findAllReadFeedsByCampaignId(campaign1.getCampaignId());
		assertEquals(mockFeeds.size(), feedDTOs.size());
		assertEquals(mockFeeds.get(0).getFeedId(), feedDTOs.get(0).getFeedId());
		assertEquals(mockFeeds.get(1).getFeedId(), feedDTOs.get(1).getFeedId());
	}
	
	@Test
	void findAllFeedsByEmail() {
		Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		Mockito.when(feedRepository.findAllFeedsByEmail(user, false)).thenReturn(mockFeeds);
		List<FeedDTO> feedDTOs = feedService.findAllFeedsByEmail(user.getEmail());
		assertEquals(mockFeeds.size(), feedDTOs.size());
		assertEquals(mockFeeds.get(0).getFeedId(), feedDTOs.get(0).getFeedId());
		assertEquals(mockFeeds.get(1).getFeedId(), feedDTOs.get(1).getFeedId());
	}
	
	@Test
	void findAllReadFeedsByEmail() {
		Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		Mockito.when(feedRepository.findAllReadFeedsByEmail(user, false, true)).thenReturn(mockFeeds);
		List<FeedDTO> feedDTOs = feedService.findAllReadFeedsByEmail(user.getEmail());
		assertEquals(mockFeeds.size(), feedDTOs.size());
		assertEquals(mockFeeds.get(0).getFeedId(), feedDTOs.get(0).getFeedId());
		assertEquals(mockFeeds.get(1).getFeedId(), feedDTOs.get(1).getFeedId());
	}


	@Test
	void findByFeedId() {
		Mockito.when(feedRepository.findById(feed1.getFeedId())).thenReturn(Optional.of(feed1));
		FeedDTO feedDTO = feedService.findByFeedId(feed1.getFeedId());
		assertEquals(feedDTO.getFeedId(), feed1.getFeedId());
	}

	@Test
	void updateReadStatusById() {

		Mockito.when(feedRepository.save(Mockito.any(Feed.class))).thenReturn(feed1);
		Mockito.when(feedRepository.findById(feed1.getFeedId())).thenReturn(Optional.of(feed1));
		FeedDTO feedDTO = feedService.updateReadStatusById(feed1.getFeedId());
		assertEquals(feedDTO.isRead(), true);

	}
	
	
}
