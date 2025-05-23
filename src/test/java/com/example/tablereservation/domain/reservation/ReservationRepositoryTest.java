package com.example.tablereservation.domain.reservation;

import com.example.tablereservation.domain.store.Store;
import com.example.tablereservation.domain.store.StoreRepository;
import com.example.tablereservation.domain.user.User;
import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.domain.user.type.Role;
import com.example.tablereservation.global.config.JpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)  // Auditing 활성화
@Transactional
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StoreRepository storeRepository;

    private User user;
    private User partnerUser;
    private Store store;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAllInBatch();
        storeRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();

        user = User.builder()
            .name("유저")
            .username("hong@example.com")
            .password("password")
            .phoneNumber("010-1234-5678")
            .role(Role.USER)
            .build();
        userRepository.save(user);

        partnerUser = User.builder()
            .name("파트너")
            .username("partner@example.com")
            .password("password")
            .phoneNumber("010-9876-5432")
            .role(Role.PARTNER)
            .build();
        userRepository.save(partnerUser);

        store = Store.builder()
            .partner(partnerUser)
            .name("Test Store")
            .address("1234 Test Street")
            .latitude(37.7749)
            .longitude(-122.4194)
            .unitTime(30)
            .availableReservationCount(10)
            .build();
        storeRepository.save(store);

        reservation = Reservation.builder()
            .user(user)
            .store(store)
            .reservationDate(LocalDate.now())
            .reservationTime(LocalTime.of(14, 0))
            .status(ReservationStatus.REQUESTED)
            .build();
        reservationRepository.save(reservation);
    }
    @Test
    void testCountApprovedReservations() {
        LocalDate reservationDate = LocalDate.of(2025, 4, 13);
        LocalTime reservationTime = LocalTime.of(18, 0);

        // 예약 1개 추가(상태: APPROVED)
        Reservation reservation1 = Reservation.builder()
            .user(user)
            .store(store)
            .reservationDate(reservationDate)
            .reservationTime(reservationTime)
            .status(ReservationStatus.APPROVED)
            .phoneNumber("010-1234-5678")
            .build();
        reservationRepository.save(reservation1);

        // 예약 2개 추가(상태: REQUESTED)
        Reservation reservation2 = Reservation.builder()
            .user(user)
            .store(store)
            .reservationDate(reservationDate)
            .reservationTime(reservationTime)
            .status(ReservationStatus.REQUESTED)
            .phoneNumber("010-9876-5432")
            .build();
        reservationRepository.save(reservation2);

        //예약 건 수 확인
        long approvedReservationsCount = reservationRepository.countApprovedReservations(store, reservationDate, reservationTime);

        assertThat(approvedReservationsCount).isEqualTo(2);  // 두개의 예약 신청
    }
}