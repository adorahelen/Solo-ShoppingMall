package edu.example.restz.repository;

import edu.example.restz.entity.Cart;
import edu.example.restz.entity.CartItem;
import edu.example.restz.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("select  c from CartItem c " +
            " join FETCH c.product  " +
            " join fetch c.product.images " +
            " where c.cart.customer = :customer " +
            " order by c.itemNo desc ")
    Optional<List<CartItem>> getCartItemsOfCustomer(@Param("customer") String customer);
// 중복 셀렉트 하지 않고, 한꺼번에 조회하기 위한 쿼리문
}
