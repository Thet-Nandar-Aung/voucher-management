package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.repository.StoreRepository;
import sg.edu.nus.iss.springboot.voucher.management.service.IStoreService;

@Service
public class StoreService implements IStoreService {

	private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

	@Autowired
	private StoreRepository storeRepository;

	@Override
	public List<Store> findByIsDeletedFalse() {
		// TODO Auto-generated method stub
		return storeRepository.findByIsDeletedFalse();
	}

	@Override
	public Store create(Store store) {
		// TODO Auto-generated method stub
		try {
			store.setCreatedDate(LocalDateTime.now());
			return storeRepository.save(store);
		} catch (Exception e) {
			logger.error("Error occurred while user creating, " + e.toString());
			e.printStackTrace();

		}

		return new Store();
	}

	@Override
	public Store update(Store store) {
		// TODO Auto-generated method stub
		try {
			store.setUpdatedDate(LocalDateTime.now());
			return storeRepository.save(store);
		} catch (Exception e) {
			logger.error("Error occurred while user creating, " + e.toString());
			e.printStackTrace();

		}

		return new Store();
	}

	@Override
	public Store findByStoreName(String storename) {
		// TODO Auto-generated method stub
		return storeRepository.findBystoreName(storename);
	}

	@Override
	public Optional<Store> findById(String storeId) {
		// TODO Auto-generated method stub
		return storeRepository.findById(storeId);
	}

	@Override
	public List<Store> findAllByUserAndStatus(String created_by, boolean isDeleted) {
		// TODO Auto-generated method stub
		return storeRepository.findAllByUserAndStatus(created_by, isDeleted);
	}

}
