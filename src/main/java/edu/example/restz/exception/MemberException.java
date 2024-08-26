package edu.example.restz.exception;

public enum MemberException {
    // 맴버 예외에 대한 종류 4가지로 정의 : 하나씩 가져다가 사용 가능함

    NOT_FOUND("NOT_FOUND", 404),
    DUPLICATE("DUPLICATE", 409),
    INVALID("INVALID", 400),

    // 위에는 API 에러(회원정보), 아래는 토큰에 대한 에러 "잘못된 인증정보 ) "
    BAD_CREDENTIALS("BAD_CREDENTIALS", 401);

    private MemberTaskException memberTaskException;

    MemberException(String message, int code){
        this.memberTaskException = new MemberTaskException(message, code);
    }

    public MemberTaskException get(){
        return this.memberTaskException;
    }
}
