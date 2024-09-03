package edu.example.restz.controller;

import edu.example.restz.dto.ReviewDTO;
import edu.example.restz.entity.Review;
import edu.example.restz.exception.ReviewException;
import edu.example.restz.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(@Validated @RequestBody ReviewDTO reviewDTO,
                                               Principal principal) {
      if(!principal.getName().equals(reviewDTO.getReviewer()) ) {
          throw ReviewException.NOT_MATCHED_REVIEWER.get();
      }
      return ResponseEntity.ok(reviewService.register(reviewDTO));
    }

    @GetMapping("/{rno}")
    public ResponseEntity<ReviewDTO> read(@PathVariable("rno") Long rno) {
        log.info("Read review by rno {}", rno);
        return ResponseEntity.ok(reviewService.read(rno));
    }

    @PutMapping("/{rno}")
    public ResponseEntity<ReviewDTO> modify(@PathVariable Long rno,
                                            @Validated @RequestBody ReviewDTO reviewDTO
    , Authentication authentication) {
        log.info("Modify review by rno {}", rno);

        if(! rno.equals(reviewDTO.getRno()) ) { // rno 가 일치하는지 체크한다
            throw ReviewException.NOT_MATCHED.get();
        }

        String reviewer = reviewService.read(rno).getReviewer();
        if(! authentication.getName().equals(reviewDTO.getReviewer())
        || !reviewer.equals(reviewDTO.getReviewer()) ){
            throw ReviewException.NOT_MATCHED_REVIEWER.get();
        }

        return ResponseEntity.ok(reviewService.update(reviewDTO));
    }

}
