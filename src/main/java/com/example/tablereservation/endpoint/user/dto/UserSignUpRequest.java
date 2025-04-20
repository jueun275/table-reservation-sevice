package com.example.tablereservation.endpoint.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSignUpRequest {
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
    private String role;

    @Builder
    public UserSignUpRequest(String username,
                             String password,
                             String name,
                             String phoneNumber,
                             String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}
