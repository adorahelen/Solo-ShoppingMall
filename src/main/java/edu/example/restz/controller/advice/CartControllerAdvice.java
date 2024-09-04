package edu.example.restz.controller.advice;

import edu.example.restz.exception.CartTaskException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class CartControllerAdvice {

    // 장바구니는 사용자 인증 정보가 요구됨
    // <- 인증된 사용자가 가진 장바구니||아이템을 이용해서 기능을 체크(구현)

    @ExceptionHandler(CartTaskException.class)
    public ResponseEntity<Map<String, String>> handleClassCastException(CartTaskException e) {
        int status = e.getStatus();
        String message = e.getMessage();

        return ResponseEntity.status(status).body(Map.of("message", message));
    }
}
