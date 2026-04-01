package com.multigenesis.ecomm_assesment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.multigenesis.ecomm_assesment.payload.AuthenticationResult;
import com.multigenesis.ecomm_assesment.payload.UserResponse;
import com.multigenesis.ecomm_assesment.security.request.LoginRequest;
import com.multigenesis.ecomm_assesment.security.request.SignupRequest;
import com.multigenesis.ecomm_assesment.security.response.MessageResponse;
import com.multigenesis.ecomm_assesment.security.response.UserInfoResponse;

public interface AuthService {
	
	    AuthenticationResult login(LoginRequest loginRequest);

	    ResponseEntity<MessageResponse> register(SignupRequest signUpRequest);

	    UserInfoResponse getCurrentUserDetails(Authentication authentication);

	    ResponseCookie logoutUser();

	    UserResponse getAllSellers(Pageable pageable);
}
