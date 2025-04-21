package com.example.tablereservation.global.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginUserArgumentResolverTest {

    LoginUserArgumentResolver resolver = new LoginUserArgumentResolver();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void supportsParameter_True_IfAnnotatedAndLongType() throws Exception {
        Method method = this.getClass().getDeclaredMethod("mockMethod", Long.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        assertThat(resolver.supportsParameter(parameter)).isTrue();
    }

    @Test
    void resolveArgument_ReturnsUserIdFromAuthentication() throws Exception {
        // given
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Authentication auth = new UsernamePasswordAuthenticationToken(42L, null);
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);

        Method method = this.getClass().getDeclaredMethod("mockMethod", Long.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        // when
        Object resolved = resolver.resolveArgument(parameter, null, null, null);

        // then
        assertThat(resolved).isEqualTo(42L);
    }

    private void mockMethod(@LoginUser Long userId) {
    }
}