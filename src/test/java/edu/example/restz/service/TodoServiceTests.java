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

    @Test
    public void testRegister(){
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setTitle("Service Test");
        todoDTO.setWriter("SERVER");
        todoDTO.setDueDate(LocalDate.of(2024, 12, 31));

       TodoDTO saveTodoDTO = todoService.register(todoDTO);

        assertNotNull(saveTodoDTO);
        assertEquals("SERVER", saveTodoDTO.getWriter());
        log.info(saveTodoDTO);
    }

    @Test
    public void testRead(){
        Long mno = 102L; // given

        TodoDTO todoDTO = todoService.read(mno); // when

        assertNotNull(todoDTO); // then
        assertEquals(mno, todoDTO.getMno());
        log.info(todoDTO);
    }

    @Test
    public void testDelete(){
        try {
            Long mno = 4L; // given
            todoService.delete(mno); // when
        } catch (EntityNotFoundException e ){
            // then
            log.info(e.getMessage() + "============");
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

