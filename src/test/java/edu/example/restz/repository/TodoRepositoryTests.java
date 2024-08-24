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
// @SpringBootTest 는 프로젝트 전체 실행, @DataJpaTest는 엔티티 같이 DB관련 부분만 실행
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
// 메서드 단위로 트랜잭션이 처리되지 않도록 해 두었음, 아래에 @Transaction 으로 ..
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
        Optional<Todo> foundTodo
                = todoRepository.findById(mno);
        assertNotNull(foundTodo);
        assertEquals(mno, foundTodo.get().getMno());

        foundTodo
                = todoRepository.findById(mno);
        assertNotNull(foundTodo);
        assertEquals(mno, foundTodo.get().getMno());
        // 동일 한 .findById(mno)를 통해 호출하였지만, 트랜잭션을 통해 JPA 영속 컨텍스트가 유지되어
        // 윗 단에서 셀렉트 한 결과로 보관되고 있는 엔티티 객체를 그대로 사용 (보관= 1차 캐시)

        // 비슷한 논리로, update&delete 실행시 변경감지(더티체킹)를 통해 영속 컨텍스트를 디비에 반영함
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
        // 엔티티 클래스에 만들어 놓은 체인지 메소드를 통해 데이터의 영속성을 변경한다.

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
        foundTodo.get().changeWriter("CHANGERS");

        assertEquals("Title changed", foundTodo.get().getTitle());
        assertEquals("CHANGERS", foundTodo.get().getWriter());

        foundTodo = todoRepository.findById(mno);
        assertEquals("Title changed", foundTodo.get().getTitle());
        assertEquals("CHANGERS", foundTodo.get().getWriter());

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
// ======여기까지는 CRUD 및 트랜잭션 테스트, 아래는 페이징 =======

    //   1.  * findAll() : 모든 데이터를 조회, 메서드의 파라미터로 Pageable 타입 지정 가능-> 자동 페이징 처리
    @Test // 페이징 테스트 (카운트 & 진짜 페이지 )
    public void testFindAll() {
        Pageable pageable
                = PageRequest.of(0, 10, Sort.by("mno").descending());
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

//    2. * @Query : JPQL이라는 쿼리언어로 작성, SQl과 유사한 형식, 어떠한 DB든 동일하게 동작(종속 X)
// testListAll 은 repository>TodoRepository interface에 정의되어 있다.
    @Test // 쿼리 테스트
    public void testListAll(){

        Pageable pageable
                = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Todo> todoPage = todoRepository.ListAll(pageable);

        assertNotNull(todoPage);
        assertEquals(100, todoPage.getTotalElements());
        assertEquals(10, todoPage.getTotalPages());
        assertEquals(0, todoPage.getNumber());
        assertEquals(10, todoPage.getSize());

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

//    3.  * Querydsl / JQQQ 라이브러리
    // 아래  Search 메소드들은  repository>search 디렉토리에 정의해 놓았다 .
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
