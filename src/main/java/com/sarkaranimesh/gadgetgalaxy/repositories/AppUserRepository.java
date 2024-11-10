package com.sarkaranimesh.gadgetgalaxy.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sarkaranimesh.gadgetgalaxy.models.AppUser;


public interface AppUserRepository extends JpaRepository<AppUser, Integer>{
	public AppUser findByEmail(String email);
}
