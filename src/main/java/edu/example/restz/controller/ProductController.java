package edu.example.restz.controller;

import edu.example.restz.dto.PageRequestDTO;
import edu.example.restz.dto.ProductDTO;
import edu.example.restz.dto.ProductListDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.exception.ProductException;
import edu.example.restz.exception.ProductTaskException;
import edu.example.restz.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Log4j2
public class ProductController {
    private  final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> register(@Validated @RequestBody ProductDTO productDTO,
                                               Principal principal) {
        log.info("--- register()");
        log.info("--- productDTO : " + productDTO);
        log.info("--- principal : " + principal);
        log.info("--- principal : " + principal.getName());

        if(productDTO.getImages() == null || productDTO.getImages().isEmpty()) {  //이미지가 없는 경우
            throw ProductException.NO_IMAGE.get();      // NO Product Image를 예외 메시지로 ProductTaskException 예외 발생 시키기
        }

        if(!principal.getName().equals( productDTO.getRegisterId()) ) {  //인증된 사용자와 productDTO의 등록자가 일치하지 않는 경우
            throw ProductException.REGISTER_ERR.get();   //NO Authenticated user를 예외 메시지로 ProductTaskException 예외 발생 시키기
        }

        return ResponseEntity.ok(productService.register(productDTO)); //상태 코드를 200 OK로 하여, 상품 등록 서비스가 반환하는 데이터를 뷰로 전달
    }

    @GetMapping("/{pno}")
    public ResponseEntity<ProductDTO> read(@PathVariable ("pno") Long pno) {
        log.info("--- read()");
        log.info("--- pno : " + pno);
        return ResponseEntity.ok(productService.read(pno));
    }

    @PutMapping("/{pno}")
    public ResponseEntity<ProductDTO> modify(@PathVariable("pno") Long pno, @RequestBody ProductDTO productDTO, Authentication authentication) {
        log.info("--- modify()");
        log.info("--- productDTO : " + productDTO);
        log.info("--- pno : " + pno);
        log.info("--- authentication : " + authentication);

        if(!pno.equals(productDTO.getPno())) {
            throw ProductException.NOT_FOUND.get();
        }
        if (productDTO.getImages() == null || productDTO.getImages().isEmpty()) {
            throw ProductException.NO_IMAGE.get();
        }
        if(!authentication.getName().equals(productDTO.getRegisterId())) {
            throw ProductException.REGISTER_ERR.get();
        }

        return ResponseEntity.ok(productService.update(productDTO));

    }
    @DeleteMapping("/{pno}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("pno") Long pno,
                                                      Authentication authentication) {
        log.info("--- delete()");
        log.info("--- pno : " + pno);
        log.info("--- authentication : " + authentication);


        String mid = productService.read(pno).getRegisterId();
        if (!authentication.getName().equals(mid)) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            authorities.stream().filter(authority -> authority.getAuthority().equals("ROLE_ADMIN"))
                    .findAny().orElseThrow(ProductException.REGISTER_ERR::get);
        }
        // 실제 삭제 작업 수행
        productService.remove(pno);  // delete() 메서드

        return ResponseEntity.ok(Map.of("Result", "Product deleted Successfully"));
    }

}