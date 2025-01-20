package com.nhnacademy.boostorecoupon.repository;


import com.nhnacademy.boostorecoupon.entity.Coupon;
import com.nhnacademy.boostorecoupon.entity.CouponHistory;
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
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class CouponHistoryRepositoryTest {

    @Autowired
    private CouponHistoryRepository couponHistoryRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    private Coupon coupon;

    private CouponHistory history1;
    private CouponHistory history2;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        CouponPolicy policy = CouponPolicy.builder()
                .name("test")
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
        couponPolicyRepository.save(policy);

        coupon = new Coupon(
                Status.UNUSED,
                now.minusDays(5),
                now.plusDays(5),
                policy
        );
        couponRepository.save(coupon);

        history1 = CouponHistory.builder()
                .status(Status.USED)
                .changeDate(now.minusDays(1))
                .reason("CANCEL")
                .coupon(coupon)
                .build();

        history2 = CouponHistory.builder()
                .status(Status.EXPIRED)
                .changeDate(now.minusDays(3))
                .reason("EXPIRED")
                .coupon(coupon)
                .build();

        couponHistoryRepository.save(history1);
        couponHistoryRepository.save(history2);
    }

    @DisplayName("쿠폰 ID 로 쿠폰변경이력 리스트 조회")
    @Test
    void findByCoupon_idOrderByCouponIdAsc() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<CouponHistory> results = couponHistoryRepository.findByCoupon_idOrderByCouponIdAsc(coupon.getId(), pageable);

        assertEquals(2, results.getTotalElements());

        assertEquals(history1.getId(), results.getContent().get(0).getId());
        assertEquals(history2.getId(), results.getContent().get(1).getId());
    }

    @DisplayName("CouponHistory 의 Status 에 해당하는 리스트 조회")
    @Test
    void findByStatusOrderByChangeDateAsc() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<CouponHistory> results = couponHistoryRepository.findByStatusOrderByChangeDateAsc(Status.USED, pageable);
        assertEquals(1, results.getTotalElements());
        assertEquals(history1.getId(), results.getContent().getFirst().getId());
    }

    @DisplayName("특정 기간 동안의 CouponHistory 조회")
    @Test
    void findChangeDate() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(10);
        LocalDateTime endDate = LocalDateTime.now().minusDays(2);
        Pageable pageable = PageRequest.of(0, 10);

        Page<CouponHistory> results = couponHistoryRepository.findChangeDate(startDate, endDate, pageable);

        assertEquals(1, results.getTotalElements());
        assertEquals(history2.getId(), results.getContent().getFirst().getId());
    }

}
