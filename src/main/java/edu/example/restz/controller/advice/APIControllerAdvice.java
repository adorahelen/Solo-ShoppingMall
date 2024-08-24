package edu.example.restz.controller.advice;

import edu.example.restz.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class APIControllerAdvice {
// @ExceptionHandler 메서드는 ResponseEntity를 통해 사용자에게 에러 메시지와 상태 코드를 반환


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleArgsException(MethodArgumentNotValidException e) {
        Map<String, Object> errMap = new HashMap<>();
        e.getBindingResult().
                getFieldErrors().forEach(err -> errMap.put(err.getField(),
                                                            err.getDefaultMessage()));

        return new ResponseEntity<>(errMap, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {
//        Map<String, Object> errMap = new HashMap<>();
//        errMap.put("error", e.getMessage());
//        errMap.put("code", e.getCode());
//        return new ResponseEntity<>(errMap, HttpStatus.NOT_FOUND);
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("message", e.getMessage());
        errMap.put("code", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errMap, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Map<String, Object> errMap = new HashMap<>();
        //errMap.put("message", e.getMessage()); 코드를 추가로 작성할 필요가 없는게 AOP
        errMap.put("error", "타입이 미스입니다.");
        errMap.put(e.getName(), e.getValue() + " is Not Valid value");
        return new ResponseEntity<>(errMap, HttpStatus.BAD_REQUEST);
    }

    //NoResourceFoundException
//    @ExceptionHandler(NoResourceFoundException.class)
//    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException e) {
//        Map<String, Object> errMap = new HashMap<>();
//        errMap.put("error", "view 파일이 존재하지 않습니다. ");
//        errMap.put("code", HttpStatus.NOT_FOUND);
//        return new ResponseEntity<>(errMap, HttpStatus.NOT_FOUND);
//    }

}
