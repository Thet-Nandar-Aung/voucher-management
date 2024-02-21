package sg.edu.nus.iss.springboot.voucher.management.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.StoreService;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "DB_USERNAME=admin", "DB_PASSWORD=RDS_12345", "AWS_ACCESS_KEY=AKIA47CRXTTV2EHMAA3S",
		"AWS_SECRET_KEY=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" })
public class StoreControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private StoreService storeService;

	Store testStore;

	@BeforeEach
	void setUp() {

		testStore = new Store("MUJI",
				"MUJI offers a wide variety of good quality items from stationery to household items and apparel.",
				"Test", "#04-36/40 Paragon Shopping Centre", "290 Orchard Rd", "", "238859", "Singapore", "Singapore",
				"Singapore", false);
	}

	@Test
	@Transactional
	void testGetAllActiveStore() throws Exception {
		storeService.create(testStore);

		mockMvc.perform(MockMvcRequestBuilders.get("/api/store/getAll"))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.message").value("200 OK"))
				.andDo(print());
	}

	@Test
	@Transactional
	void testCreateStore() throws Exception {

		// MockMultipartFile for image file
		MockMultipartFile imageFile = new MockMultipartFile("image", "store.jpg", "image/jpg", "store".getBytes());

		// MockMultipartFile for user JSON
		testStore.setCreatedBy("Eleven.11stt@gmail.com");
		MockMultipartFile store = new MockMultipartFile("store", "store", MediaType.APPLICATION_JSON_VALUE,
				objectMapper.writeValueAsBytes(testStore));

		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/store/create").file(store).file(imageFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Store created successfully."))
				.andExpect(jsonPath("$.result[0].storeName").value(testStore.getStoreName())).andDo(print());

	}

	@Test
	@Transactional
	void testUpdateStore() throws Exception {

		Store createdStore = storeService.create(testStore);
		createdStore.setDeleted(true);
		createdStore.setUpdatedBy("Eleven.11stt@gmail.com");

		// MockMultipartFile for image file
		MockMultipartFile imageFile = new MockMultipartFile("image", "store.jpg", "image/jpg", "store".getBytes());

		// MockMultipartFile for user JSON
		MockMultipartFile store = new MockMultipartFile("store", "store", MediaType.APPLICATION_JSON_VALUE,
				objectMapper.writeValueAsBytes(createdStore));

		mockMvc.perform(MockMvcRequestBuilders.multipart("/api/store/update").file(store).file(imageFile)
				.contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.message").value("Store updated successfully."))
				.andExpect(jsonPath("$.result[0].storeName").value(testStore.getStoreName())).andDo(print());

	}

}
