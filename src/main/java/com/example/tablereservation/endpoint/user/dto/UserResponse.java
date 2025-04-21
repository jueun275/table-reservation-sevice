package com.example.tablereservation.endpoint.user.dto;

import com.example.tablereservation.endpoint.reservation.dto.ReservationResponse;
import com.example.tablereservation.endpoint.review.dto.ReviewResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UserResponse {
    private String username;
    private String name;
    private String phone;
    private List<ReviewResponse> reviews;
    private List<ReservationResponse> reservations;

    public UserResponse(String username,
                        String name,
                        String phone,
                        List<ReviewResponse> reviews,
                        List<ReservationResponse> reservations) {
        this.username = username;
        this.name = name;
        this.phone = phone;
        if (!reviews.isEmpty()) this.reviews = reviews;
        if (!reservations.isEmpty()) this.reservations = reservations;
    }
}
