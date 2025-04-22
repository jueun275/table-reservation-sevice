package com.example.tablereservation.endpoint.auth.service;

import com.example.tablereservation.domain.user.User;
import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.domain.user.type.Role;
import com.example.tablereservation.endpoint.auth.dto.LoginRequest;
import com.example.tablereservation.endpoint.auth.dto.LoginResponse;
import com.example.tablereservation.global.security.TokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;


    @Test
    void login_success() {
        Long userId = 1L;
        String username = "test@test.com";
        String rawPassword = "1234";
        String encodedPassword = "암호화된1234";

        User user = User.builder()
            .id(userId)
            .username(username)
            .password(encodedPassword)
            .role(Role.USER)
            .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
        when(tokenProvider.generateToken(userId, username, Role.USER)).thenReturn("JWT-TOKEN");

        LoginResponse response = authService.login(new LoginRequest(username, rawPassword));

        assertEquals("JWT-TOKEN", response.getToken());
    }
}