package edu.example.restz.service;

import edu.example.restz.dto.PageRequestDTO;
import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.Todo;
import edu.example.restz.exception.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Log4j2
public class TodoServiceTests {
    @Autowired
    private TodoService todoService;
    //1. 필드 인젝션 어노테이션
    //2. 객체 선언
    // 3. Junit 테스트를 위한 어노테이션
    @Test
    public void testRegister(){
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle("Service Test");
        todoDTO.setWriter("SERVER");
        todoDTO.setDueDate(LocalDate.of(2024, 12, 31));
        // 4. TodoDTO 객체를 생성하여 임의의 데이터를 저장한 후
        // given

        // 임의의 데이터를 저장한 후
       TodoDTO saveTodoDTO = todoService.register(todoDTO);
        // 5. 데이터베이스에 저장하는 메소드 호출하고 반환되는 값을 저장
        // when

        // then - under all
        // 6. 저장된 결과가 null이 아닌지 검증
        assertNotNull(saveTodoDTO);
      //   Long mno = 4L;
        // 7. 반환된 결과의 writer 가 4에서 지정한 결과가 같은지 검증
        assertEquals("SERVER", saveTodoDTO.getWriter());
        // 8. 반환된 격체를 info 레벨의 로그 출력
        log.info(saveTodoDTO);
    }

    @Test
    public void testRead(){
//        TodoDTO todoDTO = todoService.read(102L);
//        assertNotNull(todoDTO);
//        assertEquals("SERVER", todoDTO.getWriter());
//        log.info(todoDTO);
        Long mno = 102L; // given

        TodoDTO todoDTO = todoService.read(mno); // when

        assertNotNull(todoDTO); // then
        // assertEquals("SERVER", todoDTO.getWriter());
        assertEquals(mno, todoDTO.getMno());
        log.info(todoDTO);


    }

    @Test
    public void testDelete(){
        try {
            Long mno = 4L; // given : 4
            todoService.delete(mno); // when : 4
        } catch (EntityNotFoundException e ){
            log.info(e.getMessage() + "============");
            // then : 4 번 최초 삭제 시에는 예외 없음
            // 삭제 된 이후 재시도 시 예외코드가 404 와 가틍ㄴ지
            assertEquals(404, e.getCode());
        }
    }

    @Test
    public void testModify(){
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setMno(5L);
        todoDTO.setTitle("Service Modified");
        todoDTO.setWriter("modifier");
        todoDTO.setDueDate(LocalDate.of(2024, 12, 31));

        TodoDTO ModifyedTodoDTO = todoService.modify(todoDTO);

        assertNotNull(ModifyedTodoDTO);
        assertEquals("Service Modified", ModifyedTodoDTO.getTitle());
        assertEquals("modifier", ModifyedTodoDTO.getWriter());
        log.info(ModifyedTodoDTO);

    }

    @Test
    public void testGetList(){
       // Pageable pageable = PageRequest.of(9, 10, Sort.by)
        PageRequestDTO pageRequestDTO = new PageRequestDTO();// 기본 생성자

        Page<TodoDTO> todoPage = todoService.getList(pageRequestDTO);

        assertNotNull(todoPage);
        assertEquals(100, todoPage.getTotalElements());
        assertEquals(10, todoPage.getTotalPages());
        assertEquals(0, todoPage.getNumber());
        assertEquals(10, todoPage.getSize());
        assertEquals(10, todoPage.getContent().size());
        todoPage.getContent().forEach(System.out::println);
    }
}
