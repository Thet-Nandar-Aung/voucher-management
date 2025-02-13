package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.iss.springboot.voucher.management.dto.VoucherDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.enums.VoucherStatus;
import sg.edu.nus.iss.springboot.voucher.management.repository.CampaignRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.StoreRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.VoucherRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.VoucherService;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class VoucherServiceTest {

	@MockBean
	private VoucherRepository voucherRepository;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private StoreRepository storeRepository;

	@MockBean
	private CampaignRepository campaignRepository;

	@Autowired
	private VoucherService voucherService;

	private static List<Voucher> mockVouchers = new ArrayList<>();
	private static User user = new User("1", "test@email.com", "username", "pwd", RoleType.CUSTOMER, null, null, true,
			null, null, null, null, null, null, null, null, false);
	private static Store store = new Store("1", "Store name 1", "description", null, null, null, null, null, null, null,
			null, null, null, null, user, null, user, false, null);
	private static Campaign campaign = new Campaign("1", "new voucher 1", store, CampaignStatus.CREATED, null, 0, 0,
			null, null, 0, null, null, user, user, null, null, mockVouchers, false);
	private static Voucher voucher1 = new Voucher("1", campaign, VoucherStatus.CLAIMED, LocalDateTime.now(), null,
			user);
	private static Voucher voucher2 = new Voucher("2", campaign, VoucherStatus.CLAIMED, LocalDateTime.now(), null,
			user);

	@BeforeAll
	static void setUp() {
		mockVouchers.add(voucher1);
		mockVouchers.add(voucher2);
	}

	@Test
	void findAllClaimedVouchersByEmail() {
		long totalRecord = 0;
		List<VoucherDTO> voucherDTOList = new ArrayList<VoucherDTO>();
		Pageable pageable = PageRequest.of(0, 10);
		Page<Voucher> mockVoucherPage = new PageImpl<>(mockVouchers, pageable, mockVouchers.size());

		Mockito.when(voucherRepository.findAllClaimedVouchersByEmail("test@email.com", pageable))
				.thenReturn(mockVoucherPage);
		Map<Long, List<VoucherDTO>> voucherPage = voucherService.findAllClaimedVouchersByEmail("test@email.com",
				pageable);

		for (Map.Entry<Long, List<VoucherDTO>> entry : voucherPage.entrySet()) {
			totalRecord = entry.getKey();
			voucherDTOList = entry.getValue();

		}
		assertEquals(mockVouchers.size(), voucherDTOList.size());
		assertEquals(mockVouchers.get(0).getVoucherId(), voucherDTOList.get(0).getVoucherId());
		assertEquals(mockVouchers.get(1).getVoucherId(), voucherDTOList.get(1).getVoucherId());
	}

	@Test
	void findAllClaimedVouchersByCampaignId() {
		long totalRecord = 0;
		List<VoucherDTO> voucherDTOList = new ArrayList<VoucherDTO>();
		Pageable pageable = PageRequest.of(0, 10);
		Page<Voucher> mockVoucherPage = new PageImpl<>(mockVouchers, pageable, mockVouchers.size());

		Mockito.when(voucherRepository.findByCampaignCampaignId("1", pageable)).thenReturn(mockVoucherPage);
		Map<Long, List<VoucherDTO>> voucherPage = voucherService.findAllClaimedVouchersByCampaignId("1", pageable);
		for (Map.Entry<Long, List<VoucherDTO>> entry : voucherPage.entrySet()) {
			totalRecord = entry.getKey();
			voucherDTOList = entry.getValue();

		}
		assertEquals(mockVouchers.size(), voucherDTOList.size());
		assertEquals(mockVouchers.get(0).getVoucherId(), voucherDTOList.get(0).getVoucherId());
		assertEquals(mockVouchers.get(1).getVoucherId(), voucherDTOList.get(1).getVoucherId());
	}

	@Test
	void claimVoucher() {
		Mockito.when(voucherRepository.save(Mockito.any(Voucher.class))).thenReturn(voucher1);
		Mockito.when(campaignRepository.findById(campaign.getCampaignId())).thenReturn(Optional.of(campaign));
		Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		voucher1.setClaimTime(LocalDateTime.now());
		VoucherDTO voucherDTO = voucherService.claim(voucher1);
		assertEquals(voucherDTO.getClaimedBy().getEmail(), voucher1.getClaimedBy().getEmail());
		assertEquals(voucherDTO.getCampaign().getCampaignId(), voucher1.getCampaign().getCampaignId());
	}

	@Test
	void consumeVoucher() {
		Mockito.when(voucherRepository.findById(voucher1.getVoucherId())).thenReturn(Optional.of(voucher1));
		Mockito.when(voucherRepository.save(Mockito.any(Voucher.class))).thenReturn(voucher1);
		Mockito.when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
		Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		voucher1.setConsumedTime(LocalDateTime.now());
		;
		VoucherDTO voucherDTO = voucherService.consume(voucher1);
		assertNotNull(voucherDTO.getConsumedTime());
	}

	@Test
	void findSingleVoucher() {
		Mockito.when(voucherRepository.findById(voucher1.getVoucherId())).thenReturn(Optional.of(voucher1));
		VoucherDTO voucherDTO = voucherService.findByVoucherId(voucher1.getVoucherId());
		assertEquals(voucherDTO.getVoucherId(), voucher1.getVoucherId());
	}

	@Test
	void findByCampaignCampaignId() {

		Mockito.when(voucherRepository.findByCampaignCampaignId(voucher1.getCampaign().getCampaignId()))
				.thenReturn(mockVouchers);
		List<Voucher> voucherList = voucherService.findByCampaignCampaignId(voucher1.getCampaign().getCampaignId());

		assertEquals(mockVouchers.size(), voucherList.size());
	}

	@Test
	void findByCampaignIdAndClaimedBy() {
		Mockito.when(userRepository.findByEmail(voucher1.getClaimedBy().getEmail())).thenReturn(user);
		Mockito.when(voucherRepository.findByCampaignAndClaimedBy(campaign, user)).thenReturn(mockVouchers);
		List<VoucherDTO> voucherDTOList = voucherService.findByCampaignIdAndClaimedBy(campaign, user);
		assertEquals(mockVouchers.size(), voucherDTOList.size());
		assertEquals(mockVouchers.get(0).getVoucherId(), voucherDTOList.get(0).getVoucherId());
	}
}
