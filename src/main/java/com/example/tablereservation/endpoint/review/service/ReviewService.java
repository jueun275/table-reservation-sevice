package com.example.tablereservation.endpoint.review.service;

import com.example.tablereservation.domain.reservation.Reservation;
import com.example.tablereservation.domain.reservation.ReservationRepository;
import com.example.tablereservation.domain.reservation.ReservationStatus;
import com.example.tablereservation.domain.review.Review;
import com.example.tablereservation.domain.review.ReviewRepository;
import com.example.tablereservation.domain.store.StoreRepository;
import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.endpoint.review.dto.ReviewCreateRequest;
import com.example.tablereservation.endpoint.review.dto.ReviewResponse;
import com.example.tablereservation.endpoint.review.dto.ReviewUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createReview(ReviewCreateRequest request, Long userId) {
        // 사용자, 상점, 예약 객체 찾기
        Reservation reservation = reservationRepository.findById(request.getReservationId())
            .orElseThrow(() -> new EntityNotFoundException("예약 정보를 찾을 수 없습니다."));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인 예약만 리뷰 작성 가능합니다.");
        }

        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            throw new IllegalStateException("예약이 완료된 경우에만 리뷰를 작성할 수 있습니다.");
        }

        if (reviewRepository.existsByReservation(reservation)) {
            throw new IllegalStateException("이미 작성된 리뷰입니다.");
        }

        return reviewRepository.save(request.toEntity(reservation)).getId();
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("리뷰가 존재하지 않습니다."));
        return ReviewResponse.fromEntity(review);
    }

    @Transactional
    public void updateReview(Long id, Long userId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("리뷰가 존재하지 않습니다"));

        if (!review.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 수정 가능");
        }
        review.update(request.getComment(), request.getRating());
    }

    @Transactional
    public void deleteReview(Long id, Long userId) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("리뷰가 존재하지 않습니다"));

        if (!validateDeleteReview(review, userId)) {
            throw new IllegalArgumentException("작성자나 매장 관리자만 삭제 가능합니다");
        }

        reviewRepository.delete(review);
    }

    private boolean validateDeleteReview(Review review, Long userId) {
        // 작성자 ID와 매장 소유자 ID를 가져와서 비교
        Long writerId = review.getUser().getId();
        Long storeOwnerId = review.getReservation().getStore().getPartner().getId();

        return userId.equals(writerId) || userId.equals(storeOwnerId);
    }

}
