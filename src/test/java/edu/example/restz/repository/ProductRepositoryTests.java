package edu.example.restz.repository;

import edu.example.restz.dto.ProductDTO;
import edu.example.restz.entity.Product;
import edu.example.restz.entity.ProductImage;
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
                            .registerId("user" + i)
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

    @Test
    @Transactional
    @Commit
    public void testUpdate() {
        Long pno = 1L;
        String pname = "변경 상품";
        int price = 1000;
        String img1 = "new1.jpg";
        String img2 = "new2.jpg";

        Optional<Product> foundproduct = productRepository.getProduct(pno);
        assertTrue(foundproduct.isPresent(), "상품이 존재하지 않습니다 ");

        Product product = foundproduct.get(); // 찾은 번호에 맞는, 해당 객체 가져오기
        product.changePname(pname); // 변경 시작
        product.changePrice(price);
        product.addImage(img1);
        product.addImage(img2);

        foundproduct = productRepository.getProduct(pno); // 해당하는 데이터 다시 가져오기
        assertEquals(pname, foundproduct.get().getPname());
        assertEquals(price, foundproduct.get().getPrice());

        SortedSet<ProductImage> productImages = product.getImages(); // 데이터에서 프로덕트 이미지를
        assertEquals(3, productImages.last().getIno());
    }

    @Test
    @Transactional
    @Commit
    public void testDelete() {
        Long pno = 5L;

        assertTrue(productRepository.getProduct(pno).isPresent(), " 없습니다 . ");

        productRepository.deleteById(pno);

        assertFalse(productRepository.getProduct(pno).isPresent(), "지웠는데 왜 있지?");
    }

    @Test
    public void testReadDTO() {

        // 반드시 DB에 있는 번호로
        Long pno = 1L;

        Optional<ProductDTO> result = productRepository.getProductDTO(pno);
        assertTrue(result.isPresent(), " 문제가 발생 1 ");

        List<String> images = result.get().getImages();
        assertNotNull(images);
        assertEquals("new2.jpg", images.get(3));
        log.info(result);
// 2024-08-30T16:36:02.680+09:00  INFO 2160 --- [restz] [    Test worker]
// e.e.r.repository.ProductRepositoryTests  :
// Optional[ProductDTO(pno=1, pname=변경 상품, price=1000,
// description=null, registerId=null, images=[1_image1.jpg, 1_image2.jpg, new1.jpg, new2.jpg])]


        // 2024-08-30T16:40:37.898+09:00  INFO 2199 --- [restz] [    Test worker] e.e.r.repository.
        // ProductRepositoryTests  : Optional[ProductDTO(pno=1, pname=변경 상품, price=1000,
        // description=상품 설명, registerId=user1, images=[1_image1.jpg, 1_image2.jpg, new1.jpg, new2.jpg])]


//        ProductDTO productDTO = result.get();
//        System.out.println(productDTO);
    }
}
