package edu.example.restz.service;

import edu.example.restz.dto.AddCartItemDTO;
import edu.example.restz.dto.CartItemDTO;
import edu.example.restz.dto.ModifyDeleteCartItemDTO;
import edu.example.restz.entity.Cart;
import edu.example.restz.entity.CartItem;
import edu.example.restz.entity.Product;
import edu.example.restz.exception.CartTaskException;
import edu.example.restz.repository.CartItemRepository;
import edu.example.restz.repository.CartRepository;
import edu.example.restz.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    // 모든 리포지토리 추가

    @Transactional(readOnly = true)
    public List<CartItemDTO> getAllItems(String mid){

        List<CartItemDTO> itemDTOList = new ArrayList<>();

        Optional<List<CartItem>> result = cartItemRepository.getCartItemsOfCustomer(mid);

        if(result.isEmpty()){
            log.info("No cart items found for customer " + mid);
            return itemDTOList;
        }
        List<CartItem> cartItems = result.get();

        cartItems.forEach(cartItem -> {
            itemDTOList.add(entityToDTO(cartItem));
        });
        return itemDTOList;
    }

    private CartItemDTO entityToDTO(CartItem cartItem) {
        return CartItemDTO.builder()
                .itemNo(cartItem.getItemNo())
                .pname(cartItem.getProduct().getPname())
                .pno(cartItem.getProduct().getPno())
                .price(cartItem.getProduct().getPrice())
                .image(cartItem.getProduct().getImages().first().getFilename())
                .quantity(cartItem.getQuantity())
                .build();
    }


    public void RegisterItem(AddCartItemDTO addCartItemDTO) {

        String mid = addCartItemDTO.getCustomer();
        Long pno = addCartItemDTO.getPno();
        int quantity = addCartItemDTO.getQuantity();

        Optional<Cart> CartResult = cartRepository.findByCustomer(mid);

        // 카트
        Cart cart = CartResult.orElseGet(() -> {
            Cart cart1 = Cart.builder().customer(mid).build();
            return cartRepository.save(cart1);
        });

        // 프로덕트
        Product product = productRepository.findById(pno)
                .orElseThrow(CartTaskException.Items.NOT_FOUND_PRODUCT::value);

        // 카트아이템
        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .build();
        try {
            cartItemRepository.save(cartItem);
        }catch (Exception e){
            log.error(e.getMessage());
            throw CartTaskException.Items.CART_ITEM_REGISTER_FAIL.value();
        }
    }

    public void modifyDeleteItem(ModifyDeleteCartItemDTO modifyDeleteCartItemDTO) {
        Long itemNo = modifyDeleteCartItemDTO.getItemNo();
        int quantity = modifyDeleteCartItemDTO.getQuantity();

        Optional<CartItem> result = cartItemRepository.findById(itemNo);
        if(result.isEmpty()){
            throw CartTaskException.Items.NOT_FOUND_CARTITEM.value();
        }
        CartItem cartItem = result.get();

        if (quantity <= 0){
            cartItemRepository.delete(cartItem);
            return;
        }
        cartItem.changeQuantity(quantity);
    }

    // 장바구니 아이템 소유주 확인 기능
    public void checkItemCustomer(String customer, Long itemNo){
        Optional<String> result = cartItemRepository.getCustomerOfItem(itemNo);
        if(result.isEmpty()){
          throw CartTaskException.Items.NOT_FOUND_CARTITEM.value();
        }
        if(!result.get().equals(customer)){
            throw CartTaskException.Items.NOT_CARTITEM_OWNER.value();
        }
    }

    // 장바구니 자체, 소유주 확인 기능
    public void checkCartCustomer(String customer, Long cno){
        Cart cart = cartRepository.findByCustomer(customer)
                .orElseThrow(CartTaskException.Items.NOT_FOUND_CART::value);

        if(! cart.getCno().equals(cno)){
            throw CartTaskException.Items.NOT_FOUND_CART.value();
        }
    }

}
