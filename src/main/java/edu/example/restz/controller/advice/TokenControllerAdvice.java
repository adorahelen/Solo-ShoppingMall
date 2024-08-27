package edu.example.restz.controller.advice;

import edu.example.restz.exception.MemberTaskException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TokenControllerAdvice {

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Object> handleAuthorizationDeniedException(AuthorizationDeniedException e) {

        Map<String, Object> body = new HashMap<>();
        body.put("messa2ge", e.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }


//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<?> handleException(AccessDeniedException e) {
//
//        Map<String, Object> errMap = new HashMap<>();
//        errMap.put("message", e.getMessage());
//
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errMap);
//    }

    @ExceptionHandler(MemberTaskException.class)
    public ResponseEntity<?> handleException(MemberTaskException e) {
        Map<String, Object> errMap = new HashMap<>();
        errMap.put("error", e.getMessage());

        return ResponseEntity.status(e.getCode()).body(errMap);

    }
}
