package com.example.eCommerceApp.service;

import com.example.eCommerceApp.dto.CartDTO;
import com.example.eCommerceApp.dto.OrderDTO;
import com.example.eCommerceApp.exception.InsufficientStockException;
import com.example.eCommerceApp.exception.ResourceNotFoundException;
import com.example.eCommerceApp.mapper.CartMapper;
import com.example.eCommerceApp.mapper.OrderMapper;
import com.example.eCommerceApp.model.*;
import com.example.eCommerceApp.repository.OrderRepository;
import com.example.eCommerceApp.repository.ProductRepository;
import com.example.eCommerceApp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final UserRepository userRepository;
    private final CartService cartService;
    private final CartMapper cartMapper;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final EmailService emailService;
    private final ProductRepository productRepository;

    @Transactional
    public OrderDTO createOrder(Long userId, String address, String phoneNumber){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(!user.isEmailConfirmation()){
            throw new IllegalStateException("Email not confirmed. Please confirm email before placing order");
        }
        CartDTO cartDTO = cartService.getCart(userId);
        Cart cart = cartMapper.toEntity(cartDTO);

        if(cart.getItems().isEmpty()){
            throw new IllegalStateException("Cannot create an order with an empty cart");
        }

        Order order = new Order();
        order.setAddress(address);
        order.setUser(user);
        order.setPhoneNumber(phoneNumber);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PREPARING);

        List<OrderItem> orderItemList = createOrderItems(cart, order);
        order.setItems(orderItemList);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);

        try{
            emailService.sendOrderConfirmation(savedOrder);
        }catch (MailException e){
            logger.error("Failed to send order confirmation email for order ID {}", savedOrder.getId(), e);
        }
        return orderMapper.toDTO(savedOrder);
    }

    private List<OrderItem> createOrderItems(Cart cart, Order order) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + cartItem.getProduct().getId()));
            if(product.getQuantity() < cartItem.getQuantity()){
                throw new InsufficientStockException("Not enough stock of product " + product.getName());
            }
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            return new OrderItem(null, cartItem.getQuantity(), product.getPrice(), product, order);
        }).collect(Collectors.toList());
    }

    public List<OrderDTO> getAllOrders(){
        return orderMapper.toDTOs(orderRepository.findAll());
    }

    public List<OrderDTO> getUserOrders(Long userId){
        return orderMapper.toDTOs(orderRepository.findByUserId(userId));
    }

    public OrderDTO updateOrderStatus(Long orderId, Order.OrderStatus status){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDTO(updatedOrder);
    }


}
