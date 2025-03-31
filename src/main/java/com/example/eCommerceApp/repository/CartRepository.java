package com.example.eCommerceApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.eCommerceApp.model.Cart;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);

}
