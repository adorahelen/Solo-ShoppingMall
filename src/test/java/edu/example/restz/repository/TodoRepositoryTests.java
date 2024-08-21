package edu.example.restz.repository;

import edu.example.restz.entity.Todo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class TodoRepositoryTests {
    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testInsert() {
        // Given - todo entity 객체 엔티티 생성
        Todo todo = Todo.builder()
                .title("JPA 테스트")
                .writer("tester")
                .dueDate(LocalDate.of(2024, 8, 31))
                .build();

        // When - 엔티티 저장
        Todo saveTodo = todoRepository.save(todo);

        // then - 기댓값, : saveTodo 는 NULL 이 아니며, 아이디는 1이다.
        assertNotNull(saveTodo);
        assertEquals(1, saveTodo.getMno());

    }

}
