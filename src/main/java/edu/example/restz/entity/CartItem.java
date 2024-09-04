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
    // CartItem 엔티티는 Cart와 Product 사이의 중간 엔티티로,
    // Cart와 Product 간의 다대다 관계를 각각의 1대다 및 다대1 관계로 분리하여 처리합니다.
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
