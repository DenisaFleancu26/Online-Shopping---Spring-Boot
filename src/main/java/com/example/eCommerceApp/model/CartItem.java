package com.example.eCommerceApp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    @ManyToOne()
    @JoinColumn(name="cart_id", nullable = false)
    private Cart cart;

    private Integer quantity;
}
