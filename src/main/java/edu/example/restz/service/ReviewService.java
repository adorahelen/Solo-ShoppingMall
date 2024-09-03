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

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }
    public Review findById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }
    public void deleteById(Long id) {
        reviewRepository.deleteById(id);
    }
    public Review update(Review review) {
        return reviewRepository.save(review);
    }


}
