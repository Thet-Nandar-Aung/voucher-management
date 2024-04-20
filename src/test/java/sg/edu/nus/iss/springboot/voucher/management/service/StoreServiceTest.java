package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.nus.iss.springboot.voucher.management.dto.StoreDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.StoreRepository;
import sg.edu.nus.iss.springboot.voucher.management.repository.UserRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.StoreService;
import sg.edu.nus.iss.springboot.voucher.management.utility.DTOMapper;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class StoreServiceTest {

	@MockBean
	private StoreRepository storeRepository;

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private StoreService storeService;

	private static User user = new User("antonia@gmail.com", "Antonia", "Pwd@21212", RoleType.MERCHANT, true);

	private static Store store = new Store("1", "MUJI",
			"MUJI offers a wide variety of good quality items from stationery to household items and apparel.", "Test",
			"#04-36/40 Paragon Shopping Centre", "290 Orchard Rd", "", "238859", "Singapore", "Singapore", "Singapore",
			null, null, null, user, null, user, false, null);

	private static List<Store> mockStores = new ArrayList<>();

	@BeforeEach
	void setUp() {

		mockStores.add(store);
	}

	@Test
	void getAllActiveStore() {
		long totalRecord = 0;
		List<StoreDTO> storeDTOList = new ArrayList<StoreDTO>();
		Pageable pageable = PageRequest.of(0, 10);
		Page<Store> mockStoresPage = new PageImpl<>(mockStores, pageable, mockStores.size());

		Mockito.when(storeRepository.findByIsDeletedFalse(pageable)).thenReturn(mockStoresPage);

		Map<Long, List<StoreDTO>> storePages = storeService.findByIsDeletedFalse(pageable);
		for (Map.Entry<Long, List<StoreDTO>> entry : storePages.entrySet()) {
			totalRecord = entry.getKey();
			storeDTOList = entry.getValue();

		}

		assertThat(totalRecord).isGreaterThan(0);
		assertThat(storeDTOList.get(0).getStoreName()).isEqualTo("MUJI");
	}

	@Test
	void getAllActiveStoreByUser() {
		long totalRecord = 0;
		List<StoreDTO> storeDTOList = new ArrayList<StoreDTO>();
		Pageable pageable = PageRequest.of(0, 10);
		Page<Store> mockStoresPage = new PageImpl<>(mockStores, pageable, mockStores.size());

		Mockito.when(storeRepository.findAllByUserAndStatus(user, false, pageable)).thenReturn(mockStoresPage);

		Map<Long, List<StoreDTO>> storePage = storeService.findAllByUserAndStatus(user, false, pageable);
		for (Map.Entry<Long, List<StoreDTO>> entry : storePage.entrySet()) {
			totalRecord = entry.getKey();
			storeDTOList = entry.getValue();

		}

		assertThat(totalRecord).isGreaterThan(0);
		assertThat(storeDTOList.get(0).getStoreName()).isEqualTo("MUJI");
	}

	@Test
	void getByStoreId() {
		store.setStoreId("11");
		Mockito.when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
		Optional<Store> dbStore = storeRepository.findById(store.getStoreId());
		assertThat(store).isNotNull();
		assertThat(dbStore.get().getStoreName()).isEqualTo("MUJI");
	}

	@Test
	void testCreateStore() {
		Mockito.when(storeRepository.save(Mockito.any(Store.class))).thenReturn(store);
		Mockito.when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
		Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
		MockMultipartFile imageFile = new MockMultipartFile("image", "store.jpg", "image/jpg", "store".getBytes());

		StoreDTO storeDTO = storeService.create(store, imageFile);

		assertThat(storeDTO).isNotNull();
		assertEquals(storeDTO.getDescription(), store.getDescription());

	}

	@Test
	void testUpdateStore() {
		store.setAddress1("Paragon Shopping Centre");
		store.setContactNumber("+65 238859");

		Mockito.when(storeRepository.save(Mockito.any(Store.class))).thenReturn(store);
		Mockito.when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
		MockMultipartFile imageFile = new MockMultipartFile("image", "store.jpg", "image/jpg", "store".getBytes());

		StoreDTO storeDTO = storeService.update(store, imageFile);
		assertThat(storeDTO).isNotNull();
		assertEquals(storeDTO.getDescription(), store.getDescription());
	}

	@Test
	void findByStoreId() {

		Mockito.when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
		StoreDTO storeDTO = storeService.findByStoreId(store.getStoreId());
		assertThat(storeDTO).isNotNull();
		assertEquals(storeDTO.getDescription(), store.getDescription());

	}

}
