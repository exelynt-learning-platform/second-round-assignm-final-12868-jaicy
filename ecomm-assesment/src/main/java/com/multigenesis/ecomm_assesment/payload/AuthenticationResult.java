package com.multigenesis.ecomm_assesment.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseCookie;

import com.multigenesis.ecomm_assesment.security.response.UserInfoResponse;

@Data
@AllArgsConstructor
public class AuthenticationResult {
    private final UserInfoResponse response;
    private final ResponseCookie jwtCookie;
}