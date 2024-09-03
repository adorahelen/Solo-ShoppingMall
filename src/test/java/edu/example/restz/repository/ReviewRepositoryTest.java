package edu.example.restz.repository;

import edu.example.restz.entity.Product;
import edu.example.restz.entity.Review;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;


// @DataJpaTest 시큐리티 때문에 X
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
public class ReviewRepositoryTest {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void testInsert(){
        // tbl_review 테이블에 상품번호 1번의 테스트 데이터 10개를 추가한다.
        Long pno = 1L;
        Product product = Product.builder().pno(pno).build();

        IntStream.rangeClosed(1, 10).forEach(i -> {
            Review review = Review.builder()
                    .content("리뷰 테스트" + i)
                    .reviewer("user" + i)
                    .star(5)
                    .product(product)
                    .build();

            // 저장된 리뷰 객체를 로깅하거나 검증할 필요가 있다면 이곳에서 처리
            Review savedReview = reviewRepository.save(review);
            log.info("Saved Review: " + savedReview);
            assertNotNull(savedReview);
            assertEquals(i, savedReview.getRno());
        });
    }
}

