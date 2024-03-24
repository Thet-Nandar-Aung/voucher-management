package sg.edu.nus.iss.springboot.voucher.management.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.springboot.voucher.management.entity.Store;
import sg.edu.nus.iss.springboot.voucher.management.entity.User;

@Repository
public interface StoreRepository extends JpaRepository<Store, String>{
	
	Store findBystoreName(String storename);
	
	Store save (Store store);

	@Query("SELECT s FROM Store s WHERE s.createdBy = :createdBy AND s.isDeleted = :isDeleted")
	Page<Store> findAllByUserAndStatus(@Param("createdBy") User createdBy, @Param("isDeleted") boolean isDeleted, Pageable pageable);

	
	Page<Store> findByIsDeletedFalse(Pageable pageable);
	   
}
