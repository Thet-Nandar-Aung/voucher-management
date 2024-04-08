package sg.edu.nus.iss.springboot.voucher.management.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.springboot.voucher.management.dto.FeedDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.UserRequest;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Feed;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.FeedService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class FeedControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@InjectMocks
	private FeedController feedController;

	@MockBean
	private FeedService feedService;

	
	private static List<FeedDTO> mockFeeds = new ArrayList<>();
	
	private static User user = new User("1", "admin12345@gmail.com", "Admin", "Pwd@123", RoleType.MERCHANT, null, null, true, null,
			null, null, null, null, null, null, null, false);

	private static Store store = new Store("1", "MUJI",
			"MUJI offers a wide variety of good quality items from stationery to household items and apparel.", "",
			"Test", "#04-36/40 Paragon Shopping Centre", "290 Orchard Rd", "", "238859", "Singapore", "Singapore",
			"Singapore", "123456", null, user, null, user, false, null);
	
	private static Campaign campaign1 = new Campaign("1", "new campaign 1", store, CampaignStatus.CREATED, null, 0, 0, null, null, 0, null,
			null, user, user, null, null, null,false);
	private static Campaign campaign2 = new Campaign("2", "new campaign 2", store, CampaignStatus.CREATED, null, 0, 0, null, null, 0, null,
			null, user, user, null, null, null,false);

	private static Feed feed1 = new Feed("1", campaign1, false, false, null, user, null);
	private static Feed feed2 = new Feed("2", campaign2, false, false, null, user, null);

	@BeforeAll
	static void setUp() {

		mockFeeds.add(DTOMapper.toFeedDTO(feed1));
		mockFeeds.add(DTOMapper.toFeedDTO(feed2));
	}

	// @Test
	// void testGetAllActiveFeed() throws Exception {
	// 	Mockito.when(feedService.findAllFeeds()).thenReturn(mockFeeds);
	// 	mockMvc.perform(MockMvcRequestBuilders.get("/api/feed/getAll").contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(MockMvcResultMatchers.status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data[0].feedId").value(1))
	// 			.andDo(print());
	// }

	// @Test
	// void testGetAllReadFeed() throws Exception {
	// 	Mockito.when(feedService.findAllReadFeeds()).thenReturn(mockFeeds);
	// 	mockMvc.perform(MockMvcRequestBuilders.get("/api/feed/getAllRead").contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(MockMvcResultMatchers.status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data[0].feedId").value(1))
	// 			.andDo(print());
	// }

	// @Test
	// void testGetAllByCampaignId() throws Exception {
	// 	Mockito.when(feedService.findAllActiveFeedsByCampaignId(campaign1.getCampaignId())).thenReturn(mockFeeds);
	// 	mockMvc.perform(MockMvcRequestBuilders.post("/api/feed/getAllByCampaignId")
	// 			.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(campaign1)))
	// 	        .andExpect(MockMvcResultMatchers.status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data[0].feedId").value(1))
	// 			.andDo(print());
	// }

	// @Test
	// void testGetAllReadByCampaignId() throws Exception {
	// 	Mockito.when(feedService.findAllReadFeedsByCampaignId(campaign1.getCampaignId())).thenReturn(mockFeeds);
	// 	mockMvc.perform(MockMvcRequestBuilders.post("/api/feed/getAllReadByCampaignId")
	// 			.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(campaign1))).andExpect(MockMvcResultMatchers.status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data[0].feedId").value(1))
	// 			.andDo(print());
	// }

	// @Test
	// void testGetAllByEmail() throws Exception {
	// 	UserRequest userReq = new UserRequest(user.getEmail());
	// 	Mockito.when(feedService.findAllFeedsByEmail(user.getEmail())).thenReturn(mockFeeds);
	// 	mockMvc.perform(MockMvcRequestBuilders.post("/api/feed/getAllByEmail").contentType(MediaType.APPLICATION_JSON)
	// 			.content(objectMapper.writeValueAsString(userReq))).andExpect(MockMvcResultMatchers.status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data[0].feedId").value(1))
	// 			.andDo(print());
	// }

	// @Test
	// void testGetAllReadFeedsByEmail() throws Exception {
	// 	UserRequest userReq = new UserRequest(user.getEmail());
	// 	Mockito.when(feedService.findAllReadFeedsByEmail(user.getEmail())).thenReturn(mockFeeds);
	// 	mockMvc.perform(MockMvcRequestBuilders.post("/api/feed/getAllReadFeedsByEmail")
	// 			.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userReq)))
	// 			.andExpect(MockMvcResultMatchers.status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data[0].feedId").value(1))
	// 			.andDo(print());
	// }

	// @Test
	// void testGetFeedById() throws Exception {
	// 	Mockito.when(feedService.findByFeedId(feed1.getFeedId())).thenReturn(DTOMapper.toFeedDTO(feed1));
	// 	mockMvc.perform(
	// 			MockMvcRequestBuilders.post("/api/feed/getById").contentType(MediaType.APPLICATION_JSON)
	// 			.content(objectMapper.writeValueAsString(feed1)))
	// 			.andExpect(MockMvcResultMatchers.status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.success").value(true)).andDo(print());
	// }

	// @Test
	// void testUpdateReadStatusById() throws Exception {
	// 	Mockito.when(feedService.updateReadStatusById(feed1.getFeedId())).thenReturn(DTOMapper.toFeedDTO(feed1));
	// 	mockMvc.perform(MockMvcRequestBuilders.post("/api/feed/updateReadStatusById")
	// 			.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(feed1))).andExpect(MockMvcResultMatchers.status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.success").value(true)).andDo(print());
	// }

	// @Test
	// void testUpdateReadStatusByEmail() throws Exception {
	// 	UserRequest userReq = new UserRequest(user.getEmail());
	// 	Mockito.when(feedService.updateReadStatusByEmail(user.getEmail())).thenReturn(mockFeeds);
	// 	mockMvc.perform(MockMvcRequestBuilders.post("/api/feed/updateReadStatusByEmail")
	// 			.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userReq)))
	// 			.andExpect(MockMvcResultMatchers.status().isOk())
	// 			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
	// 			.andExpect(jsonPath("$.success").value(true))
	// 			.andExpect(jsonPath("$.data[0].feedId").value(1)).andDo(print());
	// }

}
