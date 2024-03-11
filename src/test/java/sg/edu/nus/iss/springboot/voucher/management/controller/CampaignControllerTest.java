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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.springboot.voucher.management.dto.CampaignDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Campaign;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.CampaignStatus;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.CampaignService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CampaignService campaignService;

    @InjectMocks
    private CampaignController campaignController;

    private static List<CampaignDTO> mockCampaigns = new ArrayList<>();
    private static User user = new User("1", "test@email.com", "username", "pwd", RoleType.CUSTOMER, null, null, true,
            null, null, null, null, null, null);
    private static Store store = new Store("1", "Store name 1", "description", null, null, null, null, null, null, null,
            null, null, null, null, user, null, user, false, null);
    private static Campaign campaign1 = new Campaign("1", "new campaign 1", store, CampaignStatus.CREATED, null, 0, 0,
            null, null, null, null, null, user, user, null, null, null);
    private static Campaign campaign2 = new Campaign("2", "new campaign 2", store, CampaignStatus.CREATED, null, 0, 0,
            null, null, null, null, null, user, user, null, null, null);

    @BeforeAll
    static void setUp() {
        mockCampaigns.add(DTOMapper.toCampaignDTO(campaign1));
        mockCampaigns.add(DTOMapper.toCampaignDTO(campaign2));
    }

    @Test
    void testGetAllCampaigns() throws Exception {
        Mockito.when(campaignService.findAllCampaigns()).thenReturn(mockCampaigns);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/campaign/getAll").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].campaignId").value(1)).andDo(print());
    }

    @Test
    void testCreateCampaign() throws Exception {
        Mockito.when(campaignService.create(campaign1)).thenReturn(DTOMapper.toCampaignDTO(campaign1));
        MockMultipartFile campaign = new MockMultipartFile("campaign", "campaign", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(campaign1));
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/campaign/create").file(campaign)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true)).andDo(print());
    }

    @Test
    void testUpdateCampaign() throws Exception {
        campaign1.setDescription("new desc");
        Mockito.when(campaignService.update(campaign1)).thenReturn(DTOMapper.toCampaignDTO(campaign1));
        MockMultipartFile campaign = new MockMultipartFile("campaign", "campaign", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(campaign1));
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/campaign/update").file(campaign)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true)).andDo(print());
        // .andExpect(jsonPath("$data.description").value("new desc")).andDo(print());
    }

    @Test
    void testDeleteCampaign() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/campaign/delete/{campaignId}", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true)).andDo(print());
    }

}
