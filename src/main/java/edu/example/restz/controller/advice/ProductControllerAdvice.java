package edu.example.restz.controller.advice;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
public class ProductControllerAdvice {

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
        handleMethodArgumentNotValidException(
                MethodArgumentNotValidException ex) {

        log.error("handleMethodArgumentNotValidExceptio............n");
        log.error(ex.getMessage());

        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        String errorMessage = errors.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
    }
}
