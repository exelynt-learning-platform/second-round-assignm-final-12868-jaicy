package com.multigenesis.ecomm_assesment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multigenesis.ecomm_assesment.service.WebhookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;



@RestController
@RequestMapping("/api/webhook")
public class StripeWebhookController {
	@Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Autowired
    private WebhookService webhookService;

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        webhookService.handleEvent(event);

        return ResponseEntity.ok("Success");
    }
}

