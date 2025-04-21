package com.example.tablereservation.endpoint.user.service;

import com.example.tablereservation.domain.reservation.Reservation;
import com.example.tablereservation.domain.reservation.ReservationRepository;
import com.example.tablereservation.domain.review.Review;
import com.example.tablereservation.domain.review.ReviewRepository;
import com.example.tablereservation.domain.user.User;
import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.domain.user.type.Role;
import com.example.tablereservation.endpoint.reservation.dto.ReservationResponse;
import com.example.tablereservation.endpoint.review.dto.ReviewResponse;
import com.example.tablereservation.endpoint.user.dto.UserResponse;
import com.example.tablereservation.endpoint.user.dto.UserSignUpRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
