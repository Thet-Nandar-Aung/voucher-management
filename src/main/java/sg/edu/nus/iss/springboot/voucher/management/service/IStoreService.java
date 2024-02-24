package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;
import java.util.Optional;

import sg.edu.nus.iss.springboot.voucher.management.entity.Store;

public interface IStoreService {

	List<Store> findByIsDeletedFalse();
	
	List<Store>findAllByUserAndStatus(String created_by, boolean isDeleted);
	
	Store findByStoreName(String storename);
	
	Optional<Store>  findById(String storeId);
	
	Store create(Store store);
	
	Store update(Store store);
	
	
}
