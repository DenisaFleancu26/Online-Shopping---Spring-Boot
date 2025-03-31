package com.example.eCommerceApp.service;

import com.example.eCommerceApp.dto.CartDTO;
import com.example.eCommerceApp.exception.InsufficientStockException;
import com.example.eCommerceApp.exception.ResourceNotFoundException;
import com.example.eCommerceApp.mapper.CartMapper;
import com.example.eCommerceApp.model.Cart;
import com.example.eCommerceApp.model.CartItem;
import com.example.eCommerceApp.model.Product;
import com.example.eCommerceApp.model.User;
import com.example.eCommerceApp.repository.CartRepository;
import com.example.eCommerceApp.repository.ProductRepository;
import com.example.eCommerceApp.repository.UserRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartDTO addToCart(Long userId, Long productId, Integer quantity){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(product.getQuantity() < quantity){
            throw new InsufficientStockException("Not enough available");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElse(new Cart(null, user, new ArrayList<>()));

        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if(existingCartItem.isPresent()){
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }else{
            CartItem cartItem = new CartItem(null, product, cart, quantity);
            cart.getItems().add(cartItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDTO(savedCart);
    }

    public CartDTO getCart(Long userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return cartMapper.toDTO(cart);
    }

    public void clearCart(Long userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        cart.getItems().clear();
        cartRepository.save(cart);
    }

}
