package com.multigenesis.ecomm_assesment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.multigenesis.ecomm_assesment.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	@Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o")
    Double getTotalRevenue();

}
