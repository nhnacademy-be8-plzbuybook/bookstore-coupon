package com.nhnacademy.boostorecoupon.repository;


import com.nhnacademy.boostorecoupon.entity.Coupon;
import com.nhnacademy.boostorecoupon.entity.CouponPolicy;
import com.nhnacademy.boostorecoupon.enums.SaleType;
import com.nhnacademy.boostorecoupon.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    private CouponPolicy couponPolicy;
    private Coupon coupon1;
    private Coupon coupon2;
    private Coupon coupon3;

    @BeforeEach
    void setUp() {

        couponPolicy = CouponPolicy.builder()
                .name("policy")
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
        couponPolicyRepository.save(couponPolicy);

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

        coupon3 = new Coupon(
                Status.UNUSED,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(3),
                couponPolicy
        );

        couponRepository.save(coupon1);
        couponRepository.save(coupon2);
        couponRepository.save(coupon3);
    }

    @DisplayName("쿠폰 ID 로 쿠폰에 해당하는 쿠폰정책 객체 찾기")
    @Test
    void findCouponPolicyByCouponId() {
        Optional<CouponPolicy> result = couponRepository.findCouponPolicyByCouponId(coupon1.getId());

        assertTrue(result.isPresent());
        assertInstanceOf(CouponPolicy.class, result.get());
        assertEquals(couponPolicy.getId(), result.get().getId());
    }

    @DisplayName("쿠폰의 코드로 쿠폰 조회")
    @Test
    void findByCode() {
        Optional<Coupon> result = couponRepository.findByCode(coupon1.getCode());

        assertTrue(result.isPresent());
        assertEquals(coupon1.getId(), result.get().getId());
    }

    @DisplayName("현재 시간을 기준으로 만료된 쿠폰 목록 조회")
    @Test
    void findByExpiredAtBeforeOrderByExpiredAtAsc() {
        Page<Coupon> result = couponRepository.findByExpiredAtBeforeOrderByExpiredAtAsc(LocalDateTime.now(), PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
        assertEquals(coupon2.getId(), result.getContent().getFirst().getId());
    }

    @DisplayName("기간이 유효한 쿠폰 목록 조회")
    @Test
    void findActiveCoupons() {
        Page<Coupon> result = couponRepository.findActiveCoupons(LocalDateTime.now(), PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals(coupon1.getId(), result.getContent().getFirst().getId());
    }

    @DisplayName("쿠폰정책으로 쿠폰 목록 조회")
    @Test
    void findByCouponPolicyOrderByIdAsc() {
        Page<Coupon> result = couponRepository.findByCouponPolicyOrderByIdAsc(couponPolicy, PageRequest.of(0, 10));

        assertEquals(3, result.getTotalElements());
        assertEquals(coupon1.getId(), result.getContent().get(0).getId());
        assertEquals(coupon2.getId(), result.getContent().get(1).getId());
        assertEquals(coupon3.getId(), result.getContent().get(2).getId());
    }

    @DisplayName("쿠폰 상태로 쿠폰 목록 조회")
    @Test
    void findByStatusOrderByStatusAsc() {
        Page<Coupon> result = couponRepository.findByStatusOrderByStatusAsc(Status.UNUSED, PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
        assertEquals(coupon1.getId(), result.getContent().get(0).getId());
        assertEquals(coupon3.getId(), result.getContent().get(1).getId());
    }

    @DisplayName("기한이 만료되고 쿠폰의 상태가 UNUSED 인 쿠폰 목록 조회")
    @Test
    void findByExpiredAtBeforeAndStatusOrderByExpiredAtAsc() {
        Page<Coupon> result = couponRepository.findByExpiredAtBeforeAndStatusOrderByExpiredAtAsc(LocalDateTime.now(), Status.UNUSED, PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals(coupon3.getId(), result.getContent().getFirst().getId());
    }
}
