package com.multigenesis.ecomm_assesment.payload;

import java.util.Map;

import com.multigenesis.ecomm_assesment.model.Address;

import lombok.Data;

@Data
public class StripePaymentDto {
    private Long amount;
    private String currency;
    private String email;
    private String name;
    private Address address;
    private String description;
    private Map<String, String> metadata;
}
