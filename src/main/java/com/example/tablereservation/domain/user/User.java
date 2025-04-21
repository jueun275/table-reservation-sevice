package com.example.tablereservation.domain.user;

import com.example.tablereservation.domain.common.BaseTimeEntity;
import com.example.tablereservation.domain.user.type.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role; // USER, PARTNER

    @Builder
    public User(Long id, String name, String username, String password, String phoneNumber, Role role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
    }
}
