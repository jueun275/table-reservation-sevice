package com.example.tablereservation.endpoint.store.dto;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
public class StoreDetailResponse {
    private Long id;
    private String name;
    private String address;
    private String description;
    private Double latitude;
    private Double longitude;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;

    private List<LocalTime> availableReservationTimes;

}
