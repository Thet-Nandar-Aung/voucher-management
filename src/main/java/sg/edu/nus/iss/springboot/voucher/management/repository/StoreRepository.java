package sg.edu.nus.iss.springboot.voucher.management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;

@Repository
public interface StoreRepository extends JpaRepository<Store, String>{

	List<Store>findByIsDeletedFalse();
	
	Store findBystoreName(String storename);
	
	Store save (Store store);
	
	@Query("SELECT s FROM Store s WHERE s.createdBy = ?1 AND s.isDeleted = ?2")
	List<Store> findAllByUserAndStatus(User createdBy, boolean isDeleted);

}
