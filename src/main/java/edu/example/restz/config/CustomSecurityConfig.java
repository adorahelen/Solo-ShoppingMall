package edu.example.restz.config;

import edu.example.restz.security.filter.JWTCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration  // 이 클래스는 Spring의 설정 파일임을 나타냅니다.
@EnableMethodSecurity  // 메소드 수준의 보안 활성화 (예: @PreAuthorize 사용 가능)
public class CustomSecurityConfig {

    private JWTCheckFilter jwtCheckFilter; // JWT 체크 필터를 위한 필드

    @Autowired  // 의존성 주입을 위해 사용 (Setter Injection)
    public void setJwtCheckFilter(JWTCheckFilter jwtCheckFilter) {
        this.jwtCheckFilter = jwtCheckFilter;
    }
// ======= 1. 보안 필터 체인을 정의,  세션을 사용하지 않고, JWT를 사용해 인증 처리
    // Spring Security의 핵심 필터 체인을 정의하는 메서드
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Spring Security 기본 로그인, 로그아웃, CSRF 보호 비활성화
        http.formLogin(login -> login.disable()) // 폼 로그인을 비활성화합니다.
                .logout(logout -> logout.disable()) // 로그아웃 기능을 비활성화합니다.
                .csrf(csrf -> csrf.disable()) // CSRF 보호를 비활성화합니다.
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.NEVER)); // 세션을 사용하지 않도록 설정합니다.

        // JWT 필터 추가: UsernamePasswordAuthenticationFilter 앞에 추가하여 JWT를 통해 인증을 처리합니다.
        http.addFilterBefore(jwtCheckFilter, UsernamePasswordAuthenticationFilter.class);

        // CORS 설정을 활성화하고, 설정된 CORS 구성원을 사용하도록 설정합니다.
        http.cors(cors -> {cors.configurationSource(corsConfigurationSource());});

        return http.build(); // SecurityFilterChain 객체를 반환하여 Spring Security에 적용합니다.
    }
// ======== 2. 외부 도메인에서의 요청을 허용할 수 있도록 CORS 설정 정의
    // localhost8070 -> localhost8080 && React front 5000 -> Spring back 8080
    // CORS 설정 관련 메서드: 특정 도메인 또는 URL 패턴에서의 요청을 허용하기 위해 CORS(Cross-Origin Resource Sharing) 설정을 정의합니다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 허용된 출처 패턴을 설정 (여기서는 모든 출처를 허용)
        corsConfig.setAllowedOriginPatterns(List.of("*"));

        // 허용된 HTTP 메서드를 설정
        corsConfig.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE"));

        // 허용된 요청 헤더를 설정
        corsConfig.setAllowedHeaders(
                List.of("Authorization",
                        "Content-Type",
                        "Content-Control")
        );

        // 자격 증명(쿠키, 인증 정보 등)을 허용할지 여부를 설정
        corsConfig.setAllowCredentials(true);

        // 특정 URL 패턴에 대해 위에서 설정한 CORS 설정을 적용합니다.
        UrlBasedCorsConfigurationSource corSource = new UrlBasedCorsConfigurationSource();

        corSource.registerCorsConfiguration("/**", corsConfig); // 모든 경로에 CORS 설정을 적용합니다.

        return corSource; // CORS 설정 소스를 반환합니다.
    }
// ======= 3. 비밀번호를 해시화
    // 비밀번호 인코더를 Bean으로 등록하여 Spring Security에서 사용할 수 있도록 합니다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호를 해시화할 때 사용할 BCryptPasswordEncoder를 반환합니다.
    }
}