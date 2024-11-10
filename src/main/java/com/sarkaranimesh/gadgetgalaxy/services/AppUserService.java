package com.sarkaranimesh.gadgetgalaxy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sarkaranimesh.gadgetgalaxy.models.AppUser;
import com.sarkaranimesh.gadgetgalaxy.repositories.AppUserRepository;

@Service
public class AppUserService implements UserDetailsService {
	
	@Autowired
	private AppUserRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		AppUser appUser = repo.findByEmail(email);
		
		if(appUser != null) {
			//these function are defined in models
			var springUser = User.withUsername(appUser.getEmail())
					.password(appUser.getPassword())
					.build();
			
			return springUser;
		}
		
		return null;
				
	}

}
