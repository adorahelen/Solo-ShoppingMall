package edu.example.restz.security.filter;

import edu.example.restz.security.auth.CustomUserPrincipal;
import edu.example.restz.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component  // Spring의 Bean으로 등록되어 다른 컴포넌트에 주입 가능
@RequiredArgsConstructor  // final 필드를 초기화할 수 있는 생성자를 Lombok이 자동으로 생성
@Log4j2  // Log4j2를 사용하여 로그를 출력할 수 있게 해주는 Lombok 어노테이션
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;  // JWT 유틸리티 객체를 통해 토큰 검증 등 작업 수행

    // ================ 1. 특정 경로(토큰을 발급하는 경로)에 대해 필터를 적용하지 않도록 예외 처리
    @Override  // 이 메서드는 필터링을 적용할지 여부를 결정합니다.
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        log.info("--- shouldNotFilter() ");
        log.info("--- requestURI : " + request.getRequestURI());

        // "/api/v1/token/"로 시작하는 URI는 필터링을 적용하지 않음 (주로 토큰 발급 관련 경로)
        if(request.getRequestURI().startsWith("/api/v1/token/")) {
            return true;  // 이 경로는 필터링하지 않음
        }
        return false;  // 그 외의 경로는 필터링 적용
    }

    // ================= 2. 필터링을 수행하는 핵심 메서드 dofilter ===============
    // 요청의 Authorization 헤더에서 JWT 토큰을 추출하고,
    // 이를 검증한 후 SecurityContextHolder에 인증 정보를 설정
    // 검증에 성공하면 요청을 다음 필터로 전달하고, 실패하면 예외를 처리
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        log.info("--- doFilterInternal() ");
        log.info("--- requestURI : " + request.getRequestURI());

        String headerAuth = request.getHeader("Authorization");  // 요청 헤더에서 Authorization 정보를 가져옴
        log.info("--- headerAuth : " + headerAuth);

        // Authorization 헤더가 없거나 "Bearer "로 시작하지 않으면 403 예외 처리
        if(headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            handleException(response, new Exception("ACCESS TOKEN NOT FOUND"));
            return;
        }

        // JWT 토큰에서 "Bearer " 접두어를 제거하여 실제 토큰 값을 가져옴
        String accessToken = headerAuth.substring(7);

        try {
            // JWTUtil을 사용하여 토큰의 유효성을 검증하고 클레임(Claims)을 가져옴
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);
            log.info("--- 토큰 유효성 검증 완료 ---");

            // SecurityContextHolder에 인증 정보를 설정 (원래 통과하면 여기에 저장한 다음 추후 비교함, 책에 있음!)
            String mid = claims.get("mid").toString();  // 클레임에서 사용자 ID 추출
            String[] roles = claims.get("role").toString().split(",");  // 클레임에서 역할(Role) 정보를 추출

            // 추출된 정보로 UsernamePasswordAuthenticationToken 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    new CustomUserPrincipal(mid),  // 사용자 정보
                    null,  // 자격 증명은 JWT로 처리하므로 null로 설정
                    Arrays.stream(roles)  // 역할 정보를 GrantedAuthority로 변환
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList())
            );

            // SecurityContextHolder에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);  // 필터 체인의 다음 필터로 요청 전달
        } catch(Exception e) {
            handleException(response, e);  // 예외 발생 시 처리
        }
    }

    // 예외 발생 시 응답 처리 메서드
    public void handleException(HttpServletResponse response, Exception e)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403 Forbidden 상태 설정
        response.setContentType("application/json");  // 응답의 콘텐츠 타입을 JSON으로 설정
        response.getWriter().println("{\"error\": \"" + e.getMessage() + "\"}");  // 에러 메시지를 JSON 형식으로 출력
    }
}