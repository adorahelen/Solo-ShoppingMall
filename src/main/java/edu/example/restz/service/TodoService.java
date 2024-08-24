package edu.example.restz.service;

import edu.example.restz.dto.PageRequestDTO;
import edu.example.restz.dto.TodoDTO;
import edu.example.restz.entity.Todo;
import edu.example.restz.exception.EntityNotFoundException;
import edu.example.restz.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service // 매개변수나 리턴타입 그리고 예외처리를 중심으로
@RequiredArgsConstructor
@Transactional
@Log4j2
public class TodoService {
    private final TodoRepository todoRepository;

    // 목록 조회
    public Page<TodoDTO> getList(PageRequestDTO pageRequestDTO){ // 목록
        Sort sort = Sort.by("mno").descending();
        Pageable pageable = pageRequestDTO.getPageable(sort);

        return todoRepository.searchDTO(pageable);
    }
    // CREATE
    public TodoDTO register(TodoDTO todoDTO) {
        // DTO 를 엔티티 객체로 변환한다
        Todo todo = todoDTO.toEntity();
        // 리포지토리를 이용해 저장한다.
        todoRepository.save(todo);
        // DTO에 저장된 번호를 넣어서 반환한다.
        return new TodoDTO(todo);
    }
    // READ
    public TodoDTO read(Long mno) {
        Optional<TodoDTO> todoDTO = todoRepository.getTodoDTO(mno);
        return todoDTO.orElseThrow(()->
                new EntityNotFoundException(" !!! Todo not found 엔티티를 찾을 수 없습니다 (TodoService 예외처리) " + mno));
        // 값이 없으면, 개발자가 정의해놓은 엔티티 낫 파운드 예외를 발생시키면서, 정의해 놓은 메시지를 보낸다.
    }
    // Update
    public TodoDTO modify(TodoDTO todoDTO) {
        Optional<Todo> todo = todoRepository.findById(todoDTO.getMno());
        Todo modifyTodo = todo.
                orElseThrow((() -> new EntityNotFoundException("Todo not found" + todoDTO.getMno() + ";;==")));
        // 필요한 부분 수정, 변경이 감지되면 수정 처리 진행
        modifyTodo.changeTitle(todoDTO.getTitle());
        modifyTodo.changeWriter(todoDTO.getWriter());
        modifyTodo.changeDueDate(todoDTO.getDueDate());
        return new TodoDTO(modifyTodo);
    }
    // Delete
    public void delete(Long mno) {
        Optional<Todo>todo  = todoRepository.findById(mno); // 1 해당 데이터 조회
        Todo todoToDelete = todo.orElseThrow(()-> // 2. 예외처리 걸면서 다른 변수로 이동
                new EntityNotFoundException("Todo not found " + mno + " ;;;; "));
        todoRepository.delete(todoToDelete); // 3. 전달 후 삭제 호출
    }
}
