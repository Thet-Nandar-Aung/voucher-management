package sg.edu.nus.iss.springboot.voucher.management.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.springboot.voucher.management.dto.StoreDTO;
import sg.edu.nus.iss.springboot.voucher.management.dto.UserRequest;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.StoreService;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.UserService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "DB_USERNAME=admin",
        "DB_PASSWORD=RDS_12345",
        "AWS_ACCESS_KEY=AKIA47CRXTTV2EHMAA3S",
        "AWS_SECRET_KEY=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",
        "AES_SECRET_KEY=",
        "FRONTEND_URL="
})
public class StoreControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@MockBean
	private StoreService storeService;

	private static List<StoreDTO> mockStores = new ArrayList<>();

	private static User user = new User("antonia@gmail.com", "Antonia", "Pwd@21212", RoleType.MERCHANT, true);

	private static Store store1 = new Store("1", "MUJI",
			"MUJI offers a wide variety of good quality items from stationery to household items and apparel.", "Test",
			"#04-36/40 Paragon Shopping Centre", "290 Orchard Rd", "", "238859", "Singapore", "Singapore", "Singapore",
			null, null, null, user, null, user, false, null);
	private static Store store2 = new Store("2", "MUJI2",
			"MUJI offers a wide variety of good quality items from stationery to household items and apparel.", "Test",
			"#01-11/15  Shopping Centre", "11 Rd", "", "111231", "Singapore", "Singapore", "Singapore", null, null,
			null, user, null, user, false, null);

	@BeforeAll
	static void setUp() {

		mockStores.add(DTOMapper.toStoreDTO(store1));
		mockStores.add(DTOMapper.toStoreDTO(store2));
	}

	@Test
	void testGetStoreById() throws Exception {
		Mockito.when(storeService.findByStoreId(store1.getStoreId())).thenReturn(DTOMapper.toStoreDTO(store1));
		mockMvc.perform(MockMvcRequestBuilders.post("/api/store/getById").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(store1))).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true)).andDo(print());
	}

	/*
	@Test
	void testGetAllActiveStore() throws Exception {
		// Mock page data
		Pageable pageable = PageRequest.of(0, 1);
		Map<Long, List<StoreDTO>> mockStoreMap = new HashMap<>();
		mockStoreMap.put(0L, mockStores);

		// Mock service method
		Mockito.when(storeService.findByIsDeletedFalse(pageable)).thenReturn(mockStoreMap);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/store/getAll").param("page", "0").param("size", "1")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value("Successfully get all active store.")).andDo(print());
	}

	@Test
	void testGetAllStoreByUser() throws Exception {
		Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(user);
		// Mock page data
		Pageable pageable = PageRequest.of(0, 1);
		Map<Long, List<StoreDTO>> mockStoreMap = new HashMap<>();
		mockStores.get(0).setCreatedBy(DTOMapper.toUserDTO(user));
		mockStoreMap.put(0L, mockStores);

		// Mock service method
		Mockito.when(storeService.findAllByUserAndStatus(user, false, pageable)).thenReturn(mockStoreMap);
		UserRequest userReq = new UserRequest(user.getEmail());
		mockMvc.perform(MockMvcRequestBuilders.post("/api/store/getAllByUser").param("page", "0").param("size", "1")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userReq)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.success").value(true)).andDo(print());

	}

	@Test
	void testCreateStore() throws Exception {

		//Mockito.when(storeService.findByStoreId(store1.getStoreId())).thenReturn(DTOMapper.toStoreDTO(store1));
		Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(user);

		// MockMultipartFile for image file
		MockMultipartFile imageFile = new MockMultipartFile("image", "store.jpg", "image/jpg", "store".getBytes());

		// MockMultipartFile for user JSON

		MockMultipartFile storeReq = new MockMultipartFile("store", "store", MediaType.APPLICATION_JSON_VALUE,
				objectMapper.writeValueAsBytes(store1));

		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/store/create").file(storeReq).file(imageFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Store created successfully."))
				.andExpect(jsonPath("$.result[0].storeName").value(store1.getStoreName())).andDo(print());

	}

	@Test
	void testUpdateStore() throws Exception {

		Mockito.when(storeService.findByStoreId(store1.getStoreId())).thenReturn(DTOMapper.toStoreDTO(store1));
		Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(user);

		
		// MockMultipartFile for image file
		MockMultipartFile imageFile = new MockMultipartFile("image", "store.jpg", "image/jpg", "store".getBytes());

		// MockMultipartFile for user JSON
		MockMultipartFile storeReq = new MockMultipartFile("store", "store", MediaType.APPLICATION_JSON_VALUE,
				objectMapper.writeValueAsBytes(store1));

		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/store/update").file(storeReq).file(imageFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Store updated successfully."))
				.andExpect(jsonPath("$.result[0].storeName").value(store1.getStoreName())).andDo(print());

	}*/

}
