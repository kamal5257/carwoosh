package com.carwoosh.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.carwoosh.entity.User;
import com.carwoosh.repository.UserRepository;



@Service
public class MyUserDetailService implements UserDetailsService {

	@Autowired
	UserRepository repo;

//	@Override
//	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
////		Optional<AuthUser> user = repo.findByUserId(Integer.valueOf(userId));
////
////		AuthUser u = user.orElseThrow(() -> new UsernameNotFoundException("User not found"));
////		return new MyUserDetails(u);
//		MyUserDetails details = null;
//		try {
//			AuthUser user1 = repo.findByUserName(userId);
//			details = new MyUserDetails(user1);
//		} catch (UsernameNotFoundException e) {
//
//		}
//		return details;
//	}
	
	@Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        //Logic to get the user form the Database
		User user1 = repo.findByUserName(userId);
		return new MyUserDetails(user1);
    }

}
