package com.nhnacademy.boostorenginx.repository;


import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class CouponRepositoryTest {

    @Mock
    CouponRepository couponRepository;

    private CouponPolicy couponPolicy;
    private Coupon coupon1;
    private Coupon coupon2;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicy.builder()
                .name("Policy1")
                .saleType(SaleType.AMOUNT)
                .minimumAmount(new java.math.BigDecimal("1000"))
                .discountLimit(new java.math.BigDecimal("5000"))
                .discountRatio(0)
                .isStackable(true)
                .couponScope("book")
                .startDate(LocalDateTime.now().minusDays(5))
                .endDate(LocalDateTime.now().plusDays(5))
                .couponActive(true)
                .build();

        coupon1 = new Coupon(
                Status.UNUSED,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(3),
                couponPolicy
        );

        coupon2 = new Coupon(
                Status.EXPIRED,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(3),
                couponPolicy
        );
    }

    @Test
    void findByCodeTest() {
        when(couponRepository.findByCode("TEST1234CODE5678")).thenReturn(Optional.of(coupon1));
        Optional<Coupon> result = couponRepository.findByCode("TEST1234CODE5678");
        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo(coupon1.getCode());
    }

    @Test
    void findByExpiredAtBeforeTest() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        when(couponRepository.findByExpiredAtBefore(currentDateTime)).thenReturn(List.of(coupon2));
        List<Coupon> result = couponRepository.findByExpiredAtBefore(currentDateTime);
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getStatus()).isEqualTo(Status.EXPIRED);
    }

    @Test
    void findActiveCouponsTest() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        when(couponRepository.findActiveCoupons(currentDateTime)).thenReturn(List.of(coupon1));
        List<Coupon> result = couponRepository.findActiveCoupons(currentDateTime);
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getStatus()).isEqualTo(Status.UNUSED);
    }

    @Test
    void findByCouponPolicyTest() {
        when(couponRepository.findByCouponPolicy(couponPolicy)).thenReturn(Arrays.asList(coupon1, coupon2));
        List<Coupon> result = couponRepository.findByCouponPolicy(couponPolicy);
        assertThat(result).hasSize(2);
        assertThat(result).extracting("status").contains(Status.UNUSED, Status.EXPIRED);
    }

    @Test
    void findByStatusTest() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Coupon> mockPage = new PageImpl<>(List.of(coupon1), pageable, 1);
        when(couponRepository.findByStatus(Status.UNUSED, pageable)).thenReturn(mockPage);
        Page<Coupon> result = couponRepository.findByStatus(Status.UNUSED, pageable);
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().getStatus()).isEqualTo(Status.UNUSED);
    }

}
