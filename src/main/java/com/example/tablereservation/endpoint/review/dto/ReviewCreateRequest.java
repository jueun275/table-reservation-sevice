package com.example.tablereservation.endpoint.review.dto;

import com.example.tablereservation.domain.reservation.Reservation;
import com.example.tablereservation.domain.review.Review;
import com.example.tablereservation.domain.store.Store;
import com.example.tablereservation.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewCreateRequest {
    private Long userId;
    private Long reservationId;
    private String comment;
    private int rating;

    @Builder
    public ReviewCreateRequest(Long userId, Long reservationId, String comment, int rating) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.comment = comment;
        this.rating = rating;
    }

    public Review toEntity(Reservation reservation) {
        return Review.builder()
            .user(reservation.getUser())
            .reservation(reservation)
            .comment(this.comment)
            .rating(this.rating)
            .build();
    }
}
