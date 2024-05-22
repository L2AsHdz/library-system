package com.ayd2.librarysystem.auth.service;

import com.ayd2.librarysystem.user.model.StudentModel;
import com.ayd2.librarysystem.user.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${token.validMinutes}")
    private Integer tokenValidTime;

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    public String generateToken(UserModel user) throws IOException {
        var claims = buildClaims(user);
        return createToken(claims, user.getUsername());
    }

    public Map<String, Object> buildClaims(UserModel user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("user", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("fullName", user.getFullName());
        claims.put("rol", user.getUserRole());

        if (user instanceof StudentModel student) {
            claims.put("academicNumber", student.getAcademicNumber());
            claims.put("career", student.getCareerModel().getName());
            claims.put("isSanctioned", student.getIsSanctioned());
        }
        return claims;
    }

    private String createToken(Map<String, Object> claims, String userName) throws IOException {
        return Jwts.builder()
                .setClaims(claims)
                .setId("w" + UUID.randomUUID().getLeastSignificantBits())
                .setSubject(userName)
                .setAudience("CUNOC")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plus(tokenValidTime, ChronoUnit.MINUTES)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
