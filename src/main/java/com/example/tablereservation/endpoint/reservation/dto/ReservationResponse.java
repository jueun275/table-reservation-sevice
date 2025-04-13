package com.example.tablereservation.endpoint.reservation.dto;

import com.example.tablereservation.domain.reservation.Reservation;
import com.example.tablereservation.domain.reservation.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class ReservationResponse {
    private Long id;
    private String storeName;
    private String userName;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private String phone;
    private ReservationStatus status;

    @Builder
    public ReservationResponse(Long id,
                               String storeName,
                               String userName,
                               LocalDate reservationDate,
                               LocalTime reservationTime,
                               String phone,
                               ReservationStatus status) {
        this.id = id;
        this.storeName = storeName;
        this.userName = userName;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.phone = phone;
        this.status = status;
    }

    public static ReservationResponse fromEntity(Reservation reservation) {
        return ReservationResponse.builder()
            .id(reservation.getId())
            .storeName(reservation.getStore().getName())
            .userName(reservation.getUser().getName())
            .reservationDate(reservation.getReservationDate())
            .reservationTime(reservation.getReservationTime())
            .phone(reservation.getPhoneNumber())
            .status(reservation.getStatus())
            .build();
    }
}
