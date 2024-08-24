package edu.example.restz.repository;

import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.Todo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Log4j2
public class TodoRepositoryTests {
    @Autowired
    private TodoRepository todoRepository;

    @Test // 단순 삽입
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

    @Test // 100 개 데이터 추가
    public void testDataInsert() {
        // Given - todo entity 객체 엔티티 생성
        IntStream.rangeClosed(1, 100).forEach(i -> {

            Todo todo = Todo.builder().title("Todo test" + i)
                    .writer("tester" + i)
                    .dueDate(LocalDate.of(2024, 8, 31))
                    .build();

            // When - 엔티티 저장
            Todo saveTodo = todoRepository.save(todo);

            // then - 기댓값, : saveTodo 는 NULL 이 아니며, 아이디는 1이다.
            assertNotNull(saveTodo);


        });


    }

    @Test // select test
    public void testFindByID() {
        // given // @id type value, find entity

        // given
        Long mno = 1L;
        // when
        Optional<Todo> foundTodo = todoRepository.findById(mno);
        // then
        assertNotNull(foundTodo);
        assertEquals(mno, foundTodo.get().getMno());

        log.info("found todo: {}", foundTodo);
        log.info("mno: {}", foundTodo.get().getMno());

    }

    @Test // select test - No @Transaction
    public void testFindByIdNoTransactional() {
        // given // @id type value, find entity

        // given
        Long mno = 1L;
        Optional<Todo> foundTodo = todoRepository.findById(mno);
        assertNotNull(foundTodo);
        assertEquals(mno, foundTodo.get().getMno());

        foundTodo = todoRepository.findById(mno);
        assertNotNull(foundTodo);
        assertEquals(mno, foundTodo.get().getMno());

    }

    @Test // select test - with @Transaction
    @Transactional
    public void testFindByIdTransactional() {
        // given // @id type value, find entity

        // given
        Long mno = 1L;
        Optional<Todo> foundTodo = todoRepository.findById(mno);
        assertNotNull(foundTodo);
        assertEquals(mno, foundTodo.get().getMno());

        foundTodo = todoRepository.findById(mno);
        assertNotNull(foundTodo);
        assertEquals(mno, foundTodo.get().getMno());

    }

    @Test // Update test -Transaction NO
    @Commit
    public void testUpdateNoTransactional() {
        // given // @id type value, find entity

        // given
        Long mno = 2L;
        Optional<Todo> foundTodo = todoRepository.findById(mno);
        foundTodo.get().changeTitle("Title changed");
        foundTodo.get().changeWriter("Changer");

        assertEquals("Title changed", foundTodo.get().getTitle());
        assertEquals("Changer", foundTodo.get().getWriter());

        foundTodo = todoRepository.findById(mno);
        assertEquals("Title changed", foundTodo.get().getTitle());
        assertEquals("Changer", foundTodo.get().getWriter());

    }

    @Test // Update test -Transaction
    @Commit
    @Transactional
    public void testUpdateTransactional() {
        // given // @id type value, find entity

        // given
        Long mno = 2L;
        Optional<Todo> foundTodo = todoRepository.findById(mno);
        foundTodo.get().changeTitle("Title changed");
        foundTodo.get().changeWriter("Changer");

        assertEquals("Title changed", foundTodo.get().getTitle());
        assertEquals("Changer", foundTodo.get().getWriter());

        foundTodo = todoRepository.findById(mno);
        assertEquals("Title changed", foundTodo.get().getTitle());
        assertEquals("Changer", foundTodo.get().getWriter());

    }

    @Test
    @Transactional
    @Commit
    public void testDelete(){
        Long mno = 3L;
        todoRepository.deleteById(mno);

        Optional<Todo> foundTodo = todoRepository.findById(mno);
        assertTrue( foundTodo.isEmpty());

    }

    @Test // 페이징 테스트 (카운트 & 진짜 페이지 )
    public void testFindAll() {
        Pageable pageable = PageRequest
                .of(0, 10, Sort
                        .by("mno")
                        .descending());
        // 페이지 번호, 한 페이지 게시물, 게시물 정렬 기준

        Page<Todo> todoPage = todoRepository.findAll(pageable);
        assertNotNull(todoPage);
        assertEquals(100, todoPage.getTotalElements());
        assertEquals(10, todoPage.getTotalPages());
        assertEquals(0, todoPage.getNumber()); // 현재 페이지
        assertEquals(10, todoPage.getSize());
        assertEquals(10, todoPage.getContent().size());

        todoPage.getContent().forEach(System.out::println);
    }


    @Test // 쿼리 테스트
    public void testListAll(){

        Pageable pageable = PageRequest.of(0, 10
        ,Sort.by("mno").descending());

        Page<Todo> todoPage = todoRepository.ListAll(pageable);

        assertNotNull(todoPage);
        assertEquals(100, todoPage.getTotalElements());
        assertEquals(10, todoPage.getTotalPages());
        assertEquals(0, todoPage.getNumber());
        assertEquals(10, todoPage.getSize());

    }

    @Test // 쿼리 디에스엘 테스트
    public void testSearch(){
        Pageable pageable = PageRequest.of(9, 10, Sort.by("mno").descending());

        Page<Todo> todoPage = todoRepository.search(pageable);
        assertNotNull(todoPage);
        assertEquals(100, todoPage.getTotalElements());
        assertEquals(10, todoPage.getTotalPages());
        assertEquals(9, todoPage.getNumber());
        assertEquals(10, todoPage.getSize());
        assertEquals(10, todoPage.getContent().size());
    }

    @Test
    public void testGetTodoDTO() {
        Long mno = 2L;
        Optional<TodoDTO> foundTodoDTO
                = todoRepository.getTodoDTO(mno);

        assertNotNull(foundTodoDTO);
        assertEquals("Changer", foundTodoDTO.get().getWriter());

        foundTodoDTO.ifPresent(System.out::println);
    }

    @Test // 결과만 투두디티오로 나오도록 바꿈
    public void testSearchTodoDTO() {
        Pageable pageable = PageRequest.of(9, 10, Sort.by("mno").descending());

        Page<TodoDTO> todoPage = todoRepository.searchDTO(pageable);
        assertNotNull(todoPage);
        assertEquals(100, todoPage.getTotalElements());
        assertEquals(10, todoPage.getTotalPages());
        assertEquals(9, todoPage.getNumber());
        assertEquals(10, todoPage.getSize());
        assertEquals(10, todoPage.getContent().size());

        todoPage.getContent().forEach(System.out::println);
    }
}
