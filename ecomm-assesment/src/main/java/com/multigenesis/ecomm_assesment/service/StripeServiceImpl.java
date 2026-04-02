package com.multigenesis.ecomm_assesment.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.multigenesis.ecomm_assesment.exceptions.APIException;
import com.multigenesis.ecomm_assesment.payload.StripePaymentDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.annotation.PostConstruct;


@Service
@Transactional
public class StripeServiceImpl implements StripeService {

	@Value("${stripe.secret.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init(){
    	if (stripeApiKey == null || stripeApiKey.isBlank()) {
            throw new IllegalStateException("Stripe API key is missing");
        }
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public PaymentIntent paymentIntent(StripePaymentDto stripePaymentDto) throws StripeException {
        Customer customer;
        CustomerSearchParams searchParams =
                CustomerSearchParams.builder()
                        .setQuery("email:'" + stripePaymentDto.getEmail() + "'")
                        .build();
        CustomerSearchResult customers = Customer.search(searchParams);
        if (customers.getData().isEmpty()) {
            CustomerCreateParams customerParams = CustomerCreateParams.builder()
                    .setEmail(stripePaymentDto.getEmail())
                    .setName(stripePaymentDto.getName())
                    .setAddress(
                            CustomerCreateParams.Address.builder()
                                    .setLine1(stripePaymentDto.getAddress().getStreet())
                                    .setCity(stripePaymentDto.getAddress().getCity())
                                    .setState(stripePaymentDto.getAddress().getState())
                                    .setPostalCode(stripePaymentDto.getAddress().getPincode())
                                    .setCountry(stripePaymentDto.getAddress().getCountry())
                                    .build()
                    )
                    .build();

            customer = Customer.create(customerParams);
        } else {
            customer = customers.getData().get(0);
        }

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(stripePaymentDto.getAmount())
                        .setCurrency(stripePaymentDto.getCurrency())
                        .setCustomer(customer.getId())
                        .setDescription(stripePaymentDto.getDescription())
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();

        return PaymentIntent.create(params);
    }
    
    @Override
    public String createPaymentIntent(StripePaymentDto dto) {

        try {
            validateDto(dto);

            Customer customer = getOrCreateCustomer(dto);

            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(dto.getAmount()) // must be in cents
                            .setCurrency(dto.getCurrency())
                            .setCustomer(customer.getId())
                            .setDescription(dto.getDescription())
                            .setAutomaticPaymentMethods(
                                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                            .setEnabled(true)
                                            .build()
                            )
                            .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return paymentIntent.getClientSecret();

        } catch (StripeException e) {
            throw new APIException("Stripe error: " + e.getMessage());
        }
    }

    private Customer getOrCreateCustomer(StripePaymentDto dto) throws StripeException {
        CustomerSearchParams searchParams =
                CustomerSearchParams.builder()
                        .setQuery("email:'" + dto.getEmail() + "'")
                        .build();

        CustomerSearchResult customers = Customer.search(searchParams);

        if (!customers.getData().isEmpty()) {
            return customers.getData().get(0);
        }

        CustomerCreateParams customerParams =
                CustomerCreateParams.builder()
                        .setEmail(dto.getEmail())
                        .setName(dto.getName())
                        .build();

        return Customer.create(customerParams);
    }

    private void validateDto(StripePaymentDto dto) {
        if (dto == null) {
            throw new APIException("Payment data cannot be null");
        }

        if (dto.getAmount() == null || dto.getAmount() <= 0) {
            throw new APIException("Invalid payment amount");
        }

        if (dto.getCurrency() == null || dto.getCurrency().isBlank()) {
            throw new APIException("Currency is required");
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new APIException("Customer email is required");
        }
    }
}


