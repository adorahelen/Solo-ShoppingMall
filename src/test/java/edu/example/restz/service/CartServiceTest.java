package edu.example.restz.service;

import edu.example.restz.dto.AddCartItemDTO;
import edu.example.restz.dto.CartItemDTO;
import edu.example.restz.dto.ModifyDeleteCartItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CartServiceTest {
    @Autowired
    private CartService cartService;

    @Test
    public void testGetCartList() {
        String mid = "user9";
        List<CartItemDTO> cartItemDTOList = cartService.getAllItems(mid);
        cartItemDTOList.forEach(cartItemDTO -> {
            System.out.println(cartItemDTO);
        });
    }

    @Test
    public void testRegisterItem() {
        String mid = "user99";
        Long pno = 40L;
        int qty = 2;

        AddCartItemDTO addCartItemDTO = AddCartItemDTO.builder()
                .customer(mid)
                .pno(pno)
                .quantity(qty)
                .build();

        cartService.RegisterItem(addCartItemDTO);
    }

    @Test
    public void testModifyRemoveItem() {
        Long itemNo = 2L;
        int qty = 11;

        ModifyDeleteCartItemDTO modifyDeleteCartItemDTO = ModifyDeleteCartItemDTO.builder()
                .itemNo(itemNo)
                .quantity(qty)
                .build();

        cartService.modifyDeleteItem(modifyDeleteCartItemDTO);
    }
}
