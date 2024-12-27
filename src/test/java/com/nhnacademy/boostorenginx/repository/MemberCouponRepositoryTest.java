package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.MemberCoupon;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.enums.Status;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberCouponRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

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
        entityManager.persist(policy);

        coupon1 = new Coupon(
                Status.UNUSED,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().plusDays(5),
                policy
        );
        entityManager.persist(coupon1);

        coupon2 = new Coupon(
                Status.USED,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                policy
        );
        entityManager.persist(coupon2);

        memberCoupon1 = new MemberCoupon(
                1L,
                coupon1
        );
        entityManager.persist(memberCoupon1);

        memberCoupon2 = new MemberCoupon(
                2L,
                coupon2
        );
        entityManager.persist(memberCoupon2);

        entityManager.flush();
    }

    @DisplayName("ID 로 회원쿠폰 조회")
    @Test
    void findByMcMemberIdOrderByIdAsc() {
        Pageable pageable = PageRequest.of(0, 10);

        String jpql = "SELECT mc FROM MemberCoupon mc WHERE mc.mcMemberId = :memberId ORDER BY mc.id ASC";
        TypedQuery<MemberCoupon> query = entityManager.createQuery(jpql, MemberCoupon.class);
        query.setParameter("memberId", 1L);
        List<MemberCoupon> results = query.getResultList();

        Page<MemberCoupon> page = new PageImpl<>(results, pageable, results.size());

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        assertEquals(memberCoupon1.getMcMemberId(), page.getContent().get(0).getMcMemberId());
    }

    @DisplayName("회원 ID 에 발급된 쿠폰들을 조회")
    @Test
    void findByCoupon_IdOrderByIdAsc() {
        Pageable pageable = PageRequest.of(0, 10);

        String jpql = "SELECT mc FROM MemberCoupon mc WHERE mc.coupon.id = :couponId ORDER BY mc.id ASC";
        TypedQuery<MemberCoupon> query = entityManager.createQuery(jpql, MemberCoupon.class);
        query.setParameter("couponId", coupon1.getId());
        List<MemberCoupon> results = query.getResultList();

        Page<MemberCoupon> page = new PageImpl<>(results, pageable, results.size());

        assertNotNull(page);
        assertEquals(1, page.getContent().size());
        assertEquals(memberCoupon1.getCoupon().getId(), page.getContent().get(0).getCoupon().getId());
    }

    @DisplayName("회원 ID 와 회원쿠폰 ID 가 존재하는지 확인")
    @Test
    void existsByMcMemberIdAndId() {
        String jpql = "SELECT COUNT(mc) FROM MemberCoupon mc WHERE mc.mcMemberId = :memberId AND mc.id = :memberCouponId";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        query.setParameter("memberId", 1L);
        query.setParameter("memberCouponId", memberCoupon1.getId());
        Long count = query.getSingleResult();

        assertTrue(count > 0);

        query.setParameter("memberId", 3L);
        query.setParameter("memberCouponId", memberCoupon1.getId());
        count = query.getSingleResult();

        assertFalse(count > 0);
    }
}
