package sg.edu.nus.iss.springboot.voucher.management.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.springboot.voucher.management.dto.FeedDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.UserRequest;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.StoreService;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "DB_USERNAME=admin", "DB_PASSWORD=RDS_12345", "AWS_ACCESS_KEY=AKIA47CRXTTV2EHMAA3S",
		"AWS_SECRET_KEY=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" })
public class StoreControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private StoreService storeService;
	
	private static List<Store> mockStores= new ArrayList<>();

	private static User user = new User("antonia@gmail.com", "Antonia", "Pwd@21212", RoleType.MERCHANT, true);

	private static Store store = new Store("1","MUJI",
			"MUJI offers a wide variety of good quality items from stationery to household items and apparel.",
			"Test", "#04-36/40 Paragon Shopping Centre", "290 Orchard Rd", "", "238859", "Singapore", "Singapore",
			"Singapore", null, null, null, user, null, user, false, null);


	@BeforeEach
	void setUp() {

		mockStores.add(store);
		
	}

	@Test
	@Transactional
	void testGetAllActiveStore() throws Exception {
		Mockito.when(storeService.findByIsDeletedFalse()).thenReturn(mockStores);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/store/getAll"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.message").value("200 OK"))
				.andDo(print());
	}
	
	@Test
	@Transactional
	void testGetStoreById() throws Exception {
		Mockito.when(storeService.findById(store.getStoreId())).thenReturn(Optional.of(store));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/store/getById")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(store)))
		        .andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("200 OK"))
				.andExpect(jsonPath("$.result[0].storeId").value(store.getStoreId())).andDo(print());
	}

	/*@Test
	@Transactional
	void testGetAllStoreByUser() throws Exception {
		UserRequest userReq = new UserRequest(user.getEmail());
		Mockito.when(storeService.findAllByUserAndStatus(user,false)).thenReturn(mockStores);
		
		mockMvc.perform(MockMvcRequestBuilders.post("/api/store/getAllByUser")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userReq)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("200 OK")).andDo(print());

	}

	

	@Test
	@Transactional
	void testCreateStore() throws Exception {

		Mockito.when(storeService.findById(store.getStoreId())).thenReturn(Optional.of(store));
		
		// MockMultipartFile for image file
		MockMultipartFile imageFile = new MockMultipartFile("image", "store.jpg", "image/jpg", "store".getBytes());

		// MockMultipartFile for user JSON
	
		MockMultipartFile storeReq = new MockMultipartFile("store", "store", MediaType.APPLICATION_JSON_VALUE,
				objectMapper.writeValueAsBytes(store));

		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/store/create").file(storeReq).file(imageFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Store created successfully."))
				.andExpect(jsonPath("$.result[0].storeName").value(store.getStoreName())).andDo(print());

	}

	@Test
	@Transactional
	void testUpdateStore() throws Exception {
		
		Mockito.when(storeService.findById(store.getStoreId())).thenReturn(Optional.of(store));
		// MockMultipartFile for image file
		MockMultipartFile imageFile = new MockMultipartFile("image", "store.jpg", "image/jpg", "store".getBytes());

		// MockMultipartFile for user JSON
		MockMultipartFile storeReq = new MockMultipartFile("store", "store", MediaType.APPLICATION_JSON_VALUE,
				objectMapper.writeValueAsBytes(store));

		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/store/update").file(storeReq).file(imageFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Store updated successfully."))
				.andExpect(jsonPath("$.result[0].storeName").value(store.getStoreName())).andDo(print());

	}
*/
}
