package edu.example.restz.repository;

import edu.example.restz.entity.Cart;
import edu.example.restz.entity.CartItem;
import edu.example.restz.entity.Product;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Log4j2
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class CartRepositoryTest {
    // 쿼리문 생략 , 테스트 생략
    // entity -> repository -> DTO -> service -> controller

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Commit
    @Test
    public void testInsert(){
        String mid = "user9";
        Long pno = 46L;
        int qty = 5;

        // 장바구니 조회 또는 생성
        Optional<Cart> cartResult = cartRepository.findByCustomer(mid);

        Cart cart = cartResult.orElseGet(()->{ // 없다면, 아래와 같이 '카트' 하나 생성
            Cart cart1 = Cart.builder().customer(mid).build();
            return cartRepository.save(cart1);
        });

        // CartItem 생성 및 저장
        Product product = Product.builder().pno(pno).build(); // 참조용
        CartItem cartItem = CartItem.builder()  // 참조를 바탕으로 카트에 담아 아이템 저장
                .cart(cart)
                .product(product)
                .quantity(qty)
                .build();

        cartItemRepository.save(cartItem);
    }

    @Test
    public void testRead(){ // test read 2 는, 이미지 전부가 아니라 상품 이미지 하나만 출력하도록 함
        String mid = "user9";
        Optional<List<CartItem>> result = cartItemRepository.getCartItemsOfCustomer(mid);

        List<CartItem> cartItemList = result.orElse(null);

        cartItemList.forEach(cartItem -> {
            System.out.println(cartItem);
            System.out.println(cartItem.getProduct());
            System.out.println(cartItem.getProduct().getImages());
            System.out.println("=================================");
        }); // 추가적인 쿼리 없이 조인을 통해 상품의 이미지까지 가져온다.
    }

    @Test
    @Transactional
    @Commit
    public void testModifyAndDelete(){
        Long itemNo = 3L;
        int qty = 0;

        Optional<CartItem> result = cartItemRepository.findById(itemNo);
        log.info("###############");
        log.info(result);

        CartItem cartItem = result.get();
        cartItem.changeQuantity(qty);

        if (cartItem.getQuantity() <= 0){
            cartItemRepository.delete(cartItem);
        } // 수정과 삭제는 동시에 :
        // ? : 조정 가능한 속성값은 수량 밖에 없으며, 수량이 0보다 작거나 같으면 해당 장바구니 아이템은 삭제이다.
    }
}
