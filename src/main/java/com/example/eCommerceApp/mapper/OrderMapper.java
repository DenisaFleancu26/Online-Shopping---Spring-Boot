package com.example.eCommerceApp.mapper;

import com.example.eCommerceApp.dto.OrderDTO;
import com.example.eCommerceApp.dto.OrderItemDTO;
import com.example.eCommerceApp.model.Order;
import com.example.eCommerceApp.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "items")
    OrderDTO toDTO(Order order);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "items", source = "orderItems")
    Order toEntity(OrderDTO orderDTO);

    List<OrderDTO> toDTOs(List<Order> orders);
    List<Order> toEntities(List<OrderDTO> orderDTOS);


    @Mapping(target = "productId", source = "product.id")
    OrderItemDTO toDto(OrderItem orderItem);

    @Mapping(target = "product.id", source = "productId")
    OrderItem toEntity(OrderItemDTO orderItemDTO);

    List<OrderItemDTO> toOrderItemDTOs(List<OrderItem> orderItems);
    List<OrderItem> toOrderItemEntities(List<OrderItemDTO> orderItemDTOs);

}
