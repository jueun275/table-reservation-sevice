package com.example.tablereservation.endpoint.user.service;

import com.example.tablereservation.domain.user.User;
import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.domain.user.type.Role;
import com.example.tablereservation.endpoint.user.dto.UserSignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("could not find user: " + username));
    }

    // 회원 가입
    @Transactional
    public Long signUp(UserSignUpRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Role role = Role.valueOf(request.getRole().toUpperCase());

        User user = User.builder()
            .username(request.getUsername())
            .password(encodedPassword)
            .name(request.getName())
            .phoneNumber(request.getPhoneNumber())
            .role(role)
            .build();

        return userRepository.save(user).getId();
    }


}
