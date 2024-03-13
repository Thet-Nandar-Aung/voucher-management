package sg.edu.nus.iss.springboot.voucher.management.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.springboot.voucher.management.dto.VoucherDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.entity.Voucher;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.enums.VoucherStatus;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.VoucherService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class VoucherControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private VoucherService voucherService;

        @InjectMocks
        private VoucherController voucherController;

        private static List<VoucherDTO> mockVouchers = new ArrayList<>();
        private static User user = new User("1", "test@email.com", "username", "pwd", RoleType.CUSTOMER, null, null,
                        true, null, null, null, null, null, null, null);
        private static Store store = new Store("1", "Store name 1", "description", null, null, null, null, null, null,
                        null, null, null, null, null, user, null, user, false, null);
        private static Campaign campaign = new Campaign("1", "new campaign 1", store, CampaignStatus.CREATED, null, 10,
                        0,
                        null, null, 10, LocalDateTime.now(), LocalDateTime.now(), user, user, LocalDateTime.now(),
                        LocalDateTime.now(), null);
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
                Mockito.when(voucherService.findAllClaimedVouchersByEmail("test@email.com")).thenReturn(mockVouchers);
                mockMvc.perform(MockMvcRequestBuilders.get("/api/voucher/getByEmail/{email}", "test@email.com")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.data[0].voucherId").value(1)).andDo(print());
        }

        @Test
        void testGetAllVouchersByCampaignId() throws Exception {
                Mockito.when(voucherService.findAllClaimedVouchersByCampaignId(campaign.getCampaignId()))
                                .thenReturn(mockVouchers);
                mockMvc.perform(MockMvcRequestBuilders.get("/api/voucher/getByCampaignId/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.success").value(true))
                                .andExpect(jsonPath("$.data[0].voucherId").value(1)).andDo(print());
        }

        @Test
        void testGetVoucherByVoucherId() throws Exception {
                Mockito.when(voucherService.findByVoucherId(voucher2.getVoucherId()))
                                .thenReturn(DTOMapper.toVoucherDTO(voucher1));
                mockMvc.perform(MockMvcRequestBuilders.get("/api/voucher/getByCampaignId/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.success").value(true)).andDo(print());
                                // .andExpect(jsonPath("$.data.voucherId").value(1)).andDo(print());
        }

        @Test
        void testClaimVoucher() throws Exception {
                Mockito.when(voucherService.claim(voucher1)).thenReturn(DTOMapper.toVoucherDTO(voucher1));
                MockMultipartFile voucher = new MockMultipartFile("voucher", "voucher",
                                MediaType.APPLICATION_JSON_VALUE,
                                objectMapper.writeValueAsBytes(voucher1));
                mockMvc.perform(MockMvcRequestBuilders.multipart("/api/voucher/claim").file(voucher)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.success").value(true)).andDo(print());
        }

        @Test
        void testConsumeVoucher() throws Exception {
                voucher1.setVoucherStatus(VoucherStatus.CONSUMED);
                Mockito.when(voucherService.consume(voucher1)).thenReturn(DTOMapper.toVoucherDTO(voucher1));
                MockMultipartFile voucher = new MockMultipartFile("voucher", "voucher",
                                MediaType.APPLICATION_JSON_VALUE,
                                objectMapper.writeValueAsBytes(voucher1));
                mockMvc.perform(MockMvcRequestBuilders.multipart("/api/voucher/consume").file(voucher)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.success").value(true)).andDo(print());
                // .andExpect(jsonPath("$data.description").value("new desc")).andDo(print());
        }

}
