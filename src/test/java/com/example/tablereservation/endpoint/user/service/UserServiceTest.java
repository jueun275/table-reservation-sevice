package com.example.tablereservation.endpoint.user.service;

import com.example.tablereservation.domain.user.User;
import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.domain.user.type.Role;
import com.example.tablereservation.endpoint.user.dto.UserSignUpRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void signin_success() {
        // given
        UserSignUpRequest request = UserSignUpRequest.builder()
            .name("테스트유저")
            .password("1234")
            .username("test@test.com")
            .role(Role.USER.name())
            .build();

        when(userRepository.existsByUsername("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("1234")).thenReturn("1234암호화");

        User savedUser = User.builder()
            .id(1L)
            .username("test@test.com")
            .password("1234암호화")
            .name("테스트유저")
            .role(Role.USER)
            .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        Long result = userService.signUp(request);

        // then
        assertEquals(1L, result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signIn_fail_username_already_exit() {
        //given
        UserSignUpRequest request = UserSignUpRequest.builder()
            .name("테스트유저")
            .password("1234암호화")
            .username("test@test.com")
            .role(Role.USER.name())
            .build();

        when(userRepository.existsByUsername("test@test.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> userService.signUp(request));
    }
}