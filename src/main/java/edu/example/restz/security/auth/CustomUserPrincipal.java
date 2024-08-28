package edu.example.restz.security.auth;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor  // final로 선언된 필드에 대해 생성자를 자동으로 생성해주는 Lombok 어노테이션
public class CustomUserPrincipal implements Principal {
// Principal => Java의 보안 프레임워크에서 사용자를 나타내는 객체,  보통 사용자의 ID나 이름과 같은 식별자를 제공하는 역할

    // 사용자의 식별자 (예: 사용자 ID, 이메일 등)를 저장하기 위한 필드
    private final String mid;

    // Principal 인터페이스의 메서드를 오버라이드하여 사용자 이름(식별자)을 반환
    // 이 인터페이스의 유일한 "메서드"인 getName()을 구현하여, 사용자의 식별자를 반환하도록 합니다.
    @Override
    public String getName() {
        return mid;  // 생성자를 통해 전달받은 사용자 식별자를 반환
        // 이 값은 사용자의 고유 식별자(ID)나 이름을 의미
    }
} //  =========이 클래스는 주로 보안 관련 로직에서 사용자를 식별하기 위해 사용됨====================