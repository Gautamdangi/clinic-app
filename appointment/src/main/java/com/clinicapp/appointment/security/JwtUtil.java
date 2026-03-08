package com.clinicapp.appointment.security;

import com.clinicapp.appointment.model.Role;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration-ms}")
    private Long jwtExpirationMs;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());

    }
  public String generateToken(String username, Role role){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role",role.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String getUsernameFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
public Boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                    .parseClaimsJws(token);
            return true;

        }catch (JwtException| IllegalArgumentException e){
            return false;
        }

}
    public String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
