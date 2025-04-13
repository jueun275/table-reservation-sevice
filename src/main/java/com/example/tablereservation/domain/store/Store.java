package com.example.tablereservation.domain.store;

import com.example.tablereservation.domain.common.BaseTimeEntity;
import com.example.tablereservation.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String description;

    private Double latitude;  // 위도

    private Double longitude; // 경도

    private Integer unitTime; // 예약 시간 단위

    private Integer availableReservationCount; // 예약 가능한 수
    
    private LocalTime  openTime;
    
    private LocalTime closeTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private User partner; // User(role = PARTNER)

    @Builder
    public Store(Long id,
                 String name,
                 String address,
                 String description,
                 User partner,
                 Double latitude,
                 Double longitude,
                 Integer unitTime,
                 Integer availableReservationCount,
                 LocalTime openTime,
                 LocalTime closeTime) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.partner = partner;
        this.latitude = latitude;
        this.longitude = longitude;
        this.unitTime = unitTime;
        this.availableReservationCount = availableReservationCount;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public void update(String name,
                       String address,
                       String description,
                       Integer unitTime,
                       Integer availableReservationCount,
                       LocalTime openTime,
                       LocalTime closeTime) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.unitTime = unitTime;
        this.availableReservationCount = availableReservationCount;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
