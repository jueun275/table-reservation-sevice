package com.example.tablereservation.domain.review;

import com.example.tablereservation.domain.common.BaseTimeEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store; // 매장

    private Integer rating;
    private String comment;

    @Builder
    public Review(User user, Store store, Integer rating, String comment) {
        this.user = user;
        this.store = store;
        this.rating = rating;
        this.comment = comment;
    }
}
