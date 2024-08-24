package edu.example.restz.repository.search;

import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoSearch {
    Page<Todo> search(Pageable pageable); // 엔티티를 받아서 돌림
    Page<TodoDTO> searchDTO(Pageable pageable); // 엔티티 복사본을 받아서 돌림

// Querydsl 과 리포지토리 결합
    // 1 별도의 인터페이스 설계 => TodoSearch 라는 인터페이스 선언

    // + 2, 해당 인터페이스를 구현하는 클래스 작성
    // + 3, 기존 리포지토리에 추가
}
