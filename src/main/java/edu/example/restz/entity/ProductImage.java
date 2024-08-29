package edu.example.restz.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
// 	•	의미: @Embeddable은 JPA에서 해당 클래스를 다른 엔티티 클래스 내에 포함시킬 수 있는 “내장형 클래스”로 만들겠다는 것을 나타냅니다.
// +	내장형 클래스는 독립적으로 존재하지 않고, 다른 엔티티의 일부로써 함께 저장됩니다.

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString

//Comparable<ProductImage> 인터페이스 구현
//
//	•	compareTo(ProductImage o):
//        •	이 메서드는 두 ProductImage 객체를 비교할 때 사용됩니다.
//        •	비교 기준은 ino 필드입니다.
//        •	this.ino - o.ino라는 코드는 현재 객체의 ino 값과 비교 대상 객체의 ino 값을 뺍니다.
//        •	만약 현재 객체의 ino가 더 크다면 양수를 반환하고, 더 작다면 음수를 반환합니다. 같다면 0을 반환합니다.
//        •	이 메서드를 구현함으로써 ProductImage 객체들이 SortedSet(예: TreeSet)에 저장될 때 자동으로 ino 기준으로 정렬됩니다.

public class ProductImage implements Comparable<ProductImage> {
// 기본 키(@Id)가 없기 때문에 엔티티로 만들 수 없습니다.
// 대신 Product 엔티티와 같은 다른 엔티티에 연관 관계로 포함됩니다.
    private int ino;
    private String filename;


    @Override
    public int compareTo(ProductImage o) {
        return this.ino - o.ino;
    }




}
// 기본키가 없어서, 엔티티로 만들 수 없다.
// 따라서 연관관계로 만든다 .