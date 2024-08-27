package edu.example.restz.controller;

import edu.example.restz.dto.MemberDTO;
import edu.example.restz.security.util.JWTUtil;
import edu.example.restz.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
@Log4j2
public class TokenController {
    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @PostMapping("/make") // POST 로 요청을 처리하는 메서드
    public ResponseEntity<?> makeToken(@RequestBody MemberDTO memberDTO){
        log.info("=======!_!====Making token========!_!=====");
        MemberDTO foundMemberDTO = memberService.read(memberDTO.getMid(), memberDTO.getMpw());
        log.info("!+!+!+!+!+Found member!+!+!+!+!+: " + foundMemberDTO);

        // 토큰을 생성하자
        Map<String, Object> payloadMap = foundMemberDTO.getPayload();
        String accessToken = jwtUtil.createToken(payloadMap, 10); // 10분 유효한 접근 토큰
        String refreshToken = jwtUtil.createToken(Map.of("mid", foundMemberDTO.getMid())
                , 60*24*7); // 7일 유효한 리프레쉬 토큰

        log.info("accessToken: " + accessToken);
        log.info("refreshToken: " + refreshToken);
        return ResponseEntity.ok(Map.of("accessToken" ,accessToken, "refreshToken" ,refreshToken));
    }
}
//토큰 컨트롤러러 POST 요청일 들어오면,
//        -> 맴버 서비스에서 id랑 pw를 가지고와서 foundMemberDTo로 반환
//-가지거온 foundDTo를 기반으로 페이로드 생성 >페이로드나, 파운드DTO를 바탕으로 엑세스/리프레쉬 토큰 생성
