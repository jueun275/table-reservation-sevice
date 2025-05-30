package com.example.tablereservation.endpoint.reservation;

import com.example.tablereservation.endpoint.reservation.dto.ReservationCreateRequest;
import com.example.tablereservation.endpoint.reservation.dto.ReservationResponse;
import com.example.tablereservation.endpoint.reservation.service.ReservationService;
import com.example.tablereservation.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody ReservationCreateRequest request,
                                       @LoginUser Long userId ) {
        Long id = reservationService.createReservation(request, userId);

        return ResponseEntity.ok(id);
    }

    @PreAuthorize("hasRole('PARTNER')")
    @PatchMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        reservationService.approveReservation(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('PARTNER')")
    @PatchMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id) {
        reservationService.rejectReservation(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/arrive")
    public ResponseEntity<Void> confirmArrival(@PathVariable Long id) {
        reservationService.confirmArrival(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 점주의 매장 예약 정보 조회 (특정 날짜 기준)
     */
    @PreAuthorize("hasRole('PARTNER')")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ReservationResponse>> getDayReservationsByStore(
        @PathVariable Long storeId,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<ReservationResponse> reservations = reservationService.getDayReservationList(storeId, date);
        return ResponseEntity.ok(reservations);
    }
}
