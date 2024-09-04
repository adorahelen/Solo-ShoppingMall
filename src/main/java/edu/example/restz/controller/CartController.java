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

    @PreAuthorize("authentication.name == #AddCartItemDTO.customer")
    @PostMapping("/addItem")
    public ResponseEntity<List<CartItemDTO>> addCartItem(
            @RequestBody AddCartItemDTO addCartItemDTO) {
        log.info("###### Adding cart item: {}", addCartItemDTO);
        cartService.RegisterItem(addCartItemDTO);

        List<CartItemDTO> cartItemDTOList = cartService.getAllItems(addCartItemDTO.getCustomer());

        return ResponseEntity.ok(cartItemDTOList);
    }

    @GetMapping("/{cno}")
    public ResponseEntity<List<CartItemDTO>> getCartItemList(
            @PathVariable("cno") Long cno, Principal principal) {
        log.info("###### Getting cart item list: {}", cno);
        String mid = principal.getName();
        cartService.checkCartCustomer(mid, cno);

        List<CartItemDTO> cartItemDTOList = cartService.getAllItems(mid);
        return ResponseEntity.ok(cartItemDTOList);
    }

    @PutMapping("/modifyItem/{itemNo}")
    public ResponseEntity<List<CartItemDTO>> modifyCartItem(
            @PathVariable("itemNo") Long itemNo,
            @RequestBody ModifyDeleteCartItemDTO modideleteCartItemDTO,
            Principal principal) {
        log.info("###### Modifying cart item: {}", modideleteCartItemDTO);
        String mid = principal.getName();
        cartService.checkCartCustomer(mid, itemNo);
        modideleteCartItemDTO.setItemNo(itemNo);
        cartService.modifyDeleteItem(modideleteCartItemDTO);
        List<CartItemDTO> cartItemDTOList = cartService.getAllItems(mid);
        return ResponseEntity.ok(cartItemDTOList);
    }

    @DeleteMapping("/removeItem/{itemNo}")
    public ResponseEntity<List<CartItemDTO>> removeCartItem(
            @PathVariable("itemNo") Long itemNo, Principal principal)
    {
        log.info("###### Removing cart item: {}", itemNo);
        String mid = principal.getName();
        cartService.checkCartCustomer(mid, itemNo);
        cartService.modifyDeleteItem(
                ModifyDeleteCartItemDTO.builder().itemNo(itemNo).quantity(0).build()
        );
        List<CartItemDTO> cartItemDTOList = cartService.getAllItems(mid);
        return ResponseEntity.ok(cartItemDTOList);
    }
}
