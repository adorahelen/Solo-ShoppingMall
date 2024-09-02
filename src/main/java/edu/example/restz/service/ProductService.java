package edu.example.restz.service;

import edu.example.restz.dto.PageRequestDTO;
import edu.example.restz.dto.ProductDTO;
import edu.example.restz.dto.ProductListDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.ProductImage;
import edu.example.restz.exception.ProductException;
import edu.example.restz.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    public ProductDTO read(Long pno) {
        log.info(" === pno Search ===");
        log.info("--- " + pno);

        Optional<ProductDTO> productDTO = productRepository.getProductDTO(pno);
        return productDTO.orElseThrow(ProductException.NOT_REGISTERED::get);

    }


    public ProductDTO update(ProductDTO productDTO) {
        log.info(" === pno Modify ===");
        log.info("--- " + productDTO);
        Optional<Product> result = productRepository.findById(productDTO.getPno());
        Product product = result.orElseThrow(ProductException.NOT_REGISTERED::get);

        try {
            product.changePname(productDTO.getPname());
            product.changePrice(productDTO.getPrice());
            product.changeDescription(productDTO.getDescription());

            product.clearImages();

            // new images add
            List<String> images = productDTO.getImages();

            if(images != null && !images.isEmpty()) {
                images.forEach(product::addImage);
            }
            productRepository.save(product);
            return new ProductDTO(product);

        }catch(Exception e) {
            log.error("--- " + e.getMessage());
            throw ProductException.NOT_MODIFIED.get();
        } // end catch

    }

    public void remove(Long pno) {
        log.info(" === pno Delete ===");
        log.info("--- " + pno);
        Optional<Product> result = productRepository.findById(pno);
        Product product = result.orElseThrow(ProductException.NOT_FOUND::get);

        try {
            productRepository.delete(product);
        }catch(Exception e) {
            log.error("--- " + e.getMessage());
            throw ProductException.NOT_REMOVED.get();
        }

    }

    public Page<ProductListDTO> getList(PageRequestDTO pageRequestDTO) {
        log.info(" === pno Page List Search ===");
        log.info("--- " + pageRequestDTO);

        try {
            Pageable pageable =
                    pageRequestDTO.getPageable(Sort.by("pno").descending());

            return  productRepository.list(pageable);
        }catch (Exception e) {
            log.error("--- " + e.getMessage());
            throw ProductException.NOT_FETCHED.get();

        } // end
    }// 상품의 목록은 나중에 검색 조건을 추가하는 부분을 고려해야 한다.


}
