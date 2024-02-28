package sg.edu.nus.iss.springboot.voucher.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.iss.springboot.voucher.management.entity.Feed;

public interface FeedRepository extends JpaRepository<Feed, String>{
   
}
