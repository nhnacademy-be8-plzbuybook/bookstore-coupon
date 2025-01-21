package com.nhnacademy.bookstorecoupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstorecoupon.dto.couponhistory.CouponHistoryDuringRequestDto;
import com.nhnacademy.bookstorecoupon.dto.couponhistory.CouponHistoryFindRequestDto;
import com.nhnacademy.bookstorecoupon.dto.couponhistory.CouponHistoryResponseDto;
import com.nhnacademy.bookstorecoupon.dto.couponhistory.CouponHistoryStatusRequestDto;
import com.nhnacademy.bookstorecoupon.entity.Coupon;
import com.nhnacademy.bookstorecoupon.entity.CouponHistory;
import com.nhnacademy.bookstorecoupon.enums.Status;
import com.nhnacademy.bookstorecoupon.service.CouponHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CouponHistoryController.class)
class CouponHistoryControllerTest {

    @MockBean
    private CouponHistoryService couponHistoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Coupon mockCoupon1;
    private Coupon mockCoupon2;

    private CouponHistoryFindRequestDto findRequestDto;
    private CouponHistoryStatusRequestDto statusRequestDto;

    private Page<CouponHistory> mockHistoryPage;
    private Page<CouponHistoryResponseDto> mockResponse;

    @BeforeEach
    void setUp() {
        mockCoupon1 = mock(Coupon.class);
        when(mockCoupon1.getId()).thenReturn(1L);
        when(mockCoupon1.getStatus()).thenReturn(Status.UNUSED);

        mockCoupon2 = mock(Coupon.class);
        when(mockCoupon2.getId()).thenReturn(2L);
        when(mockCoupon2.getStatus()).thenReturn(Status.USED);

        findRequestDto = new CouponHistoryFindRequestDto(1L, 0, 10);
        statusRequestDto = new CouponHistoryStatusRequestDto("USED", 0, 10);

        List<CouponHistory> mockHistories = Arrays.asList(
                CouponHistory.builder()
                        .status(Status.USED)
                        .changeDate(LocalDateTime.now().minusDays(1))
                        .reason("test1")
                        .coupon(mockCoupon1)
                        .build(),
                CouponHistory.builder()
                        .status(Status.EXPIRED)
                        .changeDate(LocalDateTime.now().minusDays(2))
                        .reason("test2")
                        .coupon(mockCoupon1)
                        .build(),
                CouponHistory.builder()
                        .status(Status.USED)
                        .changeDate(LocalDateTime.now().plusDays(2))
                        .reason("test3")
                        .coupon(mockCoupon2)
                        .build()
        );

        mockHistoryPage = new PageImpl<>(mockHistories, PageRequest.of(0, 10), mockHistories.size());
    }

    @DisplayName("쿠폰 ID 에 해당하는 쿠폰이력 목록 조회")
    @Test
    void getHistoryByCouponId() throws Exception {
        Long couponId = 1L;

        when(couponHistoryService.getHistoryByCouponId(any(CouponHistoryFindRequestDto.class)))
                .thenReturn(mockHistoryPage);

        mockMvc.perform(get("/api/coupon-histories/{coupon-id}", couponId)
                        .param("page", String.valueOf(findRequestDto.page()))
                        .param("pageSize", String.valueOf(findRequestDto.pageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].couponId").value(1L))
                .andExpect(jsonPath("$.content[0].status").value("USED"))
                .andExpect(jsonPath("$.content[0].changeDate").exists())
                .andExpect(jsonPath("$.content[0].reason").value("test1"))
                .andExpect(jsonPath("$.content[1].couponId").value(1L))
                .andExpect(jsonPath("$.content[1].status").value("EXPIRED"))
                .andExpect(jsonPath("$.content[1].changeDate").exists())
                .andExpect(jsonPath("$.content[1].reason").value("test2"));

        verify(couponHistoryService, times(1)).getHistoryByCouponId(any(CouponHistoryFindRequestDto.class));
    }

    @DisplayName("특정 상태인 쿠폰이력 목록 조회")
    @Test
    void getHistoryByStatus() throws Exception {
        String status = "USED";
        int page = 0;
        int pageSize = 10;

        List<CouponHistory> usedHistories = mockHistoryPage.getContent().stream()
                .filter(history -> history.getStatus() == Status.USED)
                .toList();
        Page<CouponHistory> filter = new PageImpl<>(usedHistories, PageRequest.of(page, pageSize), usedHistories.size());

        when(couponHistoryService.getHistoryByStatus(any(CouponHistoryStatusRequestDto.class)))
                .thenReturn(filter);

        mockMvc.perform(get("/api/coupon-histories/status/{status}", status)
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status").value("USED"))
                .andExpect(jsonPath("$.content[0].reason").value("test1"))
                .andExpect(jsonPath("$.content[0].couponId").value(1L))
                .andExpect(jsonPath("$.content[1].status").value("USED"))
                .andExpect(jsonPath("$.content[1].reason").value("test3"))
                .andExpect(jsonPath("$.content[1].couponId").value(2L));

        verify(couponHistoryService, times(1)).getHistoryByStatus(any(CouponHistoryStatusRequestDto.class));
    }

    @DisplayName("특정 기간 쿠폰이력 목록 조회")
    @Test
    void getHistoryDate() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().minusDays(3);
        LocalDateTime endDate = LocalDateTime.now();

        List<CouponHistory> activeHistories = mockHistoryPage.getContent().stream()
                .filter(history -> history.getChangeDate().isAfter(startDate) && history.getChangeDate().isBefore(endDate))
                .toList();
        Page<CouponHistory> filter = new PageImpl<>(activeHistories, PageRequest.of(0, 10), activeHistories.size());

        when(couponHistoryService.getHistoryDate(any(CouponHistoryDuringRequestDto.class)))
                .thenReturn(filter);

        mockMvc.perform(get("/api/coupon-histories/period")
                        .param("start-date", startDate.toString())
                        .param("end-date", endDate.toString())
                        .param("page", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status").value("USED"))
                .andExpect(jsonPath("$.content[0].reason").value("test1"))
                .andExpect(jsonPath("$.content[0].couponId").value(1L))
                .andExpect(jsonPath("$.content[1].status").value("EXPIRED"))
                .andExpect(jsonPath("$.content[1].reason").value("test2"))
                .andExpect(jsonPath("$.content[1].couponId").value(1L));

        verify(couponHistoryService, times(1)).getHistoryDate(any(CouponHistoryDuringRequestDto.class));
    }
}