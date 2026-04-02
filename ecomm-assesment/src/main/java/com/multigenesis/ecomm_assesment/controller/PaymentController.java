package com.multigenesis.ecomm_assesment.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multigenesis.ecomm_assesment.payload.StripePaymentDto;
import com.multigenesis.ecomm_assesment.service.StripeService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
	@Autowired
    private StripeService stripeService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody StripePaymentDto dto) {

        String clientSecret = stripeService.createPaymentIntent(dto);

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", clientSecret);

        return ResponseEntity.ok(response);
    }
}
