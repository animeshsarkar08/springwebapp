package com.sarkaranimesh.gadgetgalaxy.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.sarkaranimesh.gadgetgalaxy.models.AppUser;
import com.sarkaranimesh.gadgetgalaxy.models.RegisterDto;
import com.sarkaranimesh.gadgetgalaxy.repositories.AppUserRepository;

import jakarta.validation.Valid;

@Controller
public class AccountController {
	
	@Autowired
	private AppUserRepository repo;
	
	@GetMapping("/register")
	public String register(Model model) {
		RegisterDto registerDto = new RegisterDto();
		model.addAttribute(registerDto);
		model.addAttribute("success",false);
		return "register";
	}
	
	@PostMapping("/register") 
	public String register(Model model, 
			@Valid @ModelAttribute RegisterDto registerDto, BindingResult result){
			
		if(!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
			result.addError(new FieldError("registerDto","confirmPassword","Please Enter the same password."));
		}
		
		AppUser appUser = repo.findByEmail(registerDto.getEmail());
		if(appUser != null) {
			result.addError(new FieldError("registerDto","email","Email is already used."));
		}
		
		if(result.hasErrors()) {
			return "register";
		}
		
		try {
			//create new account
			var bCryptEncoder = new BCryptPasswordEncoder();
			
			AppUser newUser = new AppUser();
			newUser.setUsername(registerDto.getUsername());
			newUser.setEmail(registerDto.getEmail());
			newUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));//hashing
			newUser.setCreatedAt(new Date());
			
			repo.save(newUser);
			
			model.addAttribute("registerDto", new RegisterDto());
			model.addAttribute("success",true);
			
		}catch(Exception ex) {
			result.addError(new FieldError("registerDto","username",ex.getMessage()));
		}
		
		return "register";
	}

}
