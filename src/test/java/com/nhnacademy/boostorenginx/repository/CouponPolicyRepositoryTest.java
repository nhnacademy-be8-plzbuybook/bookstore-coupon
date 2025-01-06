package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.SaleType;
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
import static org.mockito.Mockito.when;

@DataJpaTest
class CouponPolicyRepositoryTest {

    @PersistenceContext
    private EntityManager entityManager;

    private CouponPolicy policy1;
    private CouponPolicy policy2;
    private CouponPolicy policy3;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        policy1 = CouponPolicy.builder()
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

        policy2 = CouponPolicy.builder()
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

        policy3 = CouponPolicy.builder()
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

        entityManager.persist(policy1);
        entityManager.persist(policy2);
        entityManager.persist(policy3);
        entityManager.flush();
    }

    @DisplayName("CouponActive 가 true 인 쿠폰정책 조회")
    @Test
    void findByCouponActive() {
        String jpql = "SELECT c FROM CouponPolicy c WHERE c.couponActive = :active";

        TypedQuery<CouponPolicy> query = entityManager.createQuery(jpql, CouponPolicy.class);
        query.setParameter("active", true);
        List<CouponPolicy> results = query.getResultList();

        System.out.println("(couponActive = true)인 CouponPolicy: ");

        results.forEach(couponPolicy -> System.out.printf("Name: %s, SaleType: %s%n",
                couponPolicy.getName(),
                couponPolicy.getSaleType()));

        assertEquals(2, results.size());
        assertEquals("test1", results.get(0).getName());
        assertEquals("test2", results.get(1).getName());
    }

    @DisplayName("name 에 해당되는 쿠폰정책 조회")
    @Test
    void findByName() {
        String jpql = "SELECT c FROM CouponPolicy c WHERE c.name = :name";
        TypedQuery<CouponPolicy> query = entityManager.createQuery(jpql, CouponPolicy.class);
        query.setParameter("name", "test1");

        CouponPolicy result = query.getSingleResult();
        System.out.printf("Name: %s %n", result.getName());

        assertEquals("test1", result.getName());
    }

}