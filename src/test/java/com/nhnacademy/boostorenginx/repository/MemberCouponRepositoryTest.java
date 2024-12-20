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
    void findMemberCouponByMemberCouponIdOrderByMemberCouponIdAsc() {
        String jpql = "SELECT mc FROM MemberCoupon mc WHERE mc.memberCouponId = :id ORDER BY mc.memberCouponId ASC";
        TypedQuery<MemberCoupon> query = entityManager.createQuery(jpql, MemberCoupon.class);
        query.setParameter("id", 1L);

        MemberCoupon result = query.getSingleResult();
        System.out.printf("MemberCouponId: %d, Coupon: %s %n", result.getMemberCouponId(), result.getCoupon().getCode());

        assertEquals(memberCoupon1.getMemberCouponId(), result.getMemberCouponId());
        assertEquals(memberCoupon1.getCoupon().getCode(), result.getCoupon().getCode());
        assertEquals(memberCoupon1.getCoupon().getStatus(), result.getCoupon().getStatus());
    }

    @DisplayName("특정 회원 ID 에 해당되는 쿠폰 ID 들을 조회")
    @Test
    void findByMcMemberIdOrderByMcMemberIdAsc() {
        String jpql = "SELECT mc FROM MemberCoupon mc WHERE mc.mcMemberId = :id ORDER BY mc.mcMemberId ASC";
        TypedQuery<MemberCoupon> query = entityManager.createQuery(jpql, MemberCoupon.class);
        query.setParameter("id", 1L);
        List<MemberCoupon> results = query.getResultList();
        System.out.println("MemberCoupon: ");
        results.forEach(memberCoupon -> System.out.printf("MemberCouponId: %d, Coupon: %s, Status: %s %n",
                memberCoupon.getMemberCouponId(),
                memberCoupon.getCoupon().getCode(),
                memberCoupon.getCoupon().getStatus().toString()
        ));
        assertEquals(1, results.size());
        assertEquals(memberCoupon1.getMemberCouponId(), results.get(0).getMemberCouponId());
        assertEquals(memberCoupon1.getCoupon().getCode(), results.get(0).getCoupon().getCode());
        assertEquals(memberCoupon1.getCoupon().getStatus(), results.get(0).getCoupon().getStatus());
    }

    @DisplayName("회원 ID 와 회원쿠폰 ID 가 DB 에 존재하는지 확인")
    @Test
    void existsByMcMemberIdAndMemberCouponId() {
        String jpql = "SELECT mc FROM MemberCoupon mc WHERE mc.mcMemberId = :id AND mc.memberCouponId = :couponId";
        TypedQuery<MemberCoupon> query = entityManager.createQuery(jpql, MemberCoupon.class);
        query.setParameter("id", 1L);
        query.setParameter("couponId", 1L);
        boolean exists = query.getResultList().size() > 0;
        System.out.printf("MemberCouponId: %d, Coupon: %s, Status: %s %n",
                memberCoupon1.getMemberCouponId(),
                memberCoupon1.getCoupon().getCode(),
                memberCoupon1.getCoupon().getStatus().toString());
        assertTrue(exists);
        assertEquals(memberCoupon1.getMemberCouponId(), query.getSingleResult().getMemberCouponId());
        assertEquals(memberCoupon1.getCoupon().getCode(), query.getSingleResult().getCoupon().getCode());
        assertEquals(memberCoupon1.getCoupon().getStatus(), query.getSingleResult().getCoupon().getStatus());
    }
}
