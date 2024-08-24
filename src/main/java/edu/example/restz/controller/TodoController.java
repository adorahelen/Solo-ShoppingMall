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
@RequestMapping("/api/v1/todo") // 특정한 URI 반응 또는 공통적인 경로 처리
@Log4j2
public class TodoController {
    private final TodoService todoService;

//@PathVariable : 특정한 경로의 값을 변수로 사용하기 위해서
//@RequestParam : 쿼리스트링으로 전달되는 특정한 값을 처리하기 위해서
//@RequestBody : 파라미터로 전달되는 데이터를 객체형을 변환하기 위해서
    //         * JSON 으로 전달되는 데이터를, 특정한 DTO로 변환시 (해당 변수 앞에 적용)

    // @Validated : 검증 처리를 위한 어노테이션

    @PostMapping("")
    public ResponseEntity<TodoDTO> register(@Validated @RequestBody TodoDTO todoDTO) {
        log.info("Register () --====" + todoDTO); // 로그로 출력
        return ResponseEntity.ok(todoService.register(todoDTO));
    }

    @GetMapping("/{mno}")
    public ResponseEntity<TodoDTO> read(@PathVariable Long mno) {
        return ResponseEntity.ok(todoService.read(mno));
    }

    @GetMapping
    public ResponseEntity<Page<TodoDTO>> readAll(PageRequestDTO pageRequestDTO) {
        log.info("ReadAll () --====" + pageRequestDTO);
        return ResponseEntity.ok(todoService.getList(pageRequestDTO));
    }

    @PutMapping("/{mno}") // TodoDTO 안에 mno 정보가 들어있어서, @PathVariable 안씀
    public ResponseEntity<TodoDTO> update(@Validated @RequestBody TodoDTO todoDTO) {
        log.info("Update () --====" + todoDTO);
        return ResponseEntity.ok(todoService.modify(todoDTO));
    } // 내가 만든 블로그는 DTO 안에 mno 가 없기에 @PV를 사용함

    @DeleteMapping("/{mno}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable("mno") Long mno) {
        log.info("Delete () --====" + mno);
        todoService.delete(mno);
        Map<String, String> result = Map.of("ressult", "Successfully deleted");
// 일부로 JSON 형식으로 작성
        return ResponseEntity.ok(result);

    }
}

//    @DeleteMapping("/{mno}")
//    public ResponseEntity<Void> delete2(@PathVariable("mno") Long mno) {
//        todoService.delete(mno);
//        return ResponseEntity.ok().build();
//    }


