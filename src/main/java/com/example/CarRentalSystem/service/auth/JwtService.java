package com.example.CarRentalSystem.service.auth;

import com.example.CarRentalSystem.exception.ValidateTokenException;
import com.example.CarRentalSystem.infrastructure.JwtAuthFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class JwtService  {

    private final String secretKey = "5Hdo5+PxMJkLQ9Wo7WnYMR/gBzTfC5XrB3iNPvMlscY=";
    private final byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);

    public boolean validateToken(String token) {
        try {
            // Парсим и проверяем токен с использованием секретного ключа
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode(secretKey))
                    .parseClaimsJws(token);

            // Проверяем, что токен не просрочен
            Date expiration = claimsJws.getBody().getExpiration();
            return expiration != null && expiration.after(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            // Если токен недействителен или подпись неверна
            e.getMessage();
            return false;
        }
    }

//    public void validateRequestToken(HttpServletRequest request) {
//        String token = jwtAuthFilter.getTokenExtraction(request);
//        boolean isValid = validateToken(token);
//        if (!isValid) {
//            throw new ValidateTokenException("Unauthorized: Invalid or missing token");
//        }
//    }

    public Long extractUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKeyBytes) // Передаем байтовый массив
                .parseClaimsJws(token)
                .getBody();


        return Long.valueOf(claims.get("id").toString()) ;
    }

    public String extractUserRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKeyBytes)
                .parseClaimsJws(token)
                .getBody();
        return (String) claims.get("role");
    }

}
