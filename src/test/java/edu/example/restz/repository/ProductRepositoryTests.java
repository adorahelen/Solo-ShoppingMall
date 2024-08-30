package edu.example.restz.repository;

import edu.example.restz.entity.Product;
import edu.example.restz.entity.ProductImage;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.SortedSet;
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


    // ====== 디폴트값이 지연로딩으로 되어있다. (즉시로딩은 계속 전부 읽어 오기에, @ElementCollection(fetch = FetchType.LAZY))
    // 따라서 아래와 같이 트랜잭션 리드 온리를 통해 2번 읽어오게 할 수 있고
    // 아래 아래와 같이 레포인터에 메소드를 하나 정의해서 필요한 것만 즉시 로딩을 통해 조인을 통해 가져오게 할 수 있다.
    @Test
    @Transactional(readOnly = true)
    public void testRead() {
        Long pno = 1L;

        Optional<Product> foundproduct = productRepository.findById(pno);
        assertTrue(foundproduct.isPresent(), "Product not found, 상품이 있어야 됩니다.");

        System.out.println("===============================");
        Product product = foundproduct.get(); // 전부
        SortedSet<ProductImage> productImages = product.getImages(); // 1. 객체에서 필요한 부분만
        assertNotNull(productImages); // 2. 널이 아닌지 검증 -> pass
        assertEquals(0, productImages.first().getIno()); // 3. 첫번째 first
        System.out.println(productImages);

    }

    // @EntityGraph 쓰는 방법이 있고, 일반 쿼리문으로 Join 연산을 수행도 가능
    @Test
    public void testRead2(){
        Long pno = 1L;
        Optional<Product> foundproduct = productRepository.getProduct(pno);
        assertTrue(foundproduct.isPresent());

        Product product = foundproduct.get();
        SortedSet<ProductImage> productImages = product.getImages();
        assertNotNull(productImages);
        assertEquals(0, productImages.first().getIno());
        System.out.println(productImages);


    }
}
