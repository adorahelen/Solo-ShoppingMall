package edu.example.restz.controller;

import edu.example.restz.dto.MemberDTO;
import edu.example.restz.security.util.JWTUtil;
import edu.example.restz.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController  // RESTful 웹 서비스를 위한 컨트롤러임을 나타냄
@RequiredArgsConstructor  // final 필드에 대한 생성자를 Lombok이 자동으로 생성
@RequestMapping("/api/v1/token")  // 이 컨트롤러의 기본 요청 경로 설정
@Log4j2  // Log4j2를 사용한 로깅을 가능하게 함

// 4가지 토큰 메소드 + 2가지 예외 처리 메소드
public class TokenController {
    private final MemberService memberService;  // 회원 서비스 (사용자 정보 확인 및 관리)
    private final JWTUtil jwtUtil;  // JWT 유틸리티 (토큰 생성 및 검증)

    // ============= 1. 메소드 : makeToken [ 엑세스 토큰과 , 리프레쉬 토큰을 생성 후 반환한다, ]
    // 토큰 생성 메서드
    @PostMapping("/make")  // POST 요청을 처리하고 /make 경로로 호출
    public ResponseEntity<Map<String, Object>> makeToken(@RequestBody MemberDTO memberDTO) {
        log.info("makeToken() ------- ");

        // 사용자 정보를 데이터베이스에서 조회
        MemberDTO foundMemberDTO = memberService.read(memberDTO.getMid(), memberDTO.getMpw());
        log.info("--- foundMemberDTO : " + foundMemberDTO);

        // JWT 페이로드로 사용할 사용자 정보를 가져옴
        Map<String, Object> payloadMap = foundMemberDTO.getPayload();

        // JWT 액세스 토큰 생성 (60분 유효)
        String accessToken = jwtUtil.createToken(payloadMap, 60);

        // JWT 리프레시 토큰 생성 (7일 유효)
        String refreshToken = jwtUtil.createToken(Map.of("mid", foundMemberDTO.getMid()), 60 * 24 * 7);

        log.info("--- accessToken : " + accessToken);
        log.info("--- refreshToken : " + refreshToken);

        // 생성된 토큰들을 응답으로 반환
        return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
    }

// ======== 2. 메소드 : handleException 예외처리
    // 예외 처리 메서드 - 상태 코드와 메시지를 전송
    public ResponseEntity<Map<String, String>> handleException(String message, int status) {
        return ResponseEntity.status(status)
                .body(Map.of("error", message));
    }

 // ==============3. 메소드 : refreshToken, 리프레쉬 토큰을 통해 새로운 엑세스 토큰을 발급
    // 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급하는 메서드
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(
            @RequestHeader("Authorization") String headerAuth,
            @RequestParam("refreshToken") String refreshToken,
            @RequestParam("mid") String mid) {

        log.info("--- refreshToken() ");
        log.info("--- Authorization : " + headerAuth);
        log.info("--- refreshToken : " + refreshToken);
        log.info("--- mid : " + mid);

        // 파라미터 값 검증
        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) return handleException("액세스 토큰이 없습니다.", 400);
        if (refreshToken == null || refreshToken.isEmpty()) return handleException("리프레시 토큰이 없습니다.", 400);
        if (mid == null || mid.isEmpty()) return handleException("아이디가 없습니다.", 400);

        try {
            // 액세스 토큰 유효성 검증 (만료 여부 확인)
            String accessToken = headerAuth.substring(7);
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);
            log.info("--- 액세스 토큰 유효성 검증 완료 ---");

        } catch (ExpiredJwtException e) {
            log.info("--- 액세스 토큰 만료 기간 경과");

            try {
                // 리프레시 토큰을 검증하고 새로운 토큰 생성
                return ResponseEntity.ok(makeNewToken(mid, refreshToken));
            } catch (ExpiredJwtException ee) {
                log.info("--- 리프레시 토큰 만료 기간 경과");
                return handleException("리프레시 토큰 기간 만료 : " + ee.getMessage(), 400);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return handleException("리프레시 토큰 검증 예외 : " + e.getMessage(), 400);
        }
        return null;
    }

