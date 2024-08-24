package edu.example.restz.controller;

import edu.example.restz.dto.PageRequestDTO;
import edu.example.restz.dto.TodoDTO;
import edu.example.restz.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/todo")
@Log4j2
public class TodoController {
    private final TodoService todoService;


    @PostMapping("")
    public ResponseEntity<TodoDTO> register(
            @Validated @RequestBody TodoDTO todoDTO) {
        log.info("Register () --====" + todoDTO); // 로그로 출력
        return ResponseEntity.ok(todoService.register(todoDTO));
    }

    @GetMapping("/{mno}")
    public ResponseEntity<TodoDTO> read( @PathVariable Long mno){
        return ResponseEntity.ok(todoService.read(mno));
    }

    @GetMapping
    public ResponseEntity<Page<TodoDTO>> readAll(PageRequestDTO pageRequestDTO){
        log.info("ReadAll () --====" + pageRequestDTO);
        return ResponseEntity.ok(todoService.getList(pageRequestDTO));
    }

    @PutMapping("/{mno}")
    public ResponseEntity<TodoDTO> update(@Validated @RequestBody TodoDTO todoDTO){
        log.info("Update () --====" + todoDTO);
        return ResponseEntity.ok(todoService.modify(todoDTO));
    }

    @DeleteMapping("/{mno}")
    public ResponseEntity< Map<String, String>> delete(@PathVariable("mno") Long mno){
        log.info("Delete () --====" + mno);
        todoService.delete(mno);
        Map<String, String> result = Map.of("ressult", "Successfully deleted");
// 일부로 JSON 형식으로 작성
        return ResponseEntity.ok(result);

    }
}
