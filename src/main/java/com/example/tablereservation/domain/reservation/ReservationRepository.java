package com.example.tablereservation.domain.reservation;

import com.example.tablereservation.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 특정 매장의 특정 날짜+시간, 승인된 예약 수 확인 -> 예약 가능 여부 유효성 검사
    /*
    *
    * 여기서 질문이 있습니다 ('REQUESTED', 'APPROVED')로 했는데 이걸 'APPROVED'로만 해놓고
    * 점주가 예약을 승인하면서 처리하게 해야하는지 일단 승인되지 않은 예약까지 다 카운트 해야할지
    * 어느 로직이 더 맞는 방법인지 모르겠습니다.
    *
    * "예약가능" 이란 판단을 어떻게 해야할지 고민이 많았는데 피드백에서 이부분에 대해서 남겨주시면 감사하겠습니다!
    *
    *
    * */
    @Query("SELECT COUNT(r) FROM Reservation r " +
        "WHERE r.store = :store " +
        "AND r.reservationDate = :reservationDate " +
        "AND r.reservationTime = :reservationTime " +
        "AND r.status IN ('REQUESTED', 'APPROVED')")
    long countApprovedReservations(
        @Param("store") Store store,
        @Param("reservationDate") LocalDate reservationDate,
        @Param("reservationTime") LocalTime reservationTime
    );

    // 매장의 하루 예약 전체 가져오기(점주 예약 정보 확인)
    @Query("SELECT r FROM Reservation r " +
        "WHERE r.store = :store " +
        "AND r.reservationDate = :reservationDate " +
        "ORDER BY r.reservationTime ASC")
    List<Reservation> findDayReservationsByStore(
        @Param("store") Store store,
        @Param("reservationDate") LocalDate reservationDate
    );

}
