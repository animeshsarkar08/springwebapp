package com.sarkaranimesh.gadgetgalaxy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		return http 
				 .csrf().disable()//temporary disable authentication for other pages like edit and create
				.authorizeHttpRequests(auth-> auth
						.requestMatchers("/").permitAll()
						.requestMatchers("/products/**").permitAll()
						.requestMatchers("/register").permitAll()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/logout").permitAll()
						.requestMatchers("/edit").permitAll()
						.requestMatchers("/create").permitAll()
						.requestMatchers("/delete").permitAll()
						.anyRequest().authenticated()
						)
						.formLogin(form -> form
								.defaultSuccessUrl("/products",true)
						)
						.logout(config -> config.logoutSuccessUrl("/"))
						.build();
	}
	
	@Bean public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
		auth.inMemoryAuthentication()
			.withUser("animesh@gmail.com")
			.password("{noop}Animesh@123")
			.roles("USER")
	}
	

}
