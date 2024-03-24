package sg.edu.nus.iss.springboot.voucher.management.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import sg.edu.nus.iss.springboot.voucher.management.dto.StoreDTO;
import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;

public interface IStoreService {
	
	Map<Long, List<StoreDTO>> findAllByUserAndStatus(User createdBy, boolean isDeleted,Pageable pageable);
	
	StoreDTO findByStoreId(String storeId);
	
	StoreDTO findByStoreName(String storename);
	
	StoreDTO create(Store store, MultipartFile uploadFile);
	
	StoreDTO update(Store store, MultipartFile uploadFile);
	
	Map<Long, List<StoreDTO>> findByIsDeletedFalse(Pageable pageable) ;
	
	Store uploadImage(Store store, MultipartFile uploadFile);
}
