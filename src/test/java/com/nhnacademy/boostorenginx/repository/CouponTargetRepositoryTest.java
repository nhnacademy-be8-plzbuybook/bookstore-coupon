//package com.nhnacademy.boostorenginx.repository;
//
//import com.nhnacademy.boostorenginx.entity.CouponPolicy;
//import com.nhnacademy.boostorenginx.entity.CouponTarget;
//import com.nhnacademy.boostorenginx.enums.SaleType;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.TypedQuery;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class CouponTargetRepositoryTest {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    private CouponPolicy couponPolicy;
//
//    private CouponTarget couponTarget1;
//    private CouponTarget couponTarget2;
//
//    @BeforeEach
//    void setUp() {
//        LocalDateTime now = LocalDateTime.now();
//
//        couponPolicy = CouponPolicy.builder()
//                .name("test")
//                .saleType(SaleType.AMOUNT)
//                .minimumAmount(new BigDecimal("1000"))
//                .discountLimit(new BigDecimal("5000"))
//                .discountRatio(0)
//                .isStackable(true)
//                .couponScope("book")
//                .startDate(now.minusDays(1))
//                .endDate(now.plusDays(1))
//                .couponActive(true)
//                .build();
//        entityManager.persist(couponPolicy);
//
//        couponTarget1 = CouponTarget.builder()
//                .targetId(0L)
//                .build();
//        couponTarget1.setCouponPolicy(couponPolicy);
//
//        couponTarget2 = CouponTarget.builder()
//                .targetId(1L)
//                .build();
//        couponTarget2.setCouponPolicy(couponPolicy);
//
//        entityManager.persist(couponTarget1);
//        entityManager.persist(couponTarget2);
//        entityManager.flush();
//    }
//
//    @DisplayName("쿠폰대상 ID 로 쿠폰대상 조회")
//    @Test
//    void findByTargetId() {
//        String jpql = "SELECT c FROM CouponTarget c WHERE c.targetId = :targetId";
//
//        TypedQuery<CouponTarget> query = entityManager.createQuery(jpql, CouponTarget.class);
//        query.setParameter("targetId", 1L);
//
//        Optional<CouponTarget> result = query.getResultList().stream().findFirst();
//
//        System.out.println("CouponTarget: ");
//        result.ifPresent(couponTarget -> System.out.println(couponTarget.getTargetId()));
//
//        assertTrue(result.isPresent());
//        assertEquals(1L, result.get().getTargetId());
//    }
//
//    @DisplayName("특정 쿠폰정책에 속한 쿠폰대상 조회")
//    @Test
//    void findByCouponPolicy_Id() {
//        String jpql = "SELECT c FROM CouponTarget c WHERE c.couponPolicy.id = :id";
//
//        TypedQuery<CouponTarget> query = entityManager.createQuery(jpql, CouponTarget.class);
//        query.setParameter("id", couponPolicy.getId());
//
//        List<CouponTarget> results = query.getResultList();
//
//        System.out.println("CouponTarget: ");
//        results.forEach(couponTarget -> System.out.println(
//                String.format("CouponPolicy: %s, CouponTarget: %d",
//                        couponPolicy.getName(),
//                        couponTarget.getTargetId())
//        ));
//
//        assertEquals(2, results.size());
//        assertEquals(0L, results.get(0).getTargetId());
//        assertEquals(1L, results.get(1).getTargetId());
//    }
//
//    @DisplayName("특정 쿠폰정책을 속하는 쿠폰대상을 페이징")
//    @Test
//    void findByCouponPolicy() {
//        String jpql = "SELECT c FROM CouponTarget c WHERE c.couponPolicy = :couponPolicy";
//        TypedQuery<CouponTarget> query = entityManager.createQuery(jpql, CouponTarget.class);
//        query.setParameter("couponPolicy", couponPolicy);
//
//        int page = 0;
//        int size = 2;
//        query.setFirstResult(page * size);
//        query.setMaxResults(size);
//
//        List<CouponTarget> results = query.getResultList();
//        results.forEach(couponTarget -> System.out.printf(
//                "CouponPolicy: %s, CouponTarget: %d%n",
//                couponPolicy.getName(),
//                couponTarget.getTargetId()
//        ));
//
//        assertEquals(2, results.size());
//    }
//}
