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

@Component
public class JWTUtil {
    private static final Logger log = LoggerFactory.getLogger(JWTUtil.class);
    // 서명에 사용할 키 30자 이상
    private static String key = "12345678901112131415161718192021222324252627282930";
    // JWT 문자열 생성

    // 토큰 == 임호화 적용
    public   String createToken(Map<String, Object> valueMap, int min) {
        // 저장할 문자열 + 만료 시간 - 분 단위로
        SecretKey key = null;
        try {
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        Date now = new Date(); // 토큰 발행 시간
        return Jwts.builder().header().add("alg", "HS256").add("type", "JWT")
                .and().issuedAt(now).expiration( new Date( now.getTime() +
                        Duration.ofMinutes(min).toMillis())).claims(valueMap).signWith(key).compact();
                                    // 토큰 발행 시간 + 토큰 만료 시간 +  저장 데이터 + 서명
    }

    // 검증 기능 수행  후 해독 ( == 토큰 해독 : 복호화 알고리즘 적용 )
    public Map<String, Object> validateToken(String token) {
        SecretKey key = null; // 동일
        try {
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } // 동일

        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        log.info("====Claism : " + claims); // 여기까지가 검증




        return claims;
    }
}
