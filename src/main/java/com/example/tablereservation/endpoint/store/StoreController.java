package com.example.tablereservation.endpoint.store;

import com.example.tablereservation.endpoint.store.dto.StoreCreateRequest;
import com.example.tablereservation.endpoint.store.dto.StoreResponse;
import com.example.tablereservation.endpoint.store.dto.StoreUpdateRequest;
import com.example.tablereservation.endpoint.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 매장 등록
    @PostMapping
    public ResponseEntity<Long> createStore(@RequestBody StoreCreateRequest request) {
        Long storeId = storeService.createStore(request);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(storeId);
    }

    // 매장 수정
    @PutMapping("/{storeId}")
    public ResponseEntity<Void> updateStore(@PathVariable Long storeId,
                                            @RequestBody StoreUpdateRequest request) {
        storeService.updateStore(storeId, request);
        return ResponseEntity.noContent().build();
    }

    // 매장 삭제
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

    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        List<StoreResponse> stores = storeService.getStoreList();
        return ResponseEntity.ok(stores);
    }

}
