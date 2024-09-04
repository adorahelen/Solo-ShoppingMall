package edu.example.restz.controller;

import edu.example.restz.dto.PageRequestDTO;
import edu.example.restz.dto.ReviewDTO;
import edu.example.restz.dto.ReviewPageRequestDTO;
import edu.example.restz.entity.Review;
import edu.example.restz.exception.ReviewException;
import edu.example.restz.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

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
    } // 토큰 발급자, 작성한 리뷰어 아이디, 수정하고자 할 리뷰 번호 등 전부 매칭이 되어 있어야 수정이 가능하다.
    // 아직 매니 투 원, 추후 매니 투 매니


    @DeleteMapping("/{rno}")
    public ResponseEntity<Map<String, String>> remove(@PathVariable Long rno, Authentication authentication) {
        log.info("Remove review by rno {}", rno);
        String nowUsername = authentication.getName();
        if(! nowUsername.equals(reviewService.read(rno).getReviewer()) ) {
            throw ReviewException.NOT_MATCHED.get();
        }
        reviewService.remove(rno);
        return ResponseEntity.ok().body(Map.of("message", "review removed"));
    }

    //목록
    //기본요청 - 1 페이지, 리뷰 5개
    //페이지 번호를 음수로 지정 : 1 이상이어야 합니다
    //사이즈를 5 미만으로 지정 : 5 이상이어야 합니다    //
    @GetMapping("/{pno}/list")
    public ResponseEntity<Page<ReviewDTO>> getList(
            @PathVariable("pno") Long pno,
            @Validated ReviewPageRequestDTO pageRequestDTO){
        log.info("getList() ----- " + pageRequestDTO);         //로그로 출력
        pageRequestDTO.setPno(pno);
        return ResponseEntity.ok(reviewService.getList(pageRequestDTO));
    }

}
