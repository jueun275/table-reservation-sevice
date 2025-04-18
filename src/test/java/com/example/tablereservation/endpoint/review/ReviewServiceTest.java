package com.example.tablereservation.endpoint.review;

import com.example.tablereservation.domain.reservation.Reservation;
import com.example.tablereservation.domain.reservation.ReservationRepository;
import com.example.tablereservation.domain.reservation.ReservationStatus;
import com.example.tablereservation.domain.review.Review;
import com.example.tablereservation.domain.review.ReviewRepository;
import com.example.tablereservation.domain.store.Store;
import com.example.tablereservation.domain.store.StoreRepository;
import com.example.tablereservation.domain.user.User;
import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.domain.user.type.Role;
import com.example.tablereservation.endpoint.review.dto.ReviewCreateRequest;
import com.example.tablereservation.endpoint.review.dto.ReviewResponse;
import com.example.tablereservation.endpoint.review.dto.ReviewUpdateRequest;
import com.example.tablereservation.endpoint.review.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private User partner;
    private Store store;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
            .name("유저")
            .email("user@test.com")
            .password("1234")
            .phoneNumber("010-1111-2222")
            .role(Role.USER)
            .build());

        partner = userRepository.save(User.builder()
            .name("파트너")
            .email("partner@test.com")
            .password("5678")
            .phoneNumber("010-9999-8888")
            .role(Role.PARTNER)
            .build());

        store = storeRepository.save(Store.builder()
            .partner(partner)
            .name("테스트 매장")
            .address("서울시 테스트구")
            .latitude(37.123)
            .longitude(127.123)
            .unitTime(30)
            .availableReservationCount(10)
            .partner(partner)
            .build());

        reservation = reservationRepository.save(Reservation.builder()
            .user(user)
            .store(store)
            .reservationDate(LocalDate.now())
            .reservationTime(LocalTime.of(12, 30))
            .status(ReservationStatus.COMPLETED)
            .build());
    }

    @Test
    @DisplayName("리뷰 생성 성공")
    void createReview_success() {
        // given
        ReviewCreateRequest request = ReviewCreateRequest.builder()
            .reservationId(reservation.getId())
            .comment("맛있어요!")
            .rating(5)
            .build();


        // when
        Long reviewId = reviewService.createReview(request, user.getId());

        // then
        assertThat(reviewRepository.findById(reviewId)).isPresent();
    }

    @Test
    @DisplayName("리뷰 조회 성공")
    void getReview_success() {
        // given
        ReviewCreateRequest createRequest = ReviewCreateRequest.builder()
            .reservationId(reservation.getId())
            .comment("좋아요!")
            .rating(4)
            .build();

        Long reviewId = reviewService.createReview(createRequest, user.getId());

        // when
        ReviewResponse response = reviewService.getReview(reviewId);

        // then
        assertThat(response.getRating()).isEqualTo(4);
        assertThat(response.getComment()).isEqualTo("좋아요!");
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    void updateReview_success() {
        // given
        ReviewCreateRequest createRequest = ReviewCreateRequest.builder()
            .reservationId(reservation.getId())
            .comment("그냥그래요")
            .rating(3)
            .build();

        Long reviewId = reviewService.createReview(createRequest, user.getId());

        ReviewUpdateRequest updateRequest = new ReviewUpdateRequest("괜찮아요", 4);

        // when
        reviewService.updateReview(reviewId, user.getId(), updateRequest);

        // then
        ReviewResponse updated = reviewService.getReview(reviewId);
        assertThat(updated.getComment()).isEqualTo("괜찮아요");
        assertThat(updated.getRating()).isEqualTo(4);
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void deleteReview_success() {
        // given
        ReviewCreateRequest createRequest = ReviewCreateRequest.builder()
            .reservationId(reservation.getId())
            .userId(user.getId())
            .comment("맛있어요!")
            .rating(5)
            .build();

        Long reviewId = reviewService.createReview(createRequest, user.getId());

        // when
        reviewService.deleteReview(reviewId, user.getId());

        // then
        assertThatThrownBy(() -> reviewService.getReview(reviewId))
            .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("리뷰 생성 실패 - 이미 리뷰가 존재함")
    void createReview_fail_alreadyExists() {
        // given
        ReviewCreateRequest createRequest = ReviewCreateRequest.builder()
            .reservationId(reservation.getId())
            .comment("맛있어요!")
            .rating(5)
            .build();

        Long reviewId = reviewService.createReview(createRequest, user.getId());

        // when & then
        assertThatThrownBy(() -> reviewService.createReview(createRequest, user.getId()))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("이미 작성된 리뷰입니다.");
    }

    @Test
    @DisplayName("리뷰 수정 실패 - 작성자 아님")
    void updateReview_fail_notAuthor() {
        ReviewCreateRequest createRequest = ReviewCreateRequest.builder()
            .reservationId(reservation.getId())
            .comment("맛있어요!")
            .rating(5)
            .build();

        // given
        Long reviewId = reviewService.createReview(createRequest, user.getId());

        User otherUser = userRepository.save(User.builder()
            .name("다른유저")
            .email("other@example.com")
            .password("pass")
            .phoneNumber("010-2222-3333")
            .role(Role.USER)
            .build());

        // when & then
        assertThatThrownBy(() -> reviewService.updateReview(reviewId, otherUser.getId(), new ReviewUpdateRequest("수정불가", 1)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("작성자만 수정 가능");
    }
}