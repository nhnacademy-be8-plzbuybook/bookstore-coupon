package com.nhnacademy.boostorenginx.repository;


import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class CouponHistoryRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    private Coupon coupon;
    private CouponPolicy policy;
    private CouponHistory history1;
    private CouponHistory history2;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        policy = CouponPolicy.builder()
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
        entityManager.persist(policy);

        coupon = new Coupon(
                Status.UNUSED,
                now.minusDays(5),
                now.plusDays(5),
                policy
        );
        entityManager.persist(coupon);

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
        entityManager.persist(history1);
        entityManager.persist(history2);
        entityManager.flush();
    }

    @DisplayName("쿠폰으로 쿠폰변경이력 리스트 조회")
    @Test
    void findByCoupon() {
        String jpql = "SELECT c FROM CouponHistory c WHERE c.coupon = :coupon";

        TypedQuery<CouponHistory> query = entityManager.createQuery(jpql, CouponHistory.class);
        query.setParameter("coupon", coupon);
        List<CouponHistory> results = query.getResultList();

        System.out.println("CouponHistory: ");

        results.forEach(history -> System.out.println(
                String.format("Id: %d, Status: %s, ChangeDate: %s, Reason: %s",
                        history.getId(),
                        history.getStatus(),
                        history.getChangeDate(),
                        history.getReason())
        ));

        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals(2L, results.get(1).getId());
    }

    @DisplayName("쿠폰 ID 로 쿠폰변경이력 리스트 조회")
    @Test
    void findByCoupon_id() {
        String jpql = "SELECT c FROM CouponHistory c WHERE c.coupon.id = :id";

        TypedQuery<CouponHistory> query = entityManager.createQuery(jpql, CouponHistory.class);
        query.setParameter("id", coupon.getId());
        List<CouponHistory> results = query.getResultList();

        System.out.println("CouponHistory: ");

        results.forEach(history -> System.out.println(
                String.format("Id: %d, Status: %s, ChangeDate: %s, Reason: %s",
                        history.getId(),
                        history.getStatus(),
                        history.getChangeDate(),
                        history.getReason())
        ));

        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals(2L, results.get(1).getId());
    }

    @DisplayName("")
    @Test
    void findByCouponAndStatus() {
        String jpql = "SELECT c FROM CouponHistory c WHERE c.coupon = :coupon AND c.status = :status";

        TypedQuery<CouponHistory> query = entityManager.createQuery(jpql, CouponHistory.class);
        query.setParameter("coupon", coupon);
        query.setParameter("status", Status.USED);

        List<CouponHistory> results = query.getResultList();

        System.out.println("CouponHistory: ");

        results.forEach(history -> System.out.println(
                String.format("Id: %d, Status: %s, ChangeDate: %s, Reason: %s",
                        history.getId(),
                        history.getStatus(),
                        history.getChangeDate(),
                        history.getReason())
        ));

        assertEquals(1, results.size());
        assertEquals(1L, results.get(0).getId());
    }
}
