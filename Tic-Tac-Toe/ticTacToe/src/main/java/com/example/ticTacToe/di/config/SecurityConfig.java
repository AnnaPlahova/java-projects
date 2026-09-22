package com.example.ticTacToe.di.config;

import com.example.ticTacToe.web.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

// Конфигурация Spring Security
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthFilter authFilter;

    @Autowired
    public SecurityConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    // Отключает встроенную систему аутентификации, вместо нее подключается AuthFilter
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF для REST API
                .csrf(csrf -> csrf.disable())
                // Настройка прав доступа к URL
                .authorizeHttpRequests(authz -> authz
                        // Разрешить всем доступ к регистрации и входу
                        .requestMatchers("/api/auth/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/refresh/access").permitAll()
                        // Всё остальное — только для авторизованных
                        .anyRequest().authenticated()
                )
                // Добавить authFilter до BasicAuthenticationFilter
                .addFilterBefore(authFilter, BasicAuthenticationFilter.class);
        // Завершить сборку и вернуть цепочку фильтров
        return http.build();
    }
}

