package edu.example.restz.repository;

import edu.example.restz.entity.Product;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;

    @Test // 단순 삽입
    public void testInsert() {
        // Given - Product entity 객체 엔티티 생성
        IntStream.rangeClosed(1, 50).forEach(i -> {
                    Product product = Product.builder()
                            .pname("신규상품 " + i)
                            .price(5000)
                            .description("상품 설명")
                            .registerId("user0")
                            .build();

                    product.addImage(i + "_image1.jpg");
                    product.addImage(i + "_image2.jpg");

                    // When - 엔티티 저장
                    Product saveProduct = productRepository.save(product);

                    // then - 기댓값, : saveTodo 는 NULL 이 아니며, 아이디는 1이다.
                    assertNotNull(saveProduct);
                    assertEquals(i, saveProduct.getPno());
                    assertEquals(0, saveProduct.getImages().first().getIno());
        });
    }
}
