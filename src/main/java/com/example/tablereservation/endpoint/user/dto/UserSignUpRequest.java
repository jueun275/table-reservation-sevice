package com.example.tablereservation.endpoint.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignUpRequest {
    private String email;
    private String password;
    private String confirmPassword;
    private String name;
    private String phoneNumber;
    private String role;

    @Builder
    public UserSignUpRequest(String email,
                             String password,
                             String confirmPassword,
                             String name,
                             String phoneNumber,
                             String role) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
