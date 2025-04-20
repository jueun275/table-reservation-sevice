package com.example.tablereservation.endpoint.auth;

import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.domain.user.type.Role;
import com.example.tablereservation.endpoint.auth.dto.LoginRequest;
import com.example.tablereservation.endpoint.user.dto.UserSignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    private static final Logger log = LoggerFactory.getLogger(AuthControllerTest.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @DisplayName("회원가입 후 로그인 통합테스트 - 성공")
    @Test
    void signupAndLogin_success() throws Exception {
        // given
        String email = "test@test.com";
        String password = "password123";

        UserSignUpRequest signUpRequest = UserSignUpRequest.builder()
            .username(email)
            .password(password)
            .name("테스트유저")
            .phoneNumber("010-1234-5678")
            .role("USER")
            .build();

        // 회원가입 요청
        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
            .andDo(print())
            .andExpect(status().isCreated());

        // 로그인 요청
        LoginRequest loginRequest = new LoginRequest(email, password);

        var loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists())
            .andReturn();

        // 토큰 추출
        String responseBody = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseBody).get("token").asText();

        // then
        assertThat(token).isNotBlank();
        
    }

}