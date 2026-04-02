package com.multigenesis.ecomm_assesment.model;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long orderItemId;

	    @ManyToOne
	    @JoinColumn(name = "product_id")
	    private Product product;

	    @ManyToOne
	    @JoinColumn(name = "order_id")
	    private Order order;

	    private Integer quantity;
	    private double discount;
	    private double orderedProductPrice;
}
