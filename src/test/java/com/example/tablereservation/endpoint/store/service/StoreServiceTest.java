package com.example.tablereservation.endpoint.store.service;

import com.example.tablereservation.domain.store.Store;
import com.example.tablereservation.domain.store.StoreRepository;
import com.example.tablereservation.domain.user.User;
import com.example.tablereservation.domain.user.UserRepository;
import com.example.tablereservation.domain.user.type.Role;
import com.example.tablereservation.endpoint.store.dto.StoreCreateRequest;
import com.example.tablereservation.endpoint.store.dto.StoreResponse;
import com.example.tablereservation.endpoint.store.dto.StoreUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @DisplayName("매장 등록 성공")
    @Test
    void createStore_success() {
        // given
        User partner = User.builder()
            .id(1L)
            .name("partner1")
            .role(Role.PARTNER)
            .build();

        StoreCreateRequest request = StoreCreateRequest.builder()
            .partnerId(1L)
            .name("테스트 매장")
            .address("서울")
            .description("테스트용 매장입니다")
            .build();

        Store savedStore = Store.builder()
            .id(10L)
            .name(request.getName())
            .address(request.getAddress())
            .description(request.getDescription())
            .partner(partner)
            .build();

        given(userRepository.findById(1L)).willReturn(Optional.of(partner));
        given(storeRepository.save(any(Store.class))).willReturn(savedStore);

        ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);

        // when
        Long storeId = storeService.createStore(request);

        // then
        verify(storeRepository).save(captor.capture());
        Store capturedStore = captor.getValue();

        assertThat(storeId).isEqualTo(10L);
        assertThat(capturedStore.getName()).isEqualTo("테스트 매장");
        assertThat(capturedStore.getAddress()).isEqualTo("서울");
        assertThat(capturedStore.getDescription()).isEqualTo("테스트용 매장입니다");
        assertThat(capturedStore.getPartner().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("매장 등록 실패 - 일반 유저는 등록 불가")
    void createStore_fail_notPartner() {
        // given
        Long userId = 2L;

        User user = User.builder()
            .id(userId)
            .role(Role.USER) // 일반 유저!
            .build();

        StoreCreateRequest request = StoreCreateRequest.builder()
            .partnerId(userId)
            .name("맛집")
            .address("서울시 마포구")
            .description("일반 유저가 등록 시도")
            .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> storeService.createStore(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("매장은 파트너만 등록할 수 있습니다.");

        verify(storeRepository, never()).save(any());
    }

    @Test
    @DisplayName("매장 수정 성공")
    void updateStore_success() {
        // given
        User partner = User.builder()
            .id(1L)
            .build();

        Store store = Store.builder()
            .partner(partner)
            .id(1L)
            .name("테스트 매장")
            .partner(partner)
            .build();

        StoreUpdateRequest request = StoreUpdateRequest.builder()
            .partnerId(1L)
            .name("변경된 매장이름")
            .address("변경된 주소")
            .description("변경되었습니다")
            .build();

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        // when
        storeService.updateStore(1L, request);

        // then
        assertThat(store.getName()).isEqualTo("변경된 매장이름");
        assertThat(store.getAddress()).isEqualTo("변경된 주소");
        assertThat(store.getDescription()).isEqualTo("변경되었습니다");
    }

    @Test
    @DisplayName("매장 삭제 성공")
    void deleteStore_success() {
        // given
        User partner = User.builder()
            .id(1L)
            .build();

        Store store = Store.builder()
            .partner(partner)
            .id(1L)
            .name("테스트 매장")
            .partner(partner)
            .build();

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        // when
        storeService.deleteStore(1L, 1L);

        // then
        verify(storeRepository).delete(store);
    }

    @Test
    @DisplayName("매장 조회 성공")
    void getStoreById_success() {
        // given
        User partner = User.builder().id(1L).build();

        Store store = Store.builder()
            .id(1L)
            .name("테스트 매장")
            .partner(partner)
            .build();

        given(storeRepository.findById(1L)).willReturn(Optional.of(store));

        // when
        StoreResponse response = storeService.getStoreById(1L);

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("테스트 매장");
    }

    @Test
    @DisplayName("매장 리스트 조회 성공")
    void getStoreList_success() {
        // given
        User partner = User.builder()
            .id(1L)
            .build();

        List<Store> stores = List.of(
            Store.builder().id(1L)
                .partner(partner)
                .name("테스트 매장_1")
                .build(),
            Store.builder().id(2L)
                .partner(partner)
                .name("테스트 매장_2")
                .build()
        );

        given(storeRepository.findAllByOrderByNameAsc()).willReturn(stores);

        // when
        List<StoreResponse> responses = storeService.getStoreList();

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getName()).isEqualTo("테스트 매장_1");
    }

}