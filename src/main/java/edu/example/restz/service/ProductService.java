package edu.example.restz.service;

import edu.example.restz.dto.ProductDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.exception.ProductException;
import edu.example.restz.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    //상품 등록
    public ProductDTO register(ProductDTO productDTO){   //등록
        try {
            Product product = productDTO.toEntity();
            productRepository.save(product);
            return new ProductDTO(product);
        } catch(Exception e) {                  //상품 등록 시 예외가 발생한 경우
            log.error("--- " + e.getMessage()); //에러 로그로 발생 예외의 메시지를 기록하고
            throw ProductException.NOT_REGISTERED.get();  //예외 메시지를 Product NOT Registered로 지정하여 ProductTaskException 발생시키기
        }
    }

    //상품 조회
    //조회 결과가 없는 경우
    //예외 메시지를 Product NOT FOUND로 지정하여
    //ProductTaskException 발생시키기
}
