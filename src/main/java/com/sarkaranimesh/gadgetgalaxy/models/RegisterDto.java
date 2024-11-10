package com.sarkaranimesh.gadgetgalaxy.models;

import jakarta.validation.constraints.*;

public class RegisterDto {

	@NotEmpty
	private String username;
	
	@NotEmpty
	private String email;
	
	@Size(min = 6, message = "Minimum 6 characters")
	private String password;
	
	private String confirmPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
