package com.example.tablereservation.endpoint.store.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Builder
    public StoreUpdateRequest(Long partnerId,
                              String name,
                              String address,
                              String description,
                              Double latitude,
                              Double longitude) {
        this.partnerId = partnerId;
        this.name = name;
        this.address = address;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
