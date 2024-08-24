package edu.example.restz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tbl_todo")
public class Todo {
    @Id // PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long mno;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String writer;

    @Column
    private LocalDate dueDate;

    // setter
    // 영속 상태의 데이터를 변경하기 위해, 엔티티 클래스에 Change 메소드들을 추가한다.
    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeWriter(String writer) {
        this.writer = writer;
    }
    public void changeDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

}
