package com.example.tablereservation.endpoint.store.service;

import com.example.tablereservation.domain.store.Store;
import com.example.tablereservation.domain.store.StoreRepository;
import com.example.tablereservation.domain.user.User;
import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.domain.user.type.Role;
import com.example.tablereservation.endpoint.store.dto.StoreCreateRequest;
import com.example.tablereservation.endpoint.store.dto.StoreResponse;
import com.example.tablereservation.endpoint.store.dto.StoreUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    /**
     * 매장 등록
     */
    @Transactional
    public Long createStore(StoreCreateRequest request, Long partnerId) {
        User partner = userRepository.findById(partnerId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));

        if (partner.getRole() != Role.PARTNER) {
            throw new IllegalArgumentException("매장은 파트너만 등록할 수 있습니다.");
        }

        Store store = request.toEntity(partner);

        return storeRepository.save(store).getId();
    }

    /**
     * 매장 수정
     */
    @Transactional
    public void updateStore(Long storeId,Long partnerId, StoreUpdateRequest request) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 매장입니다."));

        if (!store.getPartner().getId().equals(partnerId)) {
            throw new IllegalArgumentException("본인의 매장만 수정할 수 있습니다.");
        }
         store.update(request);
    }

    /**
     * 매장 삭제
     */
    @Transactional
    public void deleteStore(Long partnerId, Long storeId) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 매장입니다."));

        if (!store.getPartner().getId().equals(partnerId)) {
            throw new RuntimeException("본인의 매장만 삭제할 수 있습니다.");
        }
        storeRepository.delete(store);
    }

    /**
     * 매장 조회
     */
    public StoreResponse getStoreById(Long storeId) {
        Store store = storeRepository.findById(storeId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 매장입니다."));

        return StoreResponse.from(store);
    }

    public List<StoreResponse> getStoreList() {
        List<Store> storeList = storeRepository.findAllByOrderByNameAsc();
        return storeList.stream()
            .map(StoreResponse::from)
            .collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public List<ReservationResponse> getDayReservationsByStore(Long storeId, LocalDate date) {
//        Store store = storeRepository.findById());
//        return reservationRepository.findDayReservationsByStore(store, date).stream()
//            .map(ReservationResponse::fromEntity)
//            .toList();
//    }

    // 예약 가능한 시간 조회 메서드 추가 예정

}

