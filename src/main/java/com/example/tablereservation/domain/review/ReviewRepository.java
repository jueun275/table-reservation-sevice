package com.example.tablereservation.domain.review;

import com.example.tablereservation.domain.reservation.Reservation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    boolean existsByReservation(Reservation reservation);

    void deleteAllInBatch();

    List<Review> findAllByUserId(Long userId);
}
