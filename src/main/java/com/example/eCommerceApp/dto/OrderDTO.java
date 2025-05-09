package com.example.eCommerceApp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import com.example.eCommerceApp.model.Order;

@Data
public class OrderDTO {

    private Long id;
    private Long userId;
    @NotBlank(message = "Address is required!")
    private String address;
    @NotBlank(message = "Phone is required!")
    private String phoneNumber;
    private Order.OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> orderItems;
}

