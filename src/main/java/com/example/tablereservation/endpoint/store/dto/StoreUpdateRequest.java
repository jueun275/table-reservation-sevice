package com.example.tablereservation.endpoint.store.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class StoreUpdateRequest {
    private Long partnerId;
    private String name;
    private String address;
    private String description;
    private Double latitude;
    private Double longitude;
    private Integer unitTime;
    private Integer availableReservationCount;
    private LocalTime openTime;
    private LocalTime closeTime;

    @Builder

    public StoreUpdateRequest(Long partnerId,
                              String name,
                              String address,
                              String description,
                              Double latitude,
                              Double longitude,
                              Integer unitTime,
                              Integer availableReservationCount,
                              LocalTime openTime,
                              LocalTime closeTime) {

        this.partnerId = partnerId;
        this.name = name;
        this.address = address;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.unitTime = unitTime;
        this.availableReservationCount = availableReservationCount;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }
}
