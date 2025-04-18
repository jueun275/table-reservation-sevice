package com.example.tablereservation.domain.review;

import com.example.tablereservation.domain.reservation.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepository extends CrudRepository<Review, Long> {
    boolean existsByReservation(Reservation reservation);

    void deleteAllInBatch();
}
