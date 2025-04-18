package com.example.tablereservation.endpoint.review.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequest {
    String comment;
    int rating;

    @Builder
    public ReviewUpdateRequest(String comment, int rating) {
        this.comment = comment;
        this.rating = rating;
    }
}
