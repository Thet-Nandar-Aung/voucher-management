package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.StoreDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.StoreRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.VoucherRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.CampaignService;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CampaignServiceTest {

	@MockBean
	private CampaignRepository campaignRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private StoreRepository storeRepository;

	@MockBean
	private VoucherRepository voucherRepository;

	@Autowired
	private CampaignService campaignService;

	private static List<Campaign> mockCampaigns = new ArrayList<>();
	private static User user = new User("1", "test@email.com", "username", "pwd", RoleType.CUSTOMER, null, null, true,
			null, null, null, null, null, null, null, null, false);
	private static Store store = new Store("1", "Store name 1", "description", null, null, null, null, null, null, null,
			null, null, null, null, user, null, user, false, null);
	private static Campaign campaign1 = new Campaign("1", "new campaign 1", store, CampaignStatus.PROMOTED, null, 0, 0,
			null, null, 0, null, null, user, user, null, null, null, false);
	private static Campaign campaign2 = new Campaign("2", "new campaign 2", store, CampaignStatus.CREATED, null, 0, 0,
			null, null, 0, null, null, user, user, null, null, null, false);

	@BeforeAll
	static void setUp() {
		mockCampaigns.add(campaign1);
		mockCampaigns.add(campaign2);
	}

	@Test
	void getAllActiveCampaigns() {
		long totalRecord = 0;
		List<CampaignDTO> campaignDTOList = new ArrayList<CampaignDTO>();
		Pageable pageable = PageRequest.of(0, 10);
		Page<Campaign> mockCampaignPage = new PageImpl<>(mockCampaigns, pageable, mockCampaigns.size());
		Mockito.when(campaignRepository.findByCampaignStatusIn(Arrays.asList(CampaignStatus.PROMOTED)))
				.thenReturn(mockCampaigns);

		Mockito.when(campaignRepository.findByCampaignStatusIn(Arrays.asList(CampaignStatus.PROMOTED), pageable))
				.thenReturn(mockCampaignPage);

		Map<Long, List<CampaignDTO>> campaignPage = campaignService.findAllActiveCampaigns(pageable);
		for (Map.Entry<Long, List<CampaignDTO>> entry : campaignPage.entrySet()) {
			totalRecord = entry.getKey();
			campaignDTOList = entry.getValue();

		}

		assertThat(totalRecord).isGreaterThan(0);
		assertEquals(mockCampaigns.get(0).getCampaignId(), campaignDTOList.get(0).getCampaignId());
	}

	@Test
	void getAllCampaignsByStoreId() {
		long totalRecord = 0;
		List<CampaignDTO> campaignDTOList = new ArrayList<CampaignDTO>();
		Pageable pageable = PageRequest.of(0, 10);
		Page<Campaign> mockCampaignPage = new PageImpl<>(mockCampaigns, pageable, mockCampaigns.size());

		Mockito.when(campaignRepository.findByStoreStoreId(store.getStoreId(), pageable)).thenReturn(mockCampaignPage);
		Map<Long, List<CampaignDTO>> campaignPage = campaignService
				.findAllCampaignsByStoreId(campaign1.getStore().getStoreId(), pageable);

		for (Map.Entry<Long, List<CampaignDTO>> entry : campaignPage.entrySet()) {
			totalRecord = entry.getKey();
			campaignDTOList = entry.getValue();

		}

		assertEquals(mockCampaigns.size(), campaignDTOList.size());
		assertEquals(mockCampaigns.get(0).getCampaignId(), campaignDTOList.get(0).getCampaignId());
		assertEquals(mockCampaigns.get(1).getCampaignId(), campaignDTOList.get(1).getCampaignId());
	}

	@Test
	void getAllCampaignsByEmail() {
		long totalRecord = 0;
		List<CampaignDTO> campaignDTOList = new ArrayList<CampaignDTO>();
		Pageable pageable = PageRequest.of(0, 10);
		Page<Campaign> mockCampaignPage = new PageImpl<>(mockCampaigns, pageable, mockCampaigns.size());

		Mockito.when(campaignRepository.findByCreatedByEmail(campaign1.getCreatedBy().getEmail(), pageable))
				.thenReturn(mockCampaignPage);
		Map<Long, List<CampaignDTO>> campaignPage = campaignService
				.findAllCampaignsByEmail(campaign1.getCreatedBy().getEmail(), pageable);

		for (Map.Entry<Long, List<CampaignDTO>> entry : campaignPage.entrySet()) {
			totalRecord = entry.getKey();
			campaignDTOList = entry.getValue();

		}

		assertEquals(mockCampaigns.size(), campaignDTOList.size());
		assertEquals(mockCampaigns.get(0).getCampaignId(), campaignDTOList.get(0).getCampaignId());
		assertEquals(mockCampaigns.get(1).getCampaignId(), campaignDTOList.get(1).getCampaignId());
	}

	@Test
	void createCampaign() {
		Mockito.when(campaignRepository.save(Mockito.any(Campaign.class))).thenReturn(campaign1);
		Mockito.when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
		Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		Mockito.when(campaignRepository.findById(campaign1.getCampaignId())).thenReturn(Optional.of(campaign1));
		CampaignDTO campaignDTO = campaignService.create(campaign1);
		assertEquals(campaignDTO.getCreatedBy().getEmail(), campaign1.getCreatedBy().getEmail());
		assertEquals(campaignDTO.getDescription(), campaign1.getDescription());
		assertEquals(campaignDTO.getStore().getStoreName(), campaign1.getStore().getStoreName());
	}

	@Test
	void updateCampaign() {
		Mockito.when(campaignRepository.save(Mockito.any(Campaign.class))).thenReturn(campaign1);
		Mockito.when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
		Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		Mockito.when(campaignRepository.findById(campaign1.getCampaignId())).thenReturn(Optional.of(campaign1));
		campaign1.setDescription("test update");
		campaign1.setCampaignStatus(CampaignStatus.CREATED);
		CampaignDTO campaignDTO = campaignService.update(campaign1);
		assertEquals(campaignDTO.getDescription(), "test update");
	}

	@Test
	void findSingleCampaign() {
		Mockito.when(campaignRepository.findById(campaign1.getCampaignId())).thenReturn(Optional.of(campaign1));
		CampaignDTO campaignDTO = campaignService.findByCampaignId(campaign1.getCampaignId());
		assertEquals(campaignDTO.getCampaignId(), campaign1.getCampaignId());
	}

	@Test
	void promoteCampaign() {
		campaign1.setCampaignStatus(CampaignStatus.CREATED);
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime startDate = currentTime.plusDays(5);
		LocalDateTime endDate = currentTime.plusMonths(1);

		campaign1.setStartDate(startDate);
		campaign1.setEndDate(endDate);
		Mockito.when(campaignRepository.save(Mockito.any(Campaign.class))).thenReturn(campaign1);
		Mockito.when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
		Mockito.when(campaignRepository.findById(campaign1.getCampaignId())).thenReturn(Optional.of(campaign1));
		Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		CampaignDTO campaignDTO = campaignService.promote(campaign1);
		assertEquals(campaignDTO.getCampaignStatus(), CampaignStatus.PROMOTED);
	}
}
