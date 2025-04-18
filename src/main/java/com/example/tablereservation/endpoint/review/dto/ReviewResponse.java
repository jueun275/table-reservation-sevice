package com.example.tablereservation.endpoint.review.dto;

import com.example.tablereservation.domain.review.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponse {
    private Long id;
    private String storeName;
    private String userName;
    private String comment;
    private int rating;
    private String reservationId; // 리뷰에서 예약 내용을 알 필요가 있을까..?

    @Builder
    public ReviewResponse(Long id, String storeName, String userName, String comment, int rating, String reservationId) {
        this.id = id;
        this.storeName = storeName;
        this.userName = userName;
        this.comment = comment;
        this.rating = rating;
        this.reservationId = reservationId;
    }

    public static ReviewResponse fromEntity(Review review) {
        return ReviewResponse.builder()
            .id(review.getId())
            .userName(review.getUser().getName())
            .comment(review.getComment())
            .rating(review.getRating())
            .build();
    }
}
