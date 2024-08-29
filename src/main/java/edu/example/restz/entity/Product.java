package edu.example.restz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity // 1. 엔티티 클래스로 만들기
@Table(name = "tbl_product") // 2. 테이블 이름 지정
@Getter
@ToString
@NoArgsConstructor // 5. 기본 생성자
@AllArgsConstructor // 6. 모든 필드를 매개 변수로 받는 생성자
@Builder
// 8. 등록일자가 자동으로 추가되도록 처리하기
@EntityListeners(value = { AuditingEntityListener.class })

public class Product {
    @Id // 9. pno 기본키 지정 후 , 1씩 자동 증가되도록 처리
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long pno; // 상품 번호

    private String pname; // 상품 이름
    private int price; // 상품 가격
    private String description; // 상품 설명
    private String registerId; // 상품 등록자

    @CreatedDate // 생성 자동 저장
    private LocalDateTime regDate; // 상품 등록 일시
    @LastModifiedDate // 수정 자동 저장
    private LocalDateTime modDate; // 상품 수정 일시

//    public Product(String pname, int price, String description) {
//        this.pname = pname;
//        this.price = price;
//        this.description = description;
//    }

// 상품 이름 , 가격 , 설명 변경하는 메서드 -> 세터로 만들고 수정함 (메소드명만)
    public void changePname(String pname) {this.pname = pname;}

    public void changePrice(int price) {this.price = price;}

    public void changeDescription(String description) {this.description = description;}
}
