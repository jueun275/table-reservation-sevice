package com.example.tablereservation.endpoint.store.dto;


import com.example.tablereservation.domain.store.Store;
import com.example.tablereservation.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class StoreCreateRequest {
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
    public StoreCreateRequest(String name,
                              String address,
                              String description,
                              Double latitude,
                              Double longitude,
                              Integer unitTime,
                              Integer availableReservationCount,
                              LocalTime openTime,
                              LocalTime closeTime) {
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

    public Store toEntity(User partner) {
        return Store.builder()
            .name(name)
            .address(address)
            .description(description)
            .latitude(latitude)
            .longitude(longitude)
            .unitTime(unitTime)
            .availableReservationCount(availableReservationCount)
            .openTime(openTime)
            .closeTime(closeTime)
            .partner(partner)
            .build();
    }
}
