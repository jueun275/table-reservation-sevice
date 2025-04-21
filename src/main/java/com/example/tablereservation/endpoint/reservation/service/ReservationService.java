package com.example.tablereservation.endpoint.reservation.service;

import com.example.tablereservation.domain.reservation.Reservation;
import com.example.tablereservation.domain.reservation.ReservationRepository;
import com.example.tablereservation.domain.reservation.ReservationStatus;
import com.example.tablereservation.domain.store.Store;
import com.example.tablereservation.domain.store.StoreRepository;
import com.example.tablereservation.domain.user.User;
import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.endpoint.reservation.dto.ReservationCreateRequest;
import com.example.tablereservation.endpoint.reservation.dto.ReservationResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    /**
     * 예약 생성
     */
    @Transactional
    public Long createReservation(ReservationCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new EntityNotFoundException("해당 유저가 존재하지 않습니다"));
        Store store = storeRepository.findById(request.getStoreId())
            .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

        validateReservationTime(request.getReservationTime(), store);

        validateReservationCount(
            store, request.getReservationDate(),
            request.getReservationTime(),
            store.getAvailableReservationCount());

        Reservation reservation = Reservation.builder()
            .user(user)
            .store(store)
            .reservationDate(request.getReservationDate())
            .reservationTime(request.getReservationTime())
            .status(ReservationStatus.REQUESTED)
            .build();

        return reservationRepository.save(reservation).getId();
    }

    /*
    * 요늘의 예약 타임 테이블 확인
    * */
    @Transactional(readOnly = true)
    public List<ReservationResponse> getDayReservationList(Long storeId, LocalDate reservationDate) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new EntityNotFoundException("가게를 찾을 수 없습니다."));

        List<Reservation> reservations = reservationRepository.findDayReservationsByStore(store, reservationDate);

        return reservations.stream()
            .map(ReservationResponse::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * 예약 승인
     */
    @Transactional
    public void approveReservation(Long reservationId) {
        Reservation reservation = getReservationOrThrow(reservationId);
        reservation.approve();
        reservationRepository.save(reservation);
    }

    /**
     * 예약 거절
     */
    @Transactional
    public void rejectReservation(Long reservationId) {
        Reservation reservation = getReservationOrThrow(reservationId);
        reservation.reject();
        reservationRepository.save(reservation);
    }

    /**
     * 예약 도착 확인처리 로직
     * 당일 예약 건이면서 예약 시간 10분 전 까지만 가능하도록 처리
     */
    @Transactional
    public void confirmArrival(Long reservationId) {
        Reservation reservation = getReservationOrThrow(reservationId);

        if (!reservation.getReservationDate().isEqual(LocalDate.now())) {
            throw new IllegalArgumentException("오늘 예약만 도착 확인할 수 있습니다.");
        }

        LocalTime latestArrivalTime = reservation.getReservationTime().minusMinutes(10);
        if (LocalTime.now().isAfter(latestArrivalTime)) {
            throw new IllegalArgumentException("예약 시간 10분 전까지만 도착 확인이 가능합니다.");
        }

        reservation.markArrived(LocalDateTime.now());
        reservationRepository.save(reservation);
    }

    /**
     * 예약 가능 여부 확인 (예약 인원 체크 )
     */
    private void validateReservationCount(Store store, LocalDate date, LocalTime time, int maxCount) {
        long approvedCount = reservationRepository.countApprovedReservations(store, date, time);
        if (approvedCount >= maxCount) {
            throw new IllegalStateException("해당 시간대는 이미 예약이 마감되었습니다.");
        }
    }

    /**
     * 예약 가능 여부 확인 ( 예약 시간 체크 )
     */
    private void validateReservationTime(LocalTime reservationTime, Store store) {
        int unit = store.getUnitTime(); // 예약시간 단위
        int minutes = reservationTime.getMinute();

        if (minutes % unit != 0) {
            throw new IllegalArgumentException("예약 시간은 " + unit + "분 단위로만 가능합니다.");
        }

        // 운영 시간 범위 확인
        LocalTime open = store.getOpenTime();
        LocalTime close = store.getCloseTime();

        if (reservationTime.isBefore(open) || !reservationTime.isBefore(close)) {
            throw new IllegalArgumentException(
                "예약 시간은 영업 시간(" + open + " ~ " + close + ") 내로만 가능합니다."
            );
        }
    }

    /**
     * 예약 단건 조회
     */
    private Reservation getReservationOrThrow(Long reservationId) {
        return reservationRepository.findById(reservationId)
            .orElseThrow(() -> new EntityNotFoundException("예약을 찾을 수 없습니다."));
    }

}
