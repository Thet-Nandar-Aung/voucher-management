package sg.edu.nus.iss.springboot.voucher.management.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.VoucherDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.enums.VoucherStatus;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.CampaignService;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.StoreService;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.VoucherService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class VoucherControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private VoucherService voucherService;

	@MockBean
	private CampaignService campaignService;

	@MockBean
	private UserService userService;

	@MockBean
	private StoreService storeService;

	private static List<VoucherDTO> mockVouchers = new ArrayList<>();
	private static User user = new User("1", "test@email.com", "username", "pwd", RoleType.CUSTOMER, null, null, true,
			null, null, null, null, null, null, null, null, false);
	private static Store store = new Store("1", "MUJI",
			"MUJI offers a wide variety of good quality items from stationery to household items and apparel.", "",
			"Test", "#04-36/40 Paragon Shopping Centre", "290 Orchard Rd", "", "238859", "Singapore", "Singapore",
			"Singapore", "123456", null, user, null, user, false, null);

	private static Campaign campaign = new Campaign("1", "new campaign 1", store, CampaignStatus.PROMOTED, null, 10, 0,
			null, null, 10, LocalDateTime.now(), LocalDateTime.now(), user, user, LocalDateTime.now(),
			LocalDateTime.now(), null, false);
	private static Voucher voucher1 = new Voucher("1", campaign, VoucherStatus.CLAIMED, LocalDateTime.now(), null,
			user);
	private static Voucher voucher2 = new Voucher("2", campaign, VoucherStatus.CLAIMED, LocalDateTime.now(), null,
			user);

	@BeforeAll
	static void setUp() {
		mockVouchers.add(DTOMapper.toVoucherDTO(voucher1));
		mockVouchers.add(DTOMapper.toVoucherDTO(voucher1));
	}

	@Test
	void testGetAllVouchersByEmail() throws Exception {

		Pageable pageable = PageRequest.of(0, 10, Sort.by("claimTime").ascending());
		Map<Long, List<VoucherDTO>> mockVoucherMap = new HashMap<>();
		mockVoucherMap.put(0L, mockVouchers);

		Mockito.when(voucherService.findAllClaimedVouchersByEmail("test@email.com", pageable))
				.thenReturn(mockVoucherMap);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/voucher/getByEmail").param("page", "0").param("size", "10")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data[0].voucherId").value(1))
				.andDo(print());
	}

	@Test
	void testGetAllVouchersByCampaignId() throws Exception {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("claimTime").ascending());
		Map<Long, List<VoucherDTO>> mockVoucherMap = new HashMap<>();
		mockVoucherMap.put(0L, mockVouchers);

		Mockito.when(voucherService.findAllClaimedVouchersByCampaignId(campaign.getCampaignId(), pageable))
				.thenReturn(mockVoucherMap);
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/voucher/getByCampaignId").param("page", "0").param("size", "10")
						.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(campaign)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.data[0].voucherId").value(1))
				.andDo(print());
	}

	@Test
	void testGetVoucherByVoucherId() throws Exception {
		Mockito.when(voucherService.findByVoucherId(voucher2.getVoucherId()))
				.thenReturn(DTOMapper.toVoucherDTO(voucher2));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/voucher/getById").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(voucher2))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true)).andDo(print());
	}

	@Test
	void testClaimVoucher() throws Exception {

		Mockito.when(campaignService.findById(campaign.getCampaignId())).thenReturn(Optional.of(campaign));

		Mockito.when(voucherService.claim(Mockito.any(Voucher.class))).thenReturn(DTOMapper.toVoucherDTO(voucher1));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/voucher/claim").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(voucher1))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true)).andDo(print());
	}

	@Test
	void testConsumeVoucher() throws Exception {

		Mockito.when(voucherService.findByVoucherId(voucher1.getVoucherId()))
				.thenReturn(DTOMapper.toVoucherDTO(voucher1));
		voucher1.setVoucherStatus(VoucherStatus.CONSUMED);
		Mockito.when(voucherService.consume(Mockito.any(Voucher.class))).thenReturn(DTOMapper.toVoucherDTO(voucher1));

		mockMvc.perform(MockMvcRequestBuilders.post("/api/voucher/consume").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(voucher1))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true)).andDo(print());
	}

}
