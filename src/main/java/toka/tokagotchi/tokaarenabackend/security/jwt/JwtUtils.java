package toka.tokagotchi.tokaarenabackend.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
/**
 * JwtUtils: componente del modulo `security`.
 * Su responsabilidad principal es soportar funcionalidades del modulo.
 */


@Component
public class JwtUtils {
    @Value("${toka.jwt.secret}")
    private String jwtSecret;

    @Value("${toka.jwt.expirationMs}")
    private int jwtExpirationMs;

    // Modifica estos tres métodos en JwtUtils.java
    public String generateTokenFromUserId(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                // .getBytes() asegura que se use el tamaño correcto en bits
                .signWith(SignatureAlgorithm.HS512, jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8))
                .compact();
    }

    public String getUserIdFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8))
                    .parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}