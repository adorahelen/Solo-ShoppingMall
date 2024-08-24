package edu.example.restz.dto;


import edu.example.restz.entity.Todo;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TodoDTO {
    // JPA 에서
    // 엔티티 (이 프로젝트 내에서는 Todo)
    // 엔티티 객체들은 영속 컨텍스트가 관리하고 있는 존재이기 때문에 가능하면 여러 곳에서 사용하지 않도록
    // 안전성을 보장 받을 수 있다.
    // => 엔티티 객체 사용 X, DTO 등으로 변환해서 사용하게 된다.
    // => 이때 Spring Data JPA 가 제공하는 Projections 라는 기능을 이용
    // 이러한 DTO 는 Entity 데이터를 복사해서 가지고 있는 객체이기 때문에, 읽기 쓰기 시켜도 OK

    private Long mno;

    @NotEmpty
    private String title;
    @NotEmpty
    private String writer;
    @FutureOrPresent
    private LocalDate dueDate;

    public TodoDTO(Todo todo) { // 이게 위에서 쓰이고 (넓게) 아래까지
        this.mno = todo.getMno();
        this.title = todo.getTitle();
        this.writer = todo.getWriter();
        this.dueDate = todo.getDueDate();

    }

    public Todo toEntity() { // 이거 디비에 넣을떄 엔티티로 바꾸는 거지  반환값을 보아라
        return Todo.builder()
                .mno(mno)
                .title(title)
                .writer(writer)
                .dueDate(dueDate).build();

    }
}
