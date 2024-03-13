package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
@TestPropertySource(properties = { "DB_USERNAME=admin", "DB_PASSWORD=RDS_12345" })
public class VoucherServiceTest {
    
    @Mock
    private VoucherRepository voucherRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CampaignRepository campaignRepository;

    @InjectMocks
    private VoucherService voucherService;

    private static List<Voucher> mockVouchers   = new ArrayList<>();
    private static User user = new User("1","test@email.com", "username", "pwd", RoleType.CUSTOMER, null, null, true, null, null, null, null, null, null,null);
    private static Store store = new Store("1", "Store name 1", "description", null, null, null, null, null, null, null, null, null, null, null, user, null, user, false, null);
    private static Campaign campaign = new Campaign("1", "new voucher 1", store, CampaignStatus.CREATED, null, 0, 0, null, null, 0, null, null, user, user, null, null, mockVouchers);
    private static Voucher voucher1 = new Voucher("1", campaign, VoucherStatus.CLAIMED, LocalDateTime.now(), null, user);
    private static Voucher voucher2 = new Voucher("2", campaign, VoucherStatus.CLAIMED, LocalDateTime.now(), null, user);
   
    @BeforeAll
    static void setUp(){
        mockVouchers.add(voucher1);
        mockVouchers.add(voucher2);
    }

    @Test
    void findAllClaimedVouchersByEmail(){
        Mockito.when(voucherRepository.findAllClaimedVouchersByEmail("test@email.com")).thenReturn(mockVouchers);
        List<VoucherDTO> voucherDTOs = voucherService.findAllClaimedVouchersByEmail("test@email.com");
        assertEquals(mockVouchers.size(), voucherDTOs.size());
        assertEquals(mockVouchers.get(0).getVoucherId(), voucherDTOs.get(0).getVoucherId());
        assertEquals(mockVouchers.get(1).getVoucherId(), voucherDTOs.get(1).getVoucherId());
    }

    @Test
    void findAllClaimedVouchersByCampaignId(){
        Mockito.when(voucherRepository.findByCampaignCampaignId("1")).thenReturn(mockVouchers  );
        List<VoucherDTO> voucherDTOs = voucherService.findAllClaimedVouchersByCampaignId("1");
        assertEquals(mockVouchers.size(), voucherDTOs.size());
        assertEquals(mockVouchers.get(0).getVoucherId(), voucherDTOs.get(0).getVoucherId());
        assertEquals(mockVouchers.get(1).getVoucherId(), voucherDTOs.get(1).getVoucherId());
    }

    @Test
    void claimVoucher(){
        Mockito.when(voucherRepository.save(Mockito.any(Voucher.class))).thenReturn(voucher1);
        Mockito.when(campaignRepository.findById(campaign.getCampaignId())).thenReturn(Optional.of(campaign));
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        voucher1.setClaimTime(LocalDateTime.now());
        VoucherDTO voucherDTO = voucherService.claim(voucher1);
        assertEquals(voucherDTO.getClaimedBy().getUserId(), voucher1.getClaimedBy().getUserId());
        assertEquals(voucherDTO.getCampaign().getCampaignId(), voucher1.getCampaign().getCampaignId());
    }

    @Test
    void consumeVoucher(){
        Mockito.when(voucherRepository.save(Mockito.any(Voucher.class))).thenReturn(voucher1);
        Mockito.when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        voucher1.setConsumedTime(LocalDateTime.now());;
        VoucherDTO voucherDTO = voucherService.consume(voucher1);
        assertNotNull(voucherDTO.getConsumedTime());
    }

    @Test
    void findSingleVoucher(){
        Mockito.when(voucherRepository.findById(voucher1.getVoucherId())).thenReturn(Optional.of(voucher1));
        VoucherDTO voucherDTO = voucherService.findByVoucherId(voucher1.getVoucherId());
        assertEquals(voucherDTO.getVoucherId(), voucher1.getVoucherId());
    }
}
