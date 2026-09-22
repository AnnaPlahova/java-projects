package com.example.ticTacToe.web.filter;

import com.example.ticTacToe.domain.model.JwtAuthentication;
import com.example.ticTacToe.domain.security.JwtProvider;
import com.example.ticTacToe.domain.security.JwtUtil;
import com.example.ticTacToe.domain.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.UUID;

// Перехватывает каждый HTTP-запрос до того, как он попадёт в контроллер
@Component
public class AuthFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthFilter(JwtProvider jwtProvider, JwtUtil jwtUtil) {
        this.jwtProvider = jwtProvider;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Разрешаем доступ к public endpointам без авторизации
        String path = httpRequest.getServletPath();
        if ("/api/auth/register".equals(path) ||
                "/api/auth/login".equals(path) ||
                "/api/auth/refresh/access".equals(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Для всех остальных эндпоинтов — проверяем Authorization
        // Извлекаем токен из заголовка Authorization
        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            try {
                // Валидируем токен
                if (jwtProvider.validateAccessToken(accessToken)) {
                    // Получаем claims
                    Claims claims = jwtProvider.getClaims(accessToken);

                    // Создаем JwtAuthentication
                    JwtAuthentication jwtAuthentication = jwtUtil.createAuthentication(claims);

                    // Устанавливаем авторизацию в SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);

                    chain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                System.err.println("JWT Authentication Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Если токен не валиден или отсутствует
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // код 401
        httpResponse.getWriter().write(httpResponse.getStatus() + " Unauthorized");
    }
}

