package com.nhnacademy.boostorenginx.repository;


import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
class CouponHistoryRepositoryTest {

    @Mock
    CouponHistoryRepository couponHistoryRepository;

    private Coupon coupon;
    private CouponHistory couponHistory1;
    private CouponHistory couponHistory2;

    @BeforeEach
    void setUp() {
        coupon = new Coupon(
                Status.UNUSED,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().plusDays(5),
                null
        );

        couponHistory1 = CouponHistory.builder()
                .coupon(coupon)
                .status(Status.USED)
                .changeDate(LocalDateTime.now().minusDays(1))
                .reason("CANCEL")
                .build();

        couponHistory2 = CouponHistory.builder()
                .coupon(coupon)
                .status(Status.EXPIRED)
                .changeDate(LocalDateTime.now().minusDays(3))
                .reason("EXPIRED")
                .build();
    }

    @Test
    void findByCouponTest() {
        when(couponHistoryRepository.findByCoupon(coupon)).thenReturn(Arrays.asList(couponHistory1, couponHistory2));
        List<CouponHistory> result = couponHistoryRepository.findByCoupon(coupon);
        assertThat(result).hasSize(2);
        assertThat(result).extracting("status").containsExactlyInAnyOrder(Status.USED, Status.EXPIRED);
    }

    @Test
    void findByCoupon_idTest() {
        when(couponHistoryRepository.findByCoupon_id(coupon.getId())).thenReturn(Arrays.asList(couponHistory1, couponHistory2));
        List<CouponHistory> result = couponHistoryRepository.findByCoupon_id(coupon.getId());
        assertThat(result).hasSize(2);
        assertThat(result).extracting("status").containsExactlyInAnyOrder(Status.USED, Status.EXPIRED);
    }
}
