package com.example.tablereservation.endpoint.auth.service;

import com.example.tablereservation.domain.user.User;
import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.endpoint.auth.dto.LoginRequest;
import com.example.tablereservation.endpoint.auth.dto.LoginResponse;
import com.example.tablereservation.global.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    //로그인 기능 - 정상적으로 검증이 되면 JWT 토근 발급
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = tokenProvider.generateToken(user.getUsername(), user.getRole());
        return LoginResponse.builder()
            .token(token)
            .username(user.getUsername())
            .role(user.getRole().name())
            .build();
    }
}
