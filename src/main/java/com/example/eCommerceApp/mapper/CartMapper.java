package com.example.eCommerceApp.mapper;

import com.example.eCommerceApp.dto.CartDTO;
import com.example.eCommerceApp.dto.CartItemDTO;
import com.example.eCommerceApp.dto.CommentDTO;
import com.example.eCommerceApp.dto.ProductDTO;
import com.example.eCommerceApp.model.Cart;
import com.example.eCommerceApp.model.CartItem;
import com.example.eCommerceApp.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
    CartDTO toDTO(Cart cart);

    @Mapping(target = "user.id", source = "userId")
    Cart toEntity(CartDTO cartDTO);


    @Mapping(target = "productId", source = "product.id")
    CartItemDTO toDTO(CartItem cartItem);

    @Mapping(target = "product.id", source = "productId")
    CartItem toEntity(CartItemDTO cartItemDTO);

    List<CartItemDTO> toCartItemDTOs(List<CartItem> cartItems);
    List<CartItem> toCartItemEntities(List<CartItemDTO> cartItemDTOs);
}
