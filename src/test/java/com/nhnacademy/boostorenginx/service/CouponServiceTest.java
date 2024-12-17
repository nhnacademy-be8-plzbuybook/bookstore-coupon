package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.error.NotFoundCouponException;
import com.nhnacademy.boostorenginx.error.NotFoundCouponPolicyException;
import com.nhnacademy.boostorenginx.repository.CouponHistoryRepository;
import com.nhnacademy.boostorenginx.repository.CouponRepository;
import com.nhnacademy.boostorenginx.service.impl.CouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponHistoryRepository couponHistoryRepository;

    private CouponService couponService;
    private Coupon mockCoupon;
    private CouponPolicy mockCouponPolicy;

    @BeforeEach
    void setUp() {
        couponService = new CouponServiceImpl(couponRepository, couponHistoryRepository);

        mockCouponPolicy = CouponPolicy.builder().name("test").build();

        mockCoupon = new Coupon(
                Status.UNUSED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                mockCouponPolicy
        );
    }

    @Test
    void getCouponByCodeTest() {
        String code = "TEST1234";
        when(couponRepository.findByCode(code)).thenReturn(Optional.of(mockCoupon));
        assertThat(couponService.getCouponByCode(code)).isNotNull();
        verify(couponRepository, times(1)).findByCode(code);
    }

    @Test
    void getCouponByCode_WhenNotFoundCode() {
        String code = "INVALID";
        when(couponRepository.findByCode(code)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> couponService.getCouponByCode(code))
                .isInstanceOf(NotFoundCouponException.class)
                .hasMessage("코드에 해당하는 쿠폰을 찾을 수 없습니다: " + code);
    }

    @Test
    void getExpiredCouponsTest() {
        LocalDateTime now = LocalDateTime.now();
        when(couponRepository.findByExpiredAtBefore(any())).thenReturn(List.of(mockCoupon));
        List<Coupon> result = couponService.getExpiredCoupons(now);
        assertThat(result).hasSize(1);
    }

    @Test
    void getExpiredCouponsTest_WhenCurrentDateTimeIsNull() {
        assertThatThrownBy(() -> couponService.getExpiredCoupons(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("입력받은 currentDateTime 이 null 입니다");
    }

    @Test
    void getActiveCouponsTest() {
        LocalDateTime now = LocalDateTime.now();
        when(couponRepository.findActiveCoupons(now)).thenReturn(List.of(mockCoupon));
        List<Coupon> result = couponService.getActiveCoupons(now);
        assertThat(result).hasSize(1).contains(mockCoupon);
    }

    @Test
    void getActiveCouponsTest_WhenCurrentDateTimeIsNull() {
        assertThatThrownBy(() -> couponService.getActiveCoupons(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("입력받은 currentDateTime 이 null 입니다");
    }

    @Test
    void getCouponsByPolicyTest() {
        when(couponRepository.findByCouponPolicy(mockCouponPolicy)).thenReturn(List.of(mockCoupon));
        List<Coupon> result = couponService.getCouponsByPolicy(mockCouponPolicy);
        assertThat(result).hasSize(1).contains(mockCoupon);
    }

    @Test
    void getCouponsByPolicyTest_WhenCouponPolicyIsNull() {
        assertThatThrownBy(() -> couponService.getCouponsByPolicy(null))
                .isInstanceOf(NotFoundCouponPolicyException.class)
                .hasMessage("입력받은 couponPolicy 가 null 입니다");
    }

    @Test
    void getCouponsByStatsTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Coupon> page = new PageImpl<>(List.of(mockCoupon));
        when(couponRepository.findByStatus(Status.UNUSED, pageable)).thenReturn(page);
        Page<Coupon> result = couponService.getCouponsByStatus(Status.UNUSED, pageable);
        assertThat(result.getContent()).hasSize(1).contains(mockCoupon);
    }

    @Test
    void updateCouponStatusTest() {
        String code = "Test1234";
        String reason = "USED";
        when(couponRepository.findByCode(code)).thenReturn(Optional.of(mockCoupon));
        couponService.updateCouponStatus(code, Status.USED, reason);
        assertThat(mockCoupon.getStatus()).isEqualTo(Status.USED);
    }
}
