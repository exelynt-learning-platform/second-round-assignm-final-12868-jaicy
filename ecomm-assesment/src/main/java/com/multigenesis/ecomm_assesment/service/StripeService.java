package com.multigenesis.ecomm_assesment.service;

import com.multigenesis.ecomm_assesment.payload.StripePaymentDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface StripeService {

    PaymentIntent paymentIntent(StripePaymentDto stripePaymentDto) throws StripeException;
    
    String createPaymentIntent(StripePaymentDto stripePaymentDto);
}
