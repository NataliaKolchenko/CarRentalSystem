package com.example.CarRentalSystem.service.auth;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class JwtService  {
    private final String secretKey = "5Hdo5+PxMJkLQ9Wo7WnYMR/gBzTfC5XrB3iNPvMlscY=";
    private final byte[] secretKeyBytes = Base64.getDecoder().decode(secretKey);

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode(secretKey))
                    .parseClaimsJws(token);

            Date expiration = claimsJws.getBody().getExpiration();
            return expiration != null && expiration.after(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            e.getMessage();
            return false;
        }
    }

    public String extractUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKeyBytes) // Passing a byte array
                .parseClaimsJws(token)
                .getBody();

        return claims.get("sub").toString() ;
    }

    public String extractUserRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKeyBytes)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role").toString();
    }
}
