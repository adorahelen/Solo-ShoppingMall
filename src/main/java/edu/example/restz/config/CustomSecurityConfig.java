package edu.example.restz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class CustomSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login ->login.disable()) // No front = login NO
                .logout(logout ->logout.disable()) // 로그아웃 안씀
                .csrf(csrf ->csrf.disable()) // 세션 관리 단위인 csrf안씀
                .sessionManagement(sess ->sess.sessionCreationPolicy(SessionCreationPolicy.NEVER)); // 세션 사용 안함
                return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
