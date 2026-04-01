package com.multigenesis.ecomm_assesment.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.multigenesis.ecomm_assesment.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
