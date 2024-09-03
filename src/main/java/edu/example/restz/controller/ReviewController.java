package edu.example.restz.controller;

import edu.example.restz.dto.ReviewDTO;
import edu.example.restz.entity.Review;
import edu.example.restz.exception.ReviewException;
import edu.example.restz.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
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


}
