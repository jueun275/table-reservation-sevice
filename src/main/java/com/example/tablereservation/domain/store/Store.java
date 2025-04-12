package com.example.tablereservation.domain.store;

import com.example.tablereservation.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String description;

    private Double latitude;  // 위도

    private Double longitude; // 경도

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private User partner; // User(role = PARTNER)

    @Builder
    public Store(Long id, String name,
                 String address,
                 String description,
                 User partner,
                 Double latitude,
                 Double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.partner = partner;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void update(String name, String address, String description) {
        this.name = name;
        this.address = address;
        this.description = description;
    }

}
