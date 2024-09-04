package edu.example.restz.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_cart_items", indexes = @Index(columnList = "cart_cno"))
@Getter
@ToString(exclude = {"product", "cart"})
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemNo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }
}
