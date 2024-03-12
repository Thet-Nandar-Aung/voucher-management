package sg.edu.nus.iss.springboot.voucher.management.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.FeedRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.CampaignService;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.FeedService;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.StoreService;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "DB_USERNAME=admin", "DB_PASSWORD=RDS_12345" })
public class CampaignControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Mock
	private FeedRepository feedRepository;

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	private UserRepository userRepository;

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private StoreService storeService;

	@Autowired
	private UserService userService;

	@InjectMocks
	private FeedService feedService;

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
		campaign1 = new Campaign("1", "new campaign 1", store, CampaignStatus.CREATED, null, 0, 0, null, null, null,
				null, null, user, user, null, null, null, null);

		mockUsers.add(user);
	}

	@Test
	@Transactional
	void testPromoteCampaign() throws Exception {

		User createdUser = userService.create(user);
		store.setCreatedBy(createdUser);

		storeService.create(store);

		CampaignDTO campaignDTO = campaignService.create(campaign1);
		String campaginId = campaignDTO.getCampaignId();

		mockMvc.perform(MockMvcRequestBuilders.post("/api/campaign/promote/{campaignId}", campaginId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Campaign promoted successfully"))
				.andExpect(jsonPath("$.data[0].campaignId").value(campaginId)).andDo(print());
	}
}
