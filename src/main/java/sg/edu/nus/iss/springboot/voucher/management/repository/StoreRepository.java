package sg.edu.nus.iss.springboot.voucher.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.springboot.voucher.management.entity.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, String>{

	List<Store>findByIsDeletedFalse();
	
	Store findBystoreName(String storename);
	
	Store save (Store store);
}
