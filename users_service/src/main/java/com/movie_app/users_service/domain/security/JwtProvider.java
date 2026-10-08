package com.movie_app.users_service.domain.security;

import com.movie_app.users_service.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    private final String SECRET_KEY = "A_SECRTET_KEY_FOR_ALEXIA_MOVIE_PROJECT_THAT_IS_LONG";

    public String generateJwtToken(User user) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateJwtToken(String token) {
        try{
            getClaimsFromToken(token); //If it can read it without an error, they it's valid
            return true;
        }catch (Exception e) {
            return false;    //Expired of not valid
        }
    }
}
