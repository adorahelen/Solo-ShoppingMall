package edu.example.restz.service;

import edu.example.restz.dto.ReviewDTO;
import edu.example.restz.entity.Review;
import edu.example.restz.exception.ReviewException;
import edu.example.restz.exception.ReviewTaskException;
import edu.example.restz.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewDTO register(ReviewDTO reviewDTO) {
// 리턴 값은 디티오,
        // 리포지토리를 통해 디비에 저장할때는 엔티티러 변환해서 저장
        try {
            Review review = reviewDTO.toEntity();
            reviewRepository.save(review);
            return new ReviewDTO(review);
        }catch (DataIntegrityViolationException e){
            throw ReviewException.PRODUCT_NOT_FOUND.get();
        }catch ( Exception e ) {
            log.error(e);
            log.error(e.getMessage());
            throw ReviewException.NOT_REGISTERED.get();
        }
    }

    public ReviewDTO read(Long rno) {
        Review review = reviewRepository.findById(rno).orElseThrow(ReviewException.NOT_FOUND::get);
        return  new ReviewDTO(review);
        // 반환을 해서 모델로(뷰) 보내기 위해 다시 디티오에 담아서 반환
        // 값을 엔티티에서 가져올때는 엔티티로 받는다.
        // 따라서 중간에 엔티티가 사용되었지만, public ReviewDTO 라고 선언한 것이다.
    }

    public ReviewDTO update(ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(reviewDTO.getRno())
                .orElseThrow(ReviewException.NOT_FOUND::get);

        try {
            review.ChangeContent(reviewDTO.getContent());
            review.ChangeStar(reviewDTO.getStar());
            reviewRepository.save(review);
            return new ReviewDTO(review);
        }catch (Exception e) {

            log.error(e);
            log.error(e.getMessage());
            throw ReviewException.NOT_MODIFIED.get();

        }
    }



}
