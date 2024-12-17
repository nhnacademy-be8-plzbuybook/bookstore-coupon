package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.error.CouponHistoryException;
import com.nhnacademy.boostorenginx.repository.CouponHistoryRepository;
import com.nhnacademy.boostorenginx.service.impl.CouponHistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponHistoryServiceTest {

    @Mock
    private CouponHistoryRepository couponHistoryRepository;

    @InjectMocks
    private CouponHistoryServiceImpl couponHistoryService;

    private Coupon coupon;
    private CouponHistory couponHistory;
    private CouponPolicy couponPolicy;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        coupon = new Coupon(
                Status.UNUSED,
                now,
                now.plusDays(2),
                couponPolicy
        );

        couponHistory = CouponHistory.builder()
                .coupon(coupon)
                .status(Status.UNUSED)
                .changeDate(now)
                .reason("CREATE")
                .build();
    }

    @Test
    void createHistoryTest() {
        when(couponHistoryRepository.save(any(CouponHistory.class))).thenReturn(couponHistory);
        CouponHistory result = couponHistoryService.createHistory(coupon, Status.UNUSED, "CREATE");
        assertEquals(Status.UNUSED, result.getStatus());
        assertEquals("CREATE", result.getReason());
        verify(couponHistoryRepository, times(1)).save(any(CouponHistory.class));
    }

    @Test
    void createHistoryTest_WhenArgumentsIsNull() {
        assertAll(
                () -> assertThrows(CouponHistoryException.class, () -> couponHistoryService.createHistory(null, Status.UNUSED, "REASON")),
                () -> assertThrows(CouponHistoryException.class, () -> couponHistoryService.createHistory(coupon, null, "REASON")),
                () -> assertThrows(CouponHistoryException.class, () -> couponHistoryService.createHistory(coupon, Status.UNUSED, null))
        );
    }

    @Test
    void getHistoryByCouponTest() {
        when(couponHistoryRepository.findByCoupon_id(1L)).thenReturn(Arrays.asList(couponHistory));
        List<CouponHistory> result = couponHistoryService.getHistoryByCouponId(1L);
        assertEquals(1, result.size());
        verify(couponHistoryRepository, times(1)).findByCoupon_id(1L);
    }

    @Test
    void getHistoryByCouponTest_WhenCouponIsNull() {
        assertThrows(CouponHistoryException.class, () -> couponHistoryService.getHistoryByCoupon(null));
    }

    @Test
    void getHistoryByCouponIdTest() {
        when(couponHistoryRepository.findByCoupon_id(1L)).thenReturn(Arrays.asList(couponHistory));
        List<CouponHistory> result = couponHistoryService.getHistoryByCouponId(1L);
        assertEquals(1, result.size());
        verify(couponHistoryRepository, times(1)).findByCoupon_id(1L);
    }

    @Test
    void getHistoryByCouponId_WhenCouponIdIsInvalid() {
        assertAll(
                () -> assertThrows(CouponHistoryException.class, () -> couponHistoryService.getHistoryByCouponId(null)),
                () -> assertThrows(CouponHistoryException.class, () -> couponHistoryService.getHistoryByCouponId(-1L))
        );
    }

    @Test
    void getHistoryByCouponAndStatusTest() {
        when(couponHistoryRepository.findByCouponAndStatus(coupon, Status.UNUSED)).thenReturn(Arrays.asList(couponHistory));
        List<CouponHistory> result = couponHistoryService.getHistoryByCouponAndStatus(coupon, Status.UNUSED);
        assertEquals(1, result.size());
        verify(couponHistoryRepository, times(1)).findByCouponAndStatus(coupon, Status.UNUSED);
    }

    @Test
    void getHistoryByCouponAndStatus_WhenCouponOrStatusIsNull() {
        assertAll(
                () -> assertThrows(CouponHistoryException.class, () -> couponHistoryService.getHistoryByCouponAndStatus(null, Status.UNUSED)),
                () -> assertThrows(CouponHistoryException.class, () -> couponHistoryService.getHistoryByCouponAndStatus(coupon, null))
        );
    }
}
