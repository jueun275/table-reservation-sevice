package com.example.tablereservation.global.security;

import com.example.tablereservation.domain.user.type.Role;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간
    private static final String KEY_ROLE = "role";

    @Value("${spring.jwt.secret}")
    private String secretKey;

    // 토큰 생성
    public String generateToken(Long userId, String username, Role role) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put(KEY_ROLE, "ROLE_" + role.name()); //ROLE_ 필수: @PreAuthorize("hasRole('PARTNER')") → 내부적으로 hasAuthority("ROLE_PARTNER") 검사
        claims.put("username", username);

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

    // Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String role = claims.get(KEY_ROLE, String.class);
        if (role == null) {
            throw new IllegalArgumentException("권한 정보가 없는 토큰입니다.");
        }

        Long userId = Long.valueOf(claims.getSubject());
        var authority = new SimpleGrantedAuthority(role);
        return new UsernamePasswordAuthenticationToken(userId, "", List.of(authority));
    }

    public Long getUserId(String token) {
        return Long.valueOf(parseClaims(token).getSubject()); // userId를 Long으로 반환
    }

    public String getUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) return false;

        try {
            Claims claims = parseClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Claims 파싱
    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 JWT 토큰입니다.", e);
        }
    }
}