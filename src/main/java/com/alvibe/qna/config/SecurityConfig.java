package com.alvibe.qna.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("------- Security Filter Chain 로드 중 ---------");

        http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/questions/**", "/member/signup", "/member/login", "/css/**", "/js/**", "/images/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/member/login")             // 커스텀 로그인 페이지 경로 설정
                        .loginProcessingUrl("/member/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/index", true) // 로그인 성공 시 이동할 기본 경로
                        .permitAll()                     // 로그인 페이지 접근은 모두에게 허용
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/member/login")      // 로그아웃 성공 후 리다이렉트 경로
                        .permitAll()                     // 로그아웃 기능은 모두에게 허용
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
}
