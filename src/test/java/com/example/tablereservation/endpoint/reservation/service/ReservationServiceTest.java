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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationService reservationService;

    @DisplayName("예약 생성 성공")
    @Test
    void createReservation_success() {
        // given
        User user = User.builder()
            .id(1L)
            .name("Test User")
            .build();

        Store store = Store.builder()
            .id(1L)
            .name("Test Store")
            .unitTime(30)
            .openTime(LocalTime.of(9, 0))  // 09:00
            .closeTime(LocalTime.of(18, 0))  // 18:00
            .availableReservationCount(5)
            .build();

        ReservationCreateRequest request = ReservationCreateRequest.builder()
            .userId(1L)
            .storeId(1L)
            .reservationDate(LocalDate.now())
            .reservationTime(LocalTime.of(14, 0))
            .phoneNumber("010-1234-5678")
            .build();

        Reservation savedReservation = Reservation.builder()
            .id(1L)
            .user(user)
            .store(store)
            .reservationDate(request.getReservationDate())
            .reservationTime(request.getReservationTime())
            .status(ReservationStatus.REQUESTED)
            .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(reservationRepository.save(any(Reservation.class))).willReturn(savedReservation);

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);

        // when
        Long reservationId = reservationService.createReservation(request);

        // then
        verify(reservationRepository).save(captor.capture());
        Reservation capturedReservation = captor.getValue();

        assertThat(reservationId).isEqualTo(1L);
        assertThat(capturedReservation.getReservationDate()).isEqualTo(request.getReservationDate());
        assertThat(capturedReservation.getReservationTime()).isEqualTo(request.getReservationTime());
        assertThat(capturedReservation.getUser().getId()).isEqualTo(1L);
        assertThat(capturedReservation.getStore().getId()).isEqualTo(1L);
    }

    @DisplayName("예약 생성 실패 - 유저가 존재하지 않음")
    @Test
    void createReservation_fail_userNotFound() {
        // given
        ReservationCreateRequest request = ReservationCreateRequest.builder()
            .userId(1L)
            .storeId(1L)
            .reservationDate(LocalDate.now())
            .reservationTime(LocalTime.of(14, 0))
            .phoneNumber("010-1234-5678")
            .build();

        given(userRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("해당 유저가 존재하지 않습니다");

        verify(reservationRepository, never()).save(any());
    }

    @DisplayName("예약 생성 실패 - 매장이 존재하지 않음")
    @Test
    void createReservation_fail_storeNotFound() {
        // given
        User user = User.builder().id(1L).build();
        ReservationCreateRequest request = ReservationCreateRequest.builder()
            .userId(1L)
            .storeId(1L)
            .reservationDate(LocalDate.now())
            .reservationTime(LocalTime.of(14, 0))
            .phoneNumber("010-1234-5678")
            .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(storeRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(request))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("가게를 찾을 수 없습니다.");

        verify(reservationRepository, never()).save(any());
    }

    @DisplayName("예약 승인 성공")
    @Test
    void approveReservation_success() {
        // given
        Reservation reservation = Reservation.builder()
            .id(1L)
            .status(ReservationStatus.REQUESTED)
            .build();

        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));

        // when
        reservationService.approveReservation(1L);

        // then
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.APPROVED);
        verify(reservationRepository).save(reservation);
    }

    @DisplayName("예약 승인 실패 - 예약을 찾을 수 없음")
    @Test
    void approveReservation_fail_reservationNotFound() {
        // given
        given(reservationRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.approveReservation(1L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("예약을 찾을 수 없습니다.");

        verify(reservationRepository, never()).save(any());
    }

    @DisplayName("예약 거절 성공")
    @Test
    void rejectReservation_success() {
        // given
        Reservation reservation = Reservation.builder()
            .id(1L)
            .status(ReservationStatus.REQUESTED)
            .build();

        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));

        // when
        reservationService.rejectReservation(1L);

        // then
        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.REJECTED);
        verify(reservationRepository).save(reservation);
    }

 // 내부에서 Now()를 사용하기 때문에 그때 그때 값의 변경이 필요
//    @DisplayName("예약 도착 확인 성공")
//    @Test
//    void confirmArrival_success() {
//        // given
//        Reservation reservation = Reservation.builder()
//            .id(1L)
//            .reservationDate(LocalDate.now())
//            .reservationTime(LocalTime.of(17, 0))
//            .status(ReservationStatus.APPROVED)
//            .build();
//
//        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));
//
//        // when
//        reservationService.confirmArrival(1L);
//
//        // then
//        assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.COMPLETED);
//        verify(reservationRepository).save(reservation);
//    }

    @DisplayName("예약 도착 확인 실패 - 오늘 예약만 도착 확인 가능")
    @Test
    void confirmArrival_fail_notToday() {
        // given
        Reservation reservation = Reservation.builder()
            .id(1L)
            .reservationDate(LocalDate.now().minusDays(1))
            .reservationTime(LocalTime.of(14, 0))
            .status(ReservationStatus.APPROVED)
            .build();

        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.confirmArrival(1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("오늘 예약만 도착 확인할 수 있습니다.");
    }

    @DisplayName("예약 도착 확인 실패 - 예약 시간 10분 전까지만 가능")
    @Test
    void confirmArrival_fail_tooLate() {
        // given
        Reservation reservation = Reservation.builder()
            .id(1L)
            .reservationDate(LocalDate.now())
            .reservationTime(LocalTime.of(14, 0))
            .status(ReservationStatus.APPROVED)
            .build();

        given(reservationRepository.findById(1L)).willReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> reservationService.confirmArrival(1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("예약 시간 10분 전까지만 도착 확인이 가능합니다.");
    }

    @DisplayName("매장 예약 상태 조회 성공")
    @Test
    void getStoreReservationStatus_success() {
        // given
        User user = User.builder()
            .id(1L)
            .name("테스트 유저")  // 유저의 이름을 지정
            .build();

        User partner = User.builder()
            .id(2L)
            .build();

        Store store = Store.builder()
            .id(1L)
            .partner(partner)
            .name("테스트 매장")
            .availableReservationCount(5)
            .build();

        // user를 reservation1에 추가
        Reservation reservation1 = Reservation.builder()
            .id(1L)
            .user(user)  // user 필드를 정확히 할당
            .store(store)
            .reservationDate(LocalDate.now())
            .reservationTime(LocalTime.of(14, 0))
            .status(ReservationStatus.APPROVED)
            .build();

        // reservation2에서 user를 설정해주기
        Reservation reservation2 = Reservation.builder()
            .id(2L)
            .user(user)  // reservation2에 대해서도 user 설정
            .store(store)
            .reservationDate(LocalDate.now())
            .reservationTime(LocalTime.of(15, 0))
            .status(ReservationStatus.REQUESTED)
            .build();

        // mock repository behavior
        given(storeRepository.findById(1L)).willReturn(Optional.of(store));
        given(reservationRepository.findDayReservationsByStore(store, LocalDate.now()))
            .willReturn(List.of(reservation1, reservation2));

        // when
        List<ReservationResponse> responses = reservationService.getDayReservationList(1L, LocalDate.now());

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getId()).isEqualTo(1L);
        assertThat(responses.get(1).getId()).isEqualTo(2L);
    }
}