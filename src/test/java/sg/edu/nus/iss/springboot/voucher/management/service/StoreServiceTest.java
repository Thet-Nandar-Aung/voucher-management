package sg.edu.nus.iss.springboot.voucher.management.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;
import sg.edu.nus.iss.springboot.voucher.management.enums.RoleType;
import sg.edu.nus.iss.springboot.voucher.management.repository.StoreRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.impl.StoreService;

@SpringBootTest
public class StoreServiceTest {

	@Mock
	private StoreRepository storeRepository;

	@InjectMocks
	private StoreService storeService;

	
	private static User user = new User("antonia@gmail.com", "Antonia", "Pwd@21212", RoleType.MERCHANT, true);
	
	private static Store store = new Store("1","MUJI",
			"MUJI offers a wide variety of good quality items from stationery to household items and apparel.",
			"Test", "#04-36/40 Paragon Shopping Centre", "290 Orchard Rd", "", "238859", "Singapore", "Singapore",
			"Singapore", null, null, null, user, null, user, false, null);

	private static List<Store> mockStores = new ArrayList<>();

	@BeforeEach
	void setUp() {

		mockStores.add(store);
	}

	@Test
	void getAllActiveStore() {
		Mockito.when(storeRepository.findByIsDeletedFalse()).thenReturn(mockStores);
		List<Store> storeList = storeService.findByIsDeletedFalse();
		assertEquals(mockStores.size(), storeList.size());
		assertEquals(mockStores.get(0).getStoreName(), storeList.get(0).getStoreName());

	}

	@Test
	void getAllActiveStoreByUser() {
		Mockito.when(storeRepository.findAllByUserAndStatus(user, false)).thenReturn(mockStores);
		List<Store> storeList = storeService.findAllByUserAndStatus(user, false);
		assertEquals(mockStores.size(), storeList.size());
		assertEquals(mockStores.get(0).getStoreName(), storeList.get(0).getStoreName());

	}

	@Test
	void getByStoreId() {
		store.setStoreId("11");
		Mockito.when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
		Optional<Store> dbStore = storeService.findById(store.getStoreId());
		assertThat(store).isNotNull();
		assertThat(dbStore.get().getStoreName()).isEqualTo("MUJI");
	}

	@Test
	void createStore() {

		Mockito.when(storeRepository.save(Mockito.any(Store.class))).thenReturn(store);
		Mockito.when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
		Store createdStore = storeService.create(store);
		assertThat(createdStore).isNotNull();
		assertThat(createdStore.getStoreName().equals("MUJI")).isTrue();

	}

	@Test
	void updateStore() {
		store.setAddress1("Paragon Shopping Centre");
		store.setContactNumber("+65 238859");

		Mockito.when(storeRepository.save(Mockito.any(Store.class))).thenReturn(store);
		Mockito.when(storeRepository.findById(store.getStoreId())).thenReturn(Optional.of(store));
		Store createdStore = storeService.update(store);
		assertThat(createdStore).isNotNull();
		assertThat(createdStore.getStoreName().equals("MUJI")).isTrue();

	}

}
