package com.example.tablereservation.endpoint.store;


import com.example.tablereservation.domain.reservation.Reservation;
import com.example.tablereservation.domain.reservation.ReservationStatus;
import com.example.tablereservation.domain.store.Store;
import com.example.tablereservation.domain.user.User;
import com.example.tablereservation.domain.user.type.Role;
import com.example.tablereservation.endpoint.store.dto.StoreCreateRequest;
import com.example.tablereservation.endpoint.store.dto.StoreResponse;
import com.example.tablereservation.endpoint.store.dto.StoreUpdateRequest;
import com.example.tablereservation.endpoint.store.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StoreController.class)
@AutoConfigureMockMvc
//@WithMockUser(username = "partner", roles = {"PARTNER"})
class StoreControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private StoreService storeService;
//
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    @Test
//    @DisplayName("매장 등록 성공")
//    void createStore_success() throws Exception {
//        // given
//        StoreCreateRequest request = StoreCreateRequest.builder()
//            .partnerId(1L)
//            .name("테스트 매장")
//            .address("서울")
//            .description("설명입니다")
//            .latitude(37.0)
//            .longitude(127.0)
//            .build();
//        User partner = User.builder()
//            .id(1L)
//            .role(Role.PARTNER)
//            .build();
//
////        given(userRepository.findById(1L)).willReturn(Optional.of(partner));
//
//        given(storeService.createStore(any())).willReturn(10L);
//
//        mockMvc.perform(post("/api/stores")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request))
//            ).andDo(print())
//            .andExpect(status().isCreated())
//            .andExpect(content().string("10"));
//    }
//
//    @Test
//    @DisplayName("매장 수정 성공")
//    void updateStore_success() throws Exception {
//        given(storeService.createStore(any()))
//            .willReturn(10L);  // 매장 ID 10이 반환된다고 가정
//
//        // when: POST 요청을 보냄
//        mockMvc.perform(post("/api/stores")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(
//                     StoreCreateRequest.builder()
//                         .partnerId(1L)
//                         .name("테스트 매장")
//                         .address("주소")
//                         .availableReservationCount(5)
//                         .build()
//
//                )))
//            .andExpect(status().isCreated())  // HTTP 201 Created 상태를 기대
//            .andExpect(content().string("10"))  // 응답 본문이 10인 것을 확인
//            .andDo(print());  // 요청과 응답을 콘솔에 출력
//
//        verify(storeService, times(1)).updateStore(eq(1L), any());
//
//    }
//
//    @Test
//    @DisplayName("매장 삭제 성공")
//    void deleteStore_success() throws Exception {
//        mockMvc.perform(delete("/api/stores/1")
//                .param("userId", "1"))
//            .andDo(print())
//            .andExpect(status().isNoContent());
//
//        verify(storeService).deleteStore(1L, 1L);
//    }
//
//    @Test
//    @DisplayName("매장 단건 조회 성공")
//    void getStore_success() throws Exception {
//        StoreResponse response = StoreResponse.builder()
//            .id(1L)
//            .name("테스트 매장")
//            .address("서울")
//            .description("설명")
//            .partnerId(1L)
//            .build();
//
//        given(storeService.getStoreById(1L)).willReturn(response);
//
//        mockMvc.perform(get("/api/stores/1"))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.id").value(1L))
//            .andExpect(jsonPath("$.name").value("테스트 매장"));
//
//    }
//
//    @Test
//    @DisplayName("매장 리스트 조회 성공")
//    void getAllStores_success() throws Exception {
//        List<StoreResponse> list = List.of(
//            StoreResponse.builder().id(1L).name("매장1").build(),
//            StoreResponse.builder().id(2L).name("매장2").build()
//        );
//
//        given(storeService.getStoreList()).willReturn(list);
//
//        mockMvc.perform(get("/api/stores"))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andExpect(jsonPath("$.size()").value(2))
//            .andExpect(jsonPath("$[0].name").value("매장1"));
//    }
}