package com.multigenesis.ecomm_assesment.repositories;

import org.springframework.stereotype.Repository;

import com.multigenesis.ecomm_assesment.model.OrderItem;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

}
