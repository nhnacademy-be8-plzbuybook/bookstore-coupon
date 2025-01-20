package com.nhnacademy.boostorecoupon.repository;

import com.nhnacademy.boostorecoupon.entity.CouponPolicy;
import com.nhnacademy.boostorecoupon.enums.SaleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

@Disabled
@DataJpaTest
class CouponPolicyRepositoryTest {

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

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

        CouponPolicy policy2 = CouponPolicy.builder()
                .name("test2")
                .saleType(SaleType.AMOUNT)
                .minimumAmount(new BigDecimal("1000"))
                .discountLimit(new BigDecimal("20000"))
                .discountRatio(0)
                .isStackable(false)
                .couponScope("CATEGORY")
                .startDate(now.minusDays(2))
                .endDate(now.plusDays(3))
                .couponActive(true)
                .build();

        CouponPolicy policy3 = CouponPolicy.builder()
                .name("test3")
                .saleType(SaleType.AMOUNT)
                .minimumAmount(new BigDecimal("1000"))
                .discountLimit(new BigDecimal("20000"))
                .discountRatio(0)
                .isStackable(false)
                .couponScope("CATEGORY")
                .startDate(now.minusDays(2))
                .endDate(now.plusDays(3))
                .couponActive(false)
                .build();

        couponPolicyRepository.save(policy1);
        couponPolicyRepository.save(policy2);
        couponPolicyRepository.save(policy3);
    }

    @DisplayName("CouponActive 가 true 인 쿠폰정책 조회")
    @Test
    void findByCouponActiveOrderByIdAsc() {
        boolean active = true;
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<CouponPolicy> results = couponPolicyRepository.findByCouponActiveOrderByIdAsc(active, pageRequest);

        assertEquals("test1", results.getContent().get(0).getName());
        assertEquals("test2", results.getContent().get(1).getName());
    }

    @DisplayName("이름으로 쿠폰정책 조회")
    @Test
    void findByName() {
        String name = "test1";
        Optional<CouponPolicy> result = couponPolicyRepository.findByName(name);

        assertTrue(result.isPresent());
        assertEquals("test1", result.get().getName());
    }

}