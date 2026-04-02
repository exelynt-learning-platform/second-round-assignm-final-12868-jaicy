package com.multigenesis.ecomm_assesment.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.multigenesis.ecomm_assesment.exceptions.APIException;
import com.multigenesis.ecomm_assesment.exceptions.ResourceNotFoundException;
import com.multigenesis.ecomm_assesment.model.Address;
import com.multigenesis.ecomm_assesment.model.Cart;
import com.multigenesis.ecomm_assesment.model.CartItem;
import com.multigenesis.ecomm_assesment.model.Order;
import com.multigenesis.ecomm_assesment.model.OrderItem;
import com.multigenesis.ecomm_assesment.model.Payment;
import com.multigenesis.ecomm_assesment.model.Product;
import com.multigenesis.ecomm_assesment.model.User;
import com.multigenesis.ecomm_assesment.payload.OrderDTO;
import com.multigenesis.ecomm_assesment.payload.OrderItemDTO;
import com.multigenesis.ecomm_assesment.payload.OrderResponse;
import com.multigenesis.ecomm_assesment.repositories.AddressRepository;
import com.multigenesis.ecomm_assesment.repositories.CartRepository;
import com.multigenesis.ecomm_assesment.repositories.OrderItemRepository;
import com.multigenesis.ecomm_assesment.repositories.OrderRepository;
import com.multigenesis.ecomm_assesment.repositories.PaymentRepository;
import com.multigenesis.ecomm_assesment.repositories.ProductRepository;
import com.multigenesis.ecomm_assesment.utils.AuthUtil;

@Service
public class OrderServiceImpl implements OrderService {
	   
	    @Autowired
	    CartRepository cartRepository;

	    @Autowired
	    AddressRepository addressRepository;

	    @Autowired
	    OrderItemRepository orderItemRepository;

	    @Autowired
	    OrderRepository orderRepository;

	    @Autowired
	    PaymentRepository paymentRepository;

	    @Autowired
	    CartService cartService;

	    @Autowired
	    ModelMapper modelMapper;

	    @Autowired
	    ProductRepository productRepository;

	    @Autowired
	    AuthUtil authUtil;
//
//	    @Override
//	    @Transactional
//	    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
//	        Cart cart = cartRepository.findCartByEmail(emailId);
//	        if (cart == null) {
//	            throw new ResourceNotFoundException("Cart", "email", emailId);
//	        }
//
//	        Address address = addressRepository.findById(addressId)
//	                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
//
//	        Order order = new Order();
//	        order.setEmail(emailId);
//	        order.setOrderDate(LocalDate.now());
//	        order.setTotalAmount(cart.getTotalPrice());
//	        order.setOrderStatus("Accepted");
//	        order.setAddress(address);
//
//	        Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
//	        payment.setOrder(order);
//	        payment = paymentRepository.save(payment);
//	        order.setPayment(payment);
//
//	        Order savedOrder = orderRepository.save(order);
//
//	        List<CartItem> cartItems = cart.getCartItems();
//	        if (cartItems.isEmpty()) {
//	            throw new APIException("Cart is empty");
//	        }
//
//	        List<OrderItem> orderItems = new ArrayList<>();
//	        for (CartItem cartItem : cartItems) {
//	            OrderItem orderItem = new OrderItem();
//	            orderItem.setProduct(cartItem.getProduct());
//	            orderItem.setQuantity(cartItem.getQuantity());
//	            orderItem.setDiscount(cartItem.getDiscount());
//	            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
//	            orderItem.setOrder(savedOrder);
//	            orderItems.add(orderItem);
//	        }
//
//	        orderItems = orderItemRepository.saveAll(orderItems);
//
//	        cart.getCartItems().forEach(item -> {
//	            int quantity = item.getQuantity();
//	            Product product = item.getProduct();
//
//	            // Reduce stock quantity
//	            product.setQuantity(product.getQuantity() - quantity);
//
//	            // Save product back to the database
//	            productRepository.save(product);
//
//	            // Remove items from cart
//	            cartService.deleteProductFromCart(cart.getCartId(), item.getProduct().getProductId());
//	        });
//
//	        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
//	        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));
//
//	        orderDTO.setAddressId(addressId);
//
//	        return orderDTO;
//	    }
	    
