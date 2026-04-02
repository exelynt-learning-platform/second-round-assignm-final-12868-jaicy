package com.multigenesis.ecomm_assesment.security.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.multigenesis.ecomm_assesment.security.jwt.AuthEntryPointJwt;
import com.multigenesis.ecomm_assesment.security.jwt.AuthTokenFilter;
import com.multigenesis.ecomm_assesment.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {
	
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider =new DaoAuthenticationProvider(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)throws Exception{
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	 @Bean
	 public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        http.csrf(csrf -> csrf.disable())
	                .cors(cors -> {})
	                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
	                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .authorizeHttpRequests(auth ->
	                        auth.requestMatchers("/api/auth/**").permitAll()
	                                .requestMatchers("/v3/api-docs/**").permitAll()
	                                .requestMatchers("/h2-console/**").permitAll()
	                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
	                                .requestMatchers("/api/seller/**").hasAnyRole("ADMIN","SELLER")
	                                .requestMatchers("/api/public/**").permitAll()
	                                .requestMatchers("/api/webhook/**").permitAll()
	                                .requestMatchers("/swagger-ui/**").permitAll()
	                                .requestMatchers("/api/test/**").permitAll()
	                                .requestMatchers("/images/**").permitAll()
	                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	                                .anyRequest().authenticated()
	                );

	        http.authenticationProvider(authenticationProvider());

	        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	        http.headers(headers -> headers.frameOptions(
	                frameOptions -> frameOptions.sameOrigin()));

	        return http.build();
	    }
	 
	 @Bean
	  public WebSecurityCustomizer webSecurityCustomizer() {
	       return (web -> web.ignoring().requestMatchers("/v2/api-docs",
	              "/configuration/ui",
	              "/swagger-resources/**",
	              "/configuration/security",
	              "/swagger-ui.html",
	              "/webjars/**"));
	   }
	

}
