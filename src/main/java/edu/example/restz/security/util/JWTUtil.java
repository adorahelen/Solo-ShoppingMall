package edu.example.restz.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
// 비밀키 : SecretKey는 충분히 길고 복잡한 문자열이어야 합니다. 이 키가 유출되면 JWT의 무결성이 훼손
// * 만료 시간(exp 클레임)을 포함하여 지정된 시간 이후에는 사용이 불가능
// * validateToken 메서드에서 만료된 토큰 -> ExpiredJwtException을 발생

@Component
public class JWTUtil {
    private static final Logger log = LoggerFactory.getLogger(JWTUtil.class);
    // 서명에 사용할 키 30자 이상
    private static String key = "12345678901112131415161718192021222324252627282930";
    // JWT 문자열 생성

    // ============= 1.  JWT 토큰 생성 메서드 =================
    // valueMap에 포함된 데이터를 JWT의 클레임으로 포함시키고, 지정된 시간(min 분) 동안 유효한 토큰을 생성
    // JWT는 HS256 알고리즘으로 서명되며, 만료 시간과 발행 시간을 포함
    public String createToken(Map<String, Object> valueMap, int min) {
        // 저장할 문자열 + 만료 시간 - 분 단위로
        SecretKey key = null;

        try { key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8")); // 비밀 키를 사용하여 SecretKey 생성
        } catch (UnsupportedEncodingException e) { throw new RuntimeException(e); }

        Date now = new Date(); // 토큰 발행 시간 // 현재 시간

        return Jwts.builder()
                .header().add("alg", "HS256")// 헤더 설정: 알고리즘과 타입 지정
                .add("type", "JWT")// 헤더 설정: 알고리즘과 타입 지정
                .and()
                .issuedAt(now) // 발급 시간 지금, 밑에는 만료시간 설정
                .expiration( new Date( now.getTime() + Duration.ofMinutes(min).toMillis()))
                .claims(valueMap) // 클레임(사용자 정보) 설정
                .signWith(key) // 비밀 키를 사용해 서명
                .compact(); // 토큰 생성 및 반환...
    }

    // =============== 2. 토큰 검증 및 파싱(복호화) 메서드 ===================
    // 주어진 JWT 토큰을 검증하고, 유효한 경우 클레임(즉, 토큰에 포함된 데이터)을 반환
    // 비밀 키를 사용해 서명이 검증되며, 서명 검증이 실패하면 예외가 발생
    public Map<String, Object> validateToken(String token) {
        SecretKey key = null;
        try {
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8")); // 비밀 키 생성
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        // 토큰을 파싱하고 클레임(Claims) 추출
        Claims claims = Jwts.parser()
                .verifyWith(key) // 비밀 키로 서명 검증
                .build()
                .parseSignedClaims(token)// 토큰 파싱
                .getPayload(); // 클레임 추출

        log.info("====Claism : " + claims);  // 클레임 로깅

        return claims; // 추출된 클레임 반환
    }
}
