package edu.example.restz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;

@Entity // 1. 엔티티 클래스로 만들기
@Table(name = "tbl_product") // 2. 테이블 이름 지정
@Getter
@ToString(exclude = "images")
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

// =======================================================================================
@ElementCollection(fetch = FetchType.LAZY) // 필요할 때 까지 로딩하지 않도록
// 	@ElementCollection(fetch = FetchType.LAZY): images 필드는 ProductImage 객체의 컬렉션을 나타내며,
// 	데이터베이스에 별도의 테이블(tbl_product_image)로 매핑됩니다.
//  ProductImage 컬렉션을 매핑하며, 지연 로딩(Lazy Loading)을 사용합니다.

@CollectionTable(name = "tbl_product_image", joinColumns = @JoinColumn(name = "pno"))
// 	•	@CollectionTable: tbl_product_image 테이블이 Product 엔티티와 관계를 가짐을 지정하며, 이때 pno가 외래 키로 사용됩니다.
// 22. 빌더 패턴을 사용할 때, images 필드를 기본적으로 빈 TreeSet으로 초기화합니다.

@Builder.Default
private SortedSet<ProductImage> images = new TreeSet<>();
// 	•	TreeSet: images 필드는 SortedSet 타입으로 정의되어 있으며, TreeSet을 사용해 정렬된 상태로 관리됩니다.
// set 집합을 사용한 이유는. 추후 중복이 되지 않게 하기 위해서

// Product image add
    public void addImage(String filename) {
        ProductImage productImage = ProductImage.builder()
                .filename(filename)
                .ino(images.size())
                .build();

        images.add(productImage);
    }
// Product image Delete
public void clearImages(){
    images.clear();
}


/*	•	데이터 구조 설정: 이 코드는 Product 엔티티에 여러 개의 이미지를 저장할 수 있도록 하는 복합적인 JPA 설정입니다.
	* ProductImage는 독립적인 엔티티가 아니므로, Product 엔티티의 일부로서 관리됩니다.
	•	지연 로딩: images 컬렉션은 필요할 때만 로드되므로 초기 데이터베이스 쿼리의 성능에 긍정적인 영향을 미칩니다.
	•	정렬된 컬렉션: SortedSet과 TreeSet을 사용해 이미지들이 자동으로 정렬된 상태로 관리됩니다.
*/

// =======================================================================================

// 상품 이름 , 가격 , 설명 변경하는 메서드 -> 세터로 만들고 수정함 (메소드명만)
    public void changePname(String pname) {this.pname = pname;}

    public void changePrice(int price) {this.price = price;}

    public void changeDescription(String description) {this.description = description;}
}
