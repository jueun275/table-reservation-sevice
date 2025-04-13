package com.example.tablereservation.endpoint.store.dto;

import com.example.tablereservation.domain.store.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreResponse {
    private Long id;
    private Long partnerId;
    private String name;
    private String address;
    private String description;


    @Builder
    public StoreResponse(Long id,
                         Long partnerId,
                         String name,
                         String address,
                         String description) {
        this.id = id;
        this.partnerId = partnerId;
        this.name = name;
        this.address = address;
        this.description = description;
    }

    public static StoreResponse from(Store store) {
        return StoreResponse.builder()
            .id(store.getId())
            .partnerId(store.getPartner().getId())
            .name(store.getName())
            .address(store.getAddress())
            .description(store.getDescription())
            .build();
    }
}
