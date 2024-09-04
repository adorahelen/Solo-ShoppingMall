package edu.example.restz.controller;

import edu.example.restz.dto.AddCartItemDTO;
import edu.example.restz.dto.CartItemDTO;
import edu.example.restz.dto.ModifyDeleteCartItemDTO;
import edu.example.restz.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;

// 문제가 될 수 있는 부분은 #AddCartItemDTO.customer에서 AddCartItemDTO의 첫 글자가 대문자라는 점입니다.
// 이 부분은 Spring Expression Language (SpEL)에서 addCartItemDTO로 사용되어야 합니다

    @PreAuthorize("authentication.name == #addCartItemDTO.customer")
    @PostMapping("/addItem")
    public ResponseEntity<List<CartItemDTO>> addCartItem(
            @RequestBody AddCartItemDTO addCartItemDTO) {
        log.info("###### Adding cart item: {}", addCartItemDTO);
        cartService.RegisterItem(addCartItemDTO);

        List<CartItemDTO> cartItemDTOList = cartService.getAllItems(addCartItemDTO.getCustomer());
        return ResponseEntity.ok(cartItemDTOList);
        // ://localhost:8080/api/v1/carts/addItem
    }

    @GetMapping("/{cno}")
    public ResponseEntity<List<CartItemDTO>> getCartItemList(
            @PathVariable("cno") Long cno, Principal principal) {
        log.info("###### Getting cart item list: {}", cno);
        String mid = principal.getName();
        cartService.checkCartCustomer(mid, cno);

        List<CartItemDTO> cartItemDTOList = cartService.getAllItems(mid);
        return ResponseEntity.ok(cartItemDTOList);
        // ://localhost:8080/api/v1/carts/2
        // 해당 장바구니가, 자신의 장바구니인지 확인하고 보여준다
        // if 다른 소유주의 장바구니 조회시 없다고 에러
    }

    @PutMapping("/modifyItem/{itemNo}")
    public ResponseEntity<List<CartItemDTO>> modifyCartItem(
            @PathVariable("itemNo") Long itemNo,
            @RequestBody ModifyDeleteCartItemDTO modideleteCartItemDTO,
            Principal principal) {

        log.info("###### Modifying cart item: {}", modideleteCartItemDTO);
        String mid = principal.getName();
        cartService.checkItemCustomer(mid, itemNo);

        modideleteCartItemDTO.setItemNo(itemNo);
        cartService.modifyDeleteItem(modideleteCartItemDTO);

        List<CartItemDTO> cartItemDTOList = cartService.getAllItems(mid);
        return ResponseEntity.ok(cartItemDTOList);
    } // ://localhost:8080/api/v1/carts/modifyItem/2

    @DeleteMapping("/removeItem/{itemNo}")
    public ResponseEntity<List<CartItemDTO>> removeCartItem(
            @PathVariable("itemNo") Long itemNo, Principal principal)
    {
        log.info("###### Removing cart item: {}", itemNo);
        String mid = principal.getName();
        cartService.checkItemCustomer(mid, itemNo);

        cartService.modifyDeleteItem(
                ModifyDeleteCartItemDTO.builder().itemNo(itemNo).quantity(0).build()
        );

        List<CartItemDTO> cartItemDTOList = cartService.getAllItems(mid);
        return ResponseEntity.ok(cartItemDTOList);
    } // ://localhost:8080/api/v1/carts/removeItem/7
}
