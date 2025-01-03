//package com.nhnacademy.boostorenginx.service.impl;
//
//import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryDuringRequestDto;
//import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryFindRequestDto;
//import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryStatusRequestDto;
//import com.nhnacademy.boostorenginx.entity.Coupon;
//import com.nhnacademy.boostorenginx.entity.CouponHistory;
//import com.nhnacademy.boostorenginx.enums.Status;
//import com.nhnacademy.boostorenginx.error.CouponHistoryException;
//import com.nhnacademy.boostorenginx.repository.CouponHistoryRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class CouponHistoryServiceImplTest {
//
//    @InjectMocks
//    private CouponHistoryServiceImpl couponHistoryService;
//
//    @Mock
//    private CouponHistoryRepository couponHistoryRepository;
//
//    private Coupon mockCoupon;
//    private CouponHistory mockHistory;
//
//    @BeforeEach
//    void setUp() {
//        mockCoupon = new Coupon();
//        mockHistory = new CouponHistory();
//    }
//
//    @DisplayName("쿠폰 ID 로 쿠폰이력 조회")
//    @Test
//    void getHistoryByCouponId() {
//        Long historyId = 1L;
//        int page = 0;
//        int pageSize = 5;
//        Pageable pageable = PageRequest.of(page, pageSize);
//        CouponHistoryFindRequestDto requestDto = new CouponHistoryFindRequestDto(historyId, page, pageSize);
//        CouponHistory history1 = new CouponHistory();
//        CouponHistory history2 = new CouponHistory();
//        Page<CouponHistory> mockPage = new PageImpl<>(Arrays.asList(history1, history2), pageable, 2);
//        when(couponHistoryRepository.findByCoupon_idOrderByCouponIdAsc(historyId, pageable)).thenReturn(mockPage);
//        Page<CouponHistory> result = couponHistoryService.getHistoryByCouponId(requestDto);
//        verify(couponHistoryRepository, times(1)).findByCoupon_idOrderByCouponIdAsc(historyId, pageable);
//        assertEquals(2, result.getTotalElements());
//    }
//
//    @DisplayName("존재하지않은 쿠폰 ID 로 쿠폰이력 조회시 예외발생")
//    @Test
//    void getHistoryByCouponId_NotFoundCouponId() {
//        Long historyId = -1L;
//        int page = 0;
//        int pageSize = 5;
//        CouponHistoryFindRequestDto requestDto = new CouponHistoryFindRequestDto(historyId, page, pageSize);
//        CouponHistoryException exception = assertThrows(CouponHistoryException.class,
//                () -> couponHistoryService.getHistoryByCouponId(requestDto));
//        assertEquals("잘못된 ID 입니다: " + historyId, exception.getMessage());
//        verifyNoInteractions(couponHistoryRepository);
//    }
//
//    @DisplayName("Status 로 쿠폰이력 조회")
//    @Test
//    void getHistoryByStatus() {
//        Status status = Status.UNUSED;
//        int page = 0;
//        int pageSize = 5;
//        Pageable pageable = PageRequest.of(page, pageSize);
//        CouponHistoryStatusRequestDto request = new CouponHistoryStatusRequestDto(status.toString(), page, pageSize);
//        CouponHistory history1 = new CouponHistory();
//        CouponHistory history2 = new CouponHistory();
//        Page<CouponHistory> mockPage = new PageImpl<>(Arrays.asList(history1, history2), pageable, 2);
//        when(couponHistoryRepository.findByStatusOrderByChangeDateAsc(status, pageable)).thenReturn(mockPage);
//        Page<CouponHistory> result = couponHistoryService.getHistoryByStatus(request);
//        verify(couponHistoryRepository, times(1)).findByStatusOrderByChangeDateAsc(status, pageable);
//        assertEquals(2, result.getTotalElements());
//    }
//
//    @DisplayName("특정 기간의 쿠폰이력 조회")
//    @Test
//    void getHistoryDate() {
//        LocalDateTime start = LocalDateTime.now().minusDays(7);
//        LocalDateTime end = LocalDateTime.now();
//        int page = 0;
//        int pageSize = 5;
//        Pageable pageable = PageRequest.of(page, pageSize);
//        CouponHistoryDuringRequestDto request = new CouponHistoryDuringRequestDto(start, end, page, pageSize);
//        CouponHistory history1 = new CouponHistory();
//        CouponHistory history2 = new CouponHistory();
//        Page<CouponHistory> mockPage = new PageImpl<>(Arrays.asList(history1, history2), pageable, 2);
//        when(couponHistoryRepository.findChangeDate(start, end, pageable)).thenReturn(mockPage);
//        Page<CouponHistory> result = couponHistoryService.getHistoryDate(request);
//        verify(couponHistoryRepository, times(1)).findChangeDate(start, end, pageable);
//        assertEquals(2, result.getTotalElements());
//    }
//}