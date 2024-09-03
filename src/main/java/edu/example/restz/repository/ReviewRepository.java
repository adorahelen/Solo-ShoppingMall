package edu.example.restz.repository;

import edu.example.restz.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select  r from Review r join FETCH r.product where   r.rno = :rno")
    Optional<Review> getReviewProduct(@Param("rno") Long rno);
    // 두번 쿼리 하지 않도록,

    @Query("select  r from Review r " +
            "join FETCH r.product rp " +
            "join fetch rp.images where r.rno = :rno")
    Optional<Review> getWithProductImage(@Param("rno") Long rno);
    // 이미지랑 같이 가져오도록 두번 조인
}
