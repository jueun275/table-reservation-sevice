package com.example.tablereservation.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.retrieveToken(request);
        try {
            if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                // 토큰 유효성 검증
                Authentication authentication = this.tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("[{}] -> {}", this.tokenProvider.getUsername(token), request.getRequestURI());
            }
        } catch (Exception e) {
            log.error("JWT 인증 실패: {}", e.getMessage());
            SecurityContextHolder.clearContext(); // 예외 발생 시 보안 컨텍스트 초기화
        }
        filterChain.doFilter(request, response);
    }

    private String retrieveToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER);

        if (ObjectUtils.isEmpty(token) || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }

        return token.substring(TOKEN_PREFIX.length());
    }
}