// ============== 4. 새로운 엑세스 토큰과 리프레쉬 토큰을 만들어, 반환한다 : make"new"Token
    // 새로운 액세스 토큰과 리프레시 토큰을 생성하는 메서드
    public Map<String, String> makeNewToken(String mid, String refreshToken) {
        // 리프레시 토큰 검증
        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
        log.info("--- 리프레시 토큰 유효성 검증 완료 ---");

        // 토큰의 사용자 ID가 요청된 ID와 일치하는지 확인
        if (!claims.get("mid").equals(mid)) {
            throw new RuntimeException("INVALID REFRESH TOKEN mid");
        }

        log.info("--- make new tokens ---");

        // 사용자 정보를 다시 조회
        MemberDTO foundMemberDTO = memberService.read(mid);

        // 새로운 JWT 토큰을 생성
        Map<String, Object> payloadMap = foundMemberDTO.getPayload();
        String newAccessToken = jwtUtil.createToken(payloadMap, 60);   // 60분 유효
        String newRefreshToken = jwtUtil.createToken(Map.of("mid", mid), 60 * 24 * 7);  // 7일 유효

        // 생성된 토큰들과 사용자 ID를 반환
        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken, "mid", mid);
    }

    // ======== 5. 예외처리 2
    // 상태 코드 400과 메시지 전송하는 메서드
    public ResponseEntity<Map<String, String>> sendResponse(String message) {
        return new ResponseEntity<>(Map.of("error", message), HttpStatus.BAD_REQUEST);
    }

    // ======== 6. 메소드 : refreshVerify
    // 리프레시 토큰 검증 메서드
    @PostMapping("/refreshVerify")
    public ResponseEntity<Map<String, String>> refreshVerify(
            @RequestHeader("Authorization") String headerAuth,
            @RequestParam("refreshToken") String refreshToken,
            @RequestParam("mid") String mid) {

        // 파라미터 값 확인
        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            return sendResponse("엑세스 토큰이 없습니다.");
        }
        if (refreshToken == null || refreshToken.isEmpty()) {
            return sendResponse("리프레쉬 토큰이 없습니다.");
        }
        if (mid.isEmpty()) {
            return sendResponse("아이디가 없습니다.");
        }

        try {
            // 액세스 토큰 유효성 검증
            String accessToken = headerAuth.substring(7);
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);
            log.info("---1. 엑세스 토큰 유효");

        } catch (ExpiredJwtException e) {
            log.info("2. 엑세스 토큰 만료기간 경과");

            try {
                // 리프레시 토큰 유효성 검증
                Map<String, Object> claims = jwtUtil.validateToken(refreshToken);
                log.info("--- 3. 리프레쉬 토큰 유효");

                // 토큰의 사용자 ID가 요청된 ID와 일치하는지 확인
                if (!claims.get("mid").equals(mid)) {
                    return sendResponse("INVALID REFRESH TOKEN mid");
                }

                log.info("--- 4. 새로운 토큰 생성");

                // 사용자 정보를 다시 조회하고 새로운 토큰 생성
                MemberDTO foundMemberDTO = memberService.read(mid);
                Map<String, Object> payloadMap = foundMemberDTO.getPayload();
                String newAccessToken = jwtUtil.createToken(payloadMap, 60);   // 60분 유효
                String newRefreshToken = jwtUtil.createToken(Map.of("mid", mid), 60 * 24 * 7);  // 7일 유효

                // 생성된 토큰들과 사용자 ID를 반환
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken, "mid", mid));

            } catch (ExpiredJwtException ee) {
                log.info("--- 5. 리프레쉬 토큰 만료기간 경과");
                return sendResponse("리프레쉬 토큰 만료기간 경과: " + ee.getMessage());
            }
        }
        return null;
    }
}