	    @Override
	    @Transactional
	    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod,
	                               String pgName, String pgPaymentId,
	                               String pgStatus, String pgResponseMessage) {

	        Cart cart = getCartByEmail(emailId);
	        Address address = getAddressById(addressId);

	        Order order = createOrder(emailId, cart, address);
	        Payment payment = createAndSavePayment(order, paymentMethod, pgName, pgPaymentId, pgStatus, pgResponseMessage);

	        order.setPayment(payment);
	        Order savedOrder = orderRepository.save(order);

	        List<OrderItem> orderItems = createOrderItems(cart, savedOrder);

	        updateStockAndClearCart(cart);

	        return buildOrderDTO(savedOrder, orderItems, addressId);
	    }
	    
	    private Cart getCartByEmail(String emailId) {
	        Cart cart = cartRepository.findCartByEmail(emailId);
	        if (cart == null) {
	            throw new ResourceNotFoundException("Cart", "email", emailId);
	        }
	        if (cart.getCartItems().isEmpty()) {
	            throw new APIException("Cart is empty");
	        }
	        return cart;
	    }
	    
	    private Address getAddressById(Long addressId) {
	        return addressRepository.findById(addressId)
	                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
	    }
	    
	    private Order createOrder(String emailId, Cart cart, Address address) {
	        Order order = new Order();
	        order.setEmail(emailId);
	        order.setOrderDate(LocalDate.now());
	        order.setTotalAmount(cart.getTotalPrice());
	        order.setOrderStatus("Accepted");
	        order.setAddress(address);
	        return order;
	    }
	    
	    private Payment createAndSavePayment(Order order, String paymentMethod,
                String pgName, String pgPaymentId,
                String pgStatus, String pgResponseMessage) {

		Payment payment = new Payment(paymentMethod, pgPaymentId, pgStatus, pgResponseMessage, pgName);
		payment.setOrder(order);
		return paymentRepository.save(payment);
		}
	    private List<OrderItem> createOrderItems(Cart cart, Order order) {
	        List<OrderItem> orderItems = new ArrayList<>();

	        for (CartItem cartItem : cart.getCartItems()) {
	            OrderItem orderItem = new OrderItem();
	            orderItem.setProduct(cartItem.getProduct());
	            orderItem.setQuantity(cartItem.getQuantity());
	            orderItem.setDiscount(cartItem.getDiscount());
	            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
	            orderItem.setOrder(order);
	            orderItems.add(orderItem);
	        }

	        return orderItemRepository.saveAll(orderItems);
	    }
	    
	    private void updateStockAndClearCart(Cart cart) {
	        for (CartItem item : cart.getCartItems()) {
	            Product product = item.getProduct();
	            int quantity = item.getQuantity();
	            if (product.getQuantity() < quantity||product.getQuantity()==null) {
	                throw new APIException("Insufficient stock for product: " + product.getProductName());
	            }

	            product.setQuantity(product.getQuantity() - quantity);
	            productRepository.save(product);

	            cartService.deleteProductFromCart(cart.getCartId(), product.getProductId());
	        }
	    }
	    
	    
	    private OrderDTO buildOrderDTO(Order order, List<OrderItem> orderItems, Long addressId) {
	        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

	        orderItems.forEach(item ->
	                orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class))
	        );

	        orderDTO.setAddressId(addressId);
	        return orderDTO;
	    }

	    @Override
	    public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
	        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
	                ? Sort.by(sortBy).ascending()
	                : Sort.by(sortBy).descending();
	        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
	        Page<Order> pageOrders = orderRepository.findAll(pageDetails);
	        List<Order> orders = pageOrders.getContent();
	        List<OrderDTO> orderDTOs = orders.stream()
	                .map(order -> modelMapper.map(order, OrderDTO.class))
	                .toList();
	        OrderResponse orderResponse = new OrderResponse();
	        orderResponse.setContent(orderDTOs);
	        orderResponse.setPageNumber(pageOrders.getNumber());
	        orderResponse.setPageSize(pageOrders.getSize());
	        orderResponse.setTotalElements(pageOrders.getTotalElements());
	        orderResponse.setTotalPages(pageOrders.getTotalPages());
	        orderResponse.setLastPage(pageOrders.isLast());
	        return orderResponse;
	    }

	    @Override
	    public OrderDTO updateOrder(Long orderId, String status) {
	        Order order = orderRepository.findById(orderId)
	                .orElseThrow(() -> new ResourceNotFoundException("Order","orderId",orderId));
	        order.setOrderStatus(status);
	        orderRepository.save(order);
	        return modelMapper.map(order, OrderDTO.class);
	    }

	    @Override
	    public OrderResponse getAllSellerOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
	        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
	                ? Sort.by(sortBy).ascending()
	                : Sort.by(sortBy).descending();
	        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

	        User seller = authUtil.loggedInUser();

	        Page<Order> pageOrders = orderRepository.findAll(pageDetails);

	        List<Order> sellerOrders = pageOrders.getContent().stream()
	                .filter(order -> order.getOrderItems().stream()
	                        .anyMatch(orderItem -> {
	                            var product = orderItem.getProduct();
	                            if (product == null || product.getUser() == null) {
	                                return false;
	                            }
	                            return product.getUser().getUserId().equals(
	                                    seller.getUserId());
	                        }))
	                .toList();

	        List<OrderDTO> orderDTOs = sellerOrders.stream()
	                .map(order -> modelMapper.map(order, OrderDTO.class))
	                .toList();
	        OrderResponse orderResponse = new OrderResponse();
	        orderResponse.setContent(orderDTOs);
	        orderResponse.setPageNumber(pageOrders.getNumber());
	        orderResponse.setPageSize(pageOrders.getSize());
	        orderResponse.setTotalElements(pageOrders.getTotalElements());
	        orderResponse.setTotalPages(pageOrders.getTotalPages());
	        orderResponse.setLastPage(pageOrders.isLast());
	        return orderResponse;
	    }
}
