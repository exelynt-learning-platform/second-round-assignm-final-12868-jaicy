package com.multigenesis.ecomm_assesment.service;

import org.springframework.transaction.annotation.Transactional;

import com.multigenesis.ecomm_assesment.payload.OrderDTO;
import com.multigenesis.ecomm_assesment.payload.OrderResponse;

public interface OrderService {
	@Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);

    OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    OrderDTO updateOrder(Long orderId, String status);

    OrderResponse getAllSellerOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

}
