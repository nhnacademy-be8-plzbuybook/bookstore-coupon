package com.nhnacademy.bookstorecoupon.repository;

import com.nhnacademy.bookstorecoupon.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.entity.CouponTarget;
import com.nhnacademy.bookstorecoupon.enums.SaleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CouponTargetRepositoryTest {

    @Autowired
    private CouponTargetRepository couponTargetRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    private CouponPolicy couponPolicy;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        couponPolicy = CouponPolicy.builder()
                .name("test")
                .saleType(SaleType.AMOUNT)
                .minimumAmount(new BigDecimal("1000"))
                .discountLimit(new BigDecimal("5000"))
                .discountRatio(0)
                .isStackable(true)
                .couponScope("book")
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(1))
                .couponActive(true)
                .build();
        couponPolicyRepository.save(couponPolicy);

        CouponTarget couponTarget1 = CouponTarget.builder()
                .ctTargetId(0L)
                .couponPolicy(couponPolicy)
                .build();

        CouponTarget couponTarget2 = CouponTarget.builder()
                .ctTargetId(1L)
                .couponPolicy(couponPolicy)
                .build();

        couponTargetRepository.save(couponTarget1);
        couponTargetRepository.save(couponTarget2);
    }

    @DisplayName("쿠폰정책 ID 로 특정 쿠폰정책에 속한 쿠폰대상 목록 조회")
    @Test
    void findByCouponPolicy_IdOrderByIdAsc() {
        Long policyId = couponPolicy.getId();
        Pageable pageable = PageRequest.of(0, 10);

        Page<CouponTarget> targets = couponTargetRepository.findByCouponPolicy_IdOrderByIdAsc(policyId, pageable);

        assertEquals(2, targets.getTotalElements());
        assertEquals(0L, targets.getContent().get(0).getCtTargetId());
        assertEquals(1L, targets.getContent().get(1).getCtTargetId());
    }

}
