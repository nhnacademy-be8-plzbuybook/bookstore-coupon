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


    @DisplayName("쿠폰 ID 로 쿠폰변경이력 리스트 조회")
    @Test
    void findByCoupon_idOrderByCouponIdAsc() {
        String jpql = "SELECT c FROM CouponHistory c WHERE c.coupon.id = :id ORDER BY c.id ASC";

        TypedQuery<CouponHistory> query = entityManager.createQuery(jpql, CouponHistory.class);
        query.setParameter("id", coupon.getId());
        List<CouponHistory> results = query.getResultList();

        System.out.println("CouponHistory:");
        results.forEach(history -> System.out.println(
                String.format("Id: %d, Status: %s, ChangeDate: %s, Reason: %s",
                        history.getId(),
                        history.getStatus(),
                        history.getChangeDate(),
                        history.getReason())
        ));

        assertEquals(2, results.size(), "결과 리스트 크기가 예상과 다름");
        assertEquals(results.get(0).getId(), results.get(0).getId(), "첫 번째 CouponHistory ID 불일치");
        assertEquals(results.get(1).getId(), results.get(1).getId(), "두 번째 CouponHistory ID 불일치");
    }

    @DisplayName("CouponHistory 의 Status 에 해당하는 리스트 조회")
    @Test
    void findByStatusOrderByChangeDateAsc() {
        String jpql = "SELECT c FROM CouponHistory c WHERE c.status = :status ORDER BY c.changeDate ASC";

        TypedQuery<CouponHistory> query = entityManager.createQuery(jpql, CouponHistory.class);
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

    @DisplayName("특정 기간동안의 CouponHistory 조회")
    @Test
    void findChangeDate() {
        String jpql = "SELECT c FROM CouponHistory c WHERE c.changeDate BETWEEN :startDate AND :endDate";
        TypedQuery<CouponHistory> query = entityManager.createQuery(jpql, CouponHistory.class);
        LocalDateTime startDate = LocalDateTime.now().minusDays(10);
        LocalDateTime endDate = LocalDateTime.now().plusDays(10);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List<CouponHistory> results = query.getResultList();
        System.out.println("CouponHistory: ");
        results.forEach(history -> System.out.printf("Id: %d, Status: %s, ChangeDate: %s, Reason: %s%n",
                history.getId(),
                history.getStatus(),
                history.getChangeDate(),
                history.getReason()));
        assertEquals(2, results.size(), "결과 리스트 크기가 예상과 다릅니다");
        assertEquals(history1.getId(), results.get(0).getId(), "첫 번째 CouponHistory ID 불일치");
        assertEquals(history2.getId(), results.get(1).getId(), "두 번째 CouponHistory ID 불일치");
    }
}
