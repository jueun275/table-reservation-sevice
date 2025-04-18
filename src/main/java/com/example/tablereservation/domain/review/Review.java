package com.example.tablereservation.domain.review;

import com.example.tablereservation.domain.common.BaseTimeEntity;
import com.example.tablereservation.domain.reservation.Reservation;
import com.example.tablereservation.domain.store.Store;
import com.example.tablereservation.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 리뷰 작성자

    @OneToOne(fetch = FetchType.LAZY)
    private Reservation reservation;

    private Integer rating;

    private String comment;

    @Builder
    public Review(Long id, User user, Reservation reservation, Integer rating, String comment) {
        this.id = id;
        this.user = user;
        this.reservation = reservation;
        this.rating = rating;
        this.comment = comment;
    }

    public void update(String comment, int rating) {
        this.comment = comment;
        this.rating = rating;
    }
}
