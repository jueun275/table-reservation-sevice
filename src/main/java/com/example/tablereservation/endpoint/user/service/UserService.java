package com.example.tablereservation.endpoint.user.service;

import com.example.tablereservation.domain.user.User;
import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.domain.user.type.Role;
import com.example.tablereservation.endpoint.user.dto.LoginRequest;
import com.example.tablereservation.endpoint.user.dto.UserSignUpRequest;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // 회원 가입
    @Transactional
    public Long signUp(UserSignUpRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Role role = Role.valueOf(request.getRole().toUpperCase());

        User user = User.builder()
            .email(request.getEmail())
            .password(encodedPassword)
            .name(request.getName())
            .phoneNumber(request.getPhoneNumber())
            .role(role)
            .build();

        return userRepository.save(user).getId();
    }

}
