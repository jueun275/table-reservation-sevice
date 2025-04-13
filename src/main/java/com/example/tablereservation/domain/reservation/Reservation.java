package com.example.tablereservation.domain.reservation;

import com.example.tablereservation.domain.common.BaseTimeEntity;
import com.example.tablereservation.domain.store.Store;
import com.example.tablereservation.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
public class Reservation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user; // 예약자

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store; // 예약된 매장

    private LocalDate reservationDate;      // 예약 날짜

    private LocalTime reservationTime;

    private LocalDateTime arrivalTime;

    private String phoneNumber; // 전화번호

    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // 예약 상태

    private boolean isVisited = false; // 방문 여부

    @Builder
    public Reservation(Long id,
                        User user,
                       Store store,
                       LocalDate reservationDate,
                       LocalTime reservationTime,
                       String phoneNumber,
                       ReservationStatus status) {
        this.id = id;
        this.user = user;
        this.store = store;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public void approve() {
        this.status = ReservationStatus.APPROVED;
    }

    public void reject() {
        this.status = ReservationStatus.REJECTED;
    }

    public void markArrived(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
        this.isVisited = true;
        this.status = ReservationStatus.COMPLETED;
    }


}
