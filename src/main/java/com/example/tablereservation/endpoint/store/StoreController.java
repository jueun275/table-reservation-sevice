package com.example.tablereservation.endpoint.store;

import com.example.tablereservation.endpoint.reservation.dto.ReservationResponse;
import com.example.tablereservation.endpoint.store.dto.StoreCreateRequest;
import com.example.tablereservation.endpoint.store.dto.StoreResponse;
import com.example.tablereservation.endpoint.store.dto.StoreUpdateRequest;
import com.example.tablereservation.endpoint.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 매장 등록 - 파트너만 가능
    @PreAuthorize("hasRole('PARTNER')")
    @PostMapping
    public ResponseEntity<Long> createStore(@RequestBody StoreCreateRequest request) {
        Long storeId = storeService.createStore(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(storeId);
    }

    // 매장 수정 - 파트너만 가능(등록이 파트너만 가능하니, 수정 삭제도 같은 권한 설정)
    @PreAuthorize("hasRole('PARTNER')")
    @PutMapping("/{storeId}")
    public ResponseEntity<Void> updateStore(@PathVariable Long storeId,
                                            @RequestBody StoreUpdateRequest request) {
        storeService.updateStore(storeId, request);
        return ResponseEntity.noContent().build();
    }

    // 매장 삭제 - 파트너만 가능(등록이 파트너만 가능하니, 수정 삭제도 같은 권한 설정)
    @PreAuthorize("hasRole('PARTNER')")
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long storeId,
                                            @RequestParam Long userId) {
        storeService.deleteStore(storeId, userId);
        return ResponseEntity.noContent().build(); // 204
    }

    // 매장 단일 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponse> getStore(@PathVariable Long storeId) {
        StoreResponse store = storeService.getStoreById(storeId);
        return ResponseEntity.ok(store);
    }

    // 매장 리스트 조회
    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        List<StoreResponse> stores = storeService.getStoreList();
        return ResponseEntity.ok(stores);
    }


/*
* store의 Detail정 보에서는 예약가능 시간을 데이터를 보내줘서
* 프런트에서 그 시간들만 예약 요청 보낼 수 있도록 할 예정.
* */

}
