package com.example.CarRentalSystem.infrastructure;

import com.example.CarRentalSystem.service.auth.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;  // Сервис для работы с JWT (создайте его, чтобы генерировать и парсить токены)

    public JwtAuthFilter(@Lazy JwtService jwtService) {
        this.jwtService = jwtService;
    }

    private final String secretKey = "5Hdo5+PxMJkLQ9Wo7WnYMR/gBzTfC5XrB3iNPvMlscY=";
    private final byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);

//    public String getTokenExtraction(HttpServletRequest request) {
//        // Извлекаем JWT-токен из заголовка Authorization
//        String authHeader = request.getHeader("Authorization");
//        String token = null;
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            token = authHeader.substring(7); // Убираем "Bearer " из строки
//        }
//        return token;
//    }

//    public Long extractUserIdFromToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(secretKeyBytes) // Передаем байтовый массив
//                .parseClaimsJws(token)
//                .getBody();
//
//
//        return Long.valueOf(claims.get("id").toString()) ;
//    }

//    public String extractUserRoleFromToken(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(secretKeyBytes)
//                .parseClaimsJws(token)
//                .getBody();
//        return (String) claims.get("role");
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwtToken = authHeader.substring(7);

            try {
                // Извлекаем userId и роль из токена
                Long userId = jwtService.extractUserIdFromToken(jwtToken);
                String role = jwtService.extractUserRoleFromToken(jwtToken);

                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Создаем "пользователя" с извлеченной ролью
                    UserDetails userDetails = new User(userId.toString(), "", List.of(new SimpleGrantedAuthority(role)));

                    // Проверяем валидность токена
                    if (jwtService.validateToken(jwtToken)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                // Логируем ошибки или обрабатываем исключения
                System.out.println("Ошибка при обработке JWT: " + e.getMessage());
            }
        }
        filterChain.doFilter(request, response);

    }
}


