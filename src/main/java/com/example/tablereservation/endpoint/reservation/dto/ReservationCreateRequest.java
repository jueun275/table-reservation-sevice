package com.example.tablereservation.endpoint.reservation.dto;

import com.example.tablereservation.domain.reservation.Reservation;
import com.example.tablereservation.domain.reservation.ReservationStatus;
import com.example.tablereservation.domain.store.Store;
import com.example.tablereservation.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ReservationCreateRequest {
    private Long userId;
    private Long storeId;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private String phoneNumber;

    @Builder
    public ReservationCreateRequest(Long storeId,
                                    Long userId,
                                    LocalDate reservationDate,
                                    LocalTime reservationTime,
                                    String phoneNumber) {
        this.storeId = storeId;
        this.userId = userId;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.phoneNumber = phoneNumber;
    }

    public Reservation toEntity(Store store, User user) {
        return Reservation.builder()
            .store(store)
            .user(user)
            .reservationDate(reservationDate)
            .reservationTime(this.reservationTime)
            .phoneNumber(this.phoneNumber)
            .status(ReservationStatus.REQUESTED)
            .build();
    }

}
