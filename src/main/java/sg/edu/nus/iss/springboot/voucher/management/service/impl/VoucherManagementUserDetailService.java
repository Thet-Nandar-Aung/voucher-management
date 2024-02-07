package sg.edu.nus.iss.springboot.voucher.management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import sg.edu.nus.iss.springboot.voucher.management.repository.*;
import sg.edu.nus.iss.springboot.voucher.management.entity.*;

@Component
public class VoucherManagementUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return userRepository.findByUsername(username).map(VoucherManagementUserDetail::new)
				.orElseThrow(() -> new UsernameNotFoundException("No user found"));
	}

}
