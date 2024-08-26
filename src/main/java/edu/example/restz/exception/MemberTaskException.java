package edu.example.restz.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberTaskException extends RuntimeException {
    // DTO처럼 예외 받아서 처리하고 비교하는 용도로 쓴다.
    private String message;
    private int code;
}
