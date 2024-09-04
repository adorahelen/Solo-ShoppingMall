package edu.example.restz.repository;

import edu.example.restz.entity.Cart;
import edu.example.restz.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


}
