package com.example.tablereservation.endpoint.review;

import com.example.tablereservation.endpoint.review.dto.ReviewCreateRequest;
import com.example.tablereservation.endpoint.review.dto.ReviewResponse;
import com.example.tablereservation.endpoint.review.dto.ReviewUpdateRequest;
import com.example.tablereservation.endpoint.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<Long> createReview(@RequestBody ReviewCreateRequest request) {
        Long userId = 1L;
        return ResponseEntity.ok(reviewService.createReview(request, userId));
    }

    // 리뷰 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReview(id));
    }
    
    // 리뷰 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReview(
        @PathVariable Long id,
        @RequestBody ReviewUpdateRequest request) {

        Long userId = 1L;
        reviewService.updateReview(id, userId, request);
        return ResponseEntity.ok().build();
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        Long userId = 1L;
        reviewService.deleteReview(id, userId);
        return ResponseEntity.ok().build();
    }
}
