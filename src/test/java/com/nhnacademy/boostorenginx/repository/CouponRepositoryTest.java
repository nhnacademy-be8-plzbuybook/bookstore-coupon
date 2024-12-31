package com.nhnacademy.boostorenginx.repository;


import com.nhnacademy.boostorenginx.entity.Coupon;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class CouponRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    private CouponRepository couponRepository;

    private CouponPolicy couponPolicy;
    private Coupon coupon1;
    private Coupon coupon2;
    private Coupon coupon3;
    private String code1;
    private String code2;
    private String code3;

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
        entityManager.persist(couponPolicy);

        coupon1 = new Coupon(
                Status.UNUSED,
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(3),
                couponPolicy
        );

        code1 = coupon1.getCode();
        entityManager.persist(coupon1);

        coupon2 = new Coupon(
                Status.EXPIRED,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(3),
                couponPolicy
        );

        code2 = coupon2.getCode();
        entityManager.persist(coupon2);

        coupon3 = new Coupon(
                Status.UNUSED,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().minusDays(3),
                couponPolicy
        );
        code3 = coupon3.getCode();
        entityManager.persist(coupon3);

        entityManager.flush();
    }

    @DisplayName("쿠폰의 코드로 쿠폰객체 조회 테스트")
    @Test
    void findByCode() {
        String jpql = "SELECT c FROM Coupon c WHERE c.code = :code";
        TypedQuery<Coupon> query = entityManager.createQuery(jpql, Coupon.class);
        query.setParameter("code", code1);

        Coupon result = query.getSingleResult();

        assertEquals(code1, result.getCode());
        assertEquals(Status.UNUSED, result.getStatus());
        assertEquals(couponPolicy, result.getCouponPolicy());
    }

    @DisplayName("만료된 쿠폰들 조회")
    @Test
    void findByExpiredAtBeforeOrderByExpiredAtAsc() {
        String jpql = "SELECT c FROM Coupon c WHERE c.expiredAt < :expiredAt";
        TypedQuery<Coupon> query = entityManager.createQuery(jpql, Coupon.class);
        query.setParameter("expiredAt", LocalDateTime.now());

        List<Coupon> results = query.getResultList();

        results.forEach(coupon -> System.out.println(
                String.format("Code: %s, Status: %s",
                        coupon.getCode(),
                        coupon.getStatus())
        ));

        assertEquals(2, results.size());
        assertEquals(code2, results.get(0).getCode());
        assertEquals(code3, results.get(1).getCode());
    }

    @DisplayName("사용가능한 기간인 쿠폰들 조회")
    @Test
    void findActiveCoupons() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);

        String jpql = "SELECT c FROM Coupon c WHERE :currentDateTime BETWEEN c.issuedAt AND c.expiredAt ORDER BY c.issuedAt ASC";
        TypedQuery<Coupon> query = entityManager.createQuery(jpql, Coupon.class);

        query.setParameter("currentDateTime", LocalDateTime.now().minusDays(1));

        List<Coupon> results = query.getResultList();

        System.out.println("Active Coupon:");
        results.forEach(coupon -> System.out.println(
                String.format("Code: %s, IssuedAt: %s, ExpiredAt: %s, Status: %s",
                        coupon.getCode(),
                        coupon.getIssuedAt(),
                        coupon.getExpiredAt(),
                        coupon.getStatus())
        ));

        assertEquals(1, results.size());
        assertEquals(code1, results.get(0).getCode());
    }

    @DisplayName("쿠폰정책으로 쿠폰들 조회")
    @Test
    void findByCouponPolicyOrderByIdAsc() {
        String jpql = "SELECT c FROM Coupon c WHERE c.couponPolicy = :couponPolicy ORDER BY c.id ASC";

        TypedQuery<Coupon> query = entityManager.createQuery(jpql, Coupon.class);
        query.setParameter("couponPolicy", couponPolicy);

        List<Coupon> results = query.getResultList();

        System.out.println("Coupon Policy: ");
        results.forEach(coupon -> System.out.println(
                String.format("Code: %s, IssuedAt: %s, ExpiredAt: %s, Status: %s",
                        coupon.getCode(),
                        coupon.getIssuedAt(),
                        coupon.getExpiredAt(),
                        coupon.getStatus())
        ));

        assertEquals(3, results.size());
        assertEquals(code1, results.get(0).getCode());
        assertEquals(code2, results.get(1).getCode());
        assertEquals(code3, results.get(2).getCode());
    }

    @DisplayName("상태로 쿠폰조회")
    @Test
    void findByStatusOrderByStatusAsc() {
        String jpql = "SELECT c FROM Coupon c WHERE c.status = :status ORDER BY c.status ASC";

        TypedQuery<Coupon> query = entityManager.createQuery(jpql, Coupon.class);
        query.setParameter("status", Status.UNUSED);

        List<Coupon> results = query.getResultList();

        System.out.println("Coupon Status: ");
        results.forEach(coupon -> System.out.println(
                String.format("Code: %s, Status: %s",
                        coupon.getCode(),
                        coupon.getStatus())
        ));

        assertEquals(2, results.size());
        assertEquals(code1, results.get(0).getCode());
        assertEquals(code3, results.get(1).getCode());
    }

    @DisplayName("만료된 쿠폰의 상태 쿠폰 조회")
    @Test
    void findByExpiredAtBeforeAndStatusOrderByExpiredAtAsc() {
        String jpql = "SELECT c FROM Coupon c WHERE c.expiredAt < :expiredAt AND c.status = :status ORDER BY c.expiredAt ASC";

        TypedQuery<Coupon> query = entityManager.createQuery(jpql, Coupon.class);
        query.setParameter("expiredAt", LocalDateTime.now());
        query.setParameter("status", Status.UNUSED);

        List<Coupon> results = query.getResultList();

        System.out.println("Coupon Status: ");
        results.forEach(coupon -> System.out.println(
                String.format("Code: %s, IssuedAt: %s, ExpiredAt: %s, Status: %s",
                        coupon.getCode(),
                        coupon.getIssuedAt(),
                        coupon.getExpiredAt(),
                        coupon.getStatus())
        ));

        assertEquals(1, results.size());
        assertEquals(code3, results.get(0).getCode());
    }
}
