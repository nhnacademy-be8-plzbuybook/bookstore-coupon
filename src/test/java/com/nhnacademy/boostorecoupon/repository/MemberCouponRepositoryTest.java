package com.nhnacademy.boostorecoupon.repository;

import com.nhnacademy.boostorecoupon.entity.Coupon;
import com.nhnacademy.boostorecoupon.entity.CouponPolicy;
import com.nhnacademy.boostorecoupon.entity.MemberCoupon;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberCouponRepositoryTest {

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    private CouponPolicy policy;
    private Coupon coupon1;
    private Coupon coupon2;
    private MemberCoupon memberCoupon1;
    private MemberCoupon memberCoupon2;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        policy = CouponPolicy.builder()
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
        couponPolicyRepository.save(policy);

        coupon1 = new Coupon(
                Status.UNUSED,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().plusDays(5),
                policy
        );
        couponRepository.save(coupon1);

        coupon2 = new Coupon(
                Status.USED,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                policy
        );
        couponRepository.save(coupon2);

        memberCoupon1 = new MemberCoupon(1L, coupon1);
        memberCoupon2 = new MemberCoupon(2L, coupon2);

        memberCouponRepository.save(memberCoupon1);
        memberCouponRepository.save(memberCoupon2);
    }

    @DisplayName("회원 ID 와 쿠폰 ID 로 회원쿠폰 조회")
    @Test
    void findByMcMemberIdAndCoupon_Id() {
        Optional<MemberCoupon> result = memberCouponRepository.findByMcMemberIdAndCoupon_Id(1L, coupon1.getId());
        assertTrue(result.isPresent());
        assertEquals(memberCoupon1.getId(), result.get().getId());
    }

    @DisplayName("쿠폰의 ID에 해당되는 회원쿠폰 목록 조회")
    @Test
    void findByCoupon_IdOrderByIdAsc() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MemberCoupon> result = memberCouponRepository.findByCoupon_IdOrderByIdAsc(coupon1.getId(), pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(memberCoupon1.getId(), result.getContent().getFirst().getId());
    }

    @DisplayName("회원 ID에 해당되는 회원쿠폰 목록 조회")
    @Test
    void findByMcMemberIdOrderByIdAsc() {
        Long mcMemberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<MemberCoupon> result = memberCouponRepository.findByMcMemberIdOrderByIdAsc(mcMemberId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(memberCoupon1.getId(), result.getContent().getFirst().getId());
    }

    @DisplayName("회원 ID 와 쿠폰의 Status 로 회원쿠폰 목록 조회")
    @Test
    void findByMcMemberIdAndCoupon_StatusOrderByIdAsc() {
        Long mcMemberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<MemberCoupon> result = memberCouponRepository.findByMcMemberIdAndCoupon_StatusOrderByIdAsc(mcMemberId, Status.UNUSED, pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals(memberCoupon1.getId(), result.getContent().getFirst().getId());
    }

    @DisplayName("회원 ID 와 회원쿠폰 ID 가 존재하는지 확인")
    @Test
    void existsByMcMemberIdAndId() {
        Long mcMemberId = 1L;

        boolean exists = memberCouponRepository.existsByMcMemberIdAndId(mcMemberId, memberCoupon1.getId());
        assertTrue(exists);

        exists = memberCouponRepository.existsByMcMemberIdAndId(1L, memberCoupon2.getId());
        assertFalse(exists);
    }
}
