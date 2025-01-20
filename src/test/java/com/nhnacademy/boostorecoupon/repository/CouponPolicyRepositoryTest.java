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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CouponPolicyRepositoryTest {

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @Autowired
    private CouponRepository couponRepository;

    private Coupon coupon;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        CouponPolicy policy1 = CouponPolicy.builder()
                .name("test1")
                .saleType(SaleType.RATIO)
                .minimumAmount(new BigDecimal("1000"))
                .discountLimit(new BigDecimal("10000"))
                .discountRatio(10)
                .isStackable(true)
                .couponScope("ALL")
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(10))
                .couponActive(true)
                .build();
        couponPolicyRepository.save(policy1);

        CouponPolicy policy2 = CouponPolicy.builder()
                .name("test2")
                .saleType(SaleType.AMOUNT)
                .minimumAmount(new BigDecimal("1000"))
                .discountLimit(new BigDecimal("20000"))
                .discountRatio(0)
                .isStackable(false)
                .couponScope("CATEGORY")
                .startDate(now.minusDays(2))
                .endDate(now.minusDays(1))
                .couponActive(true)
                .build();
        couponPolicyRepository.save(policy2);

        CouponPolicy policy3 = CouponPolicy.builder()
                .name("test3")
                .saleType(SaleType.AMOUNT)
                .minimumAmount(new BigDecimal("1000"))
                .discountLimit(new BigDecimal("20000"))
                .discountRatio(0)
                .isStackable(false)
                .couponScope("CATEGORY")
                .startDate(now.minusDays(2))
                .endDate(now.plusDays(1))
                .couponActive(false)
                .build();
        couponPolicyRepository.save(policy3);

        coupon = new Coupon(
                Status.UNUSED,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                policy1
        );
        couponRepository.save(coupon);

    }

    @DisplayName("CouponActive 가 true 인 쿠폰정책 조회")
    @Test
    void findByCouponActiveOrderByIdAsc_WhenCouponActiveTrue() {
        boolean active = true;
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<CouponPolicy> results = couponPolicyRepository.findByCouponActiveOrderByIdAsc(active, pageRequest);

        assertEquals("test1", results.getContent().get(0).getName());
        assertEquals("test2", results.getContent().get(1).getName());
    }

    @DisplayName("CouponActive 가 false 인 쿠폰정책 조회")
    @Test
    void findByCouponActiveOrderByIdAsc_WhenCouponActiveIsFalse() {
        boolean active = false;
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<CouponPolicy> results = couponPolicyRepository.findByCouponActiveOrderByIdAsc(active, pageRequest);

        assertEquals("test3", results.getContent().getFirst().getName());
    }

    @DisplayName("만료된 쿠폰정책 조회")
    @Test
    void findExpiredCouponPolicies() {
        boolean active = true;
        LocalDateTime now = LocalDateTime.now();
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<CouponPolicy> results = couponPolicyRepository.findExpiredCouponPolicies(active, now, pageRequest);

        assertEquals(1, results.getTotalElements());
        assertEquals("test2", results.getContent().getFirst().getName());
    }

    @DisplayName("이름으로 쿠폰정책 조회")
    @Test
    void findByName() {
        String name = "test1";
        Optional<CouponPolicy> result = couponPolicyRepository.findByName(name);

        assertTrue(result.isPresent());
        assertEquals("test1", result.get().getName());
    }

    @DisplayName("쿠폰 ID 로 쿠폰정책 조회")
    @Test
    void findCouponPolicyByCouponId() {
        Optional<CouponPolicy> result = couponPolicyRepository.findCouponPolicyByCouponId(coupon.getId());

        assertTrue(result.isPresent());
        assertEquals("test1", result.get().getName());
    }
}