package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.SaleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CouponPolicyRepositoryTest {

    @Mock
    CouponPolicyRepository couponPolicyRepository;

    private CouponPolicy policy1;
    private CouponPolicy policy2;

    @BeforeEach
    void setUp() {
        policy1 = CouponPolicy.builder()
                .name("Policy1")
                .saleType(SaleType.AMOUNT)
                .minimumAmount(new BigDecimal("1000"))
                .discountLimit(new BigDecimal("5000"))
                .discountRatio(0)
                .isStackable(true)
                .couponScope("book")
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .couponActive(true)
                .build();

        policy2 = CouponPolicy.builder()
                .name("Policy2")
                .saleType(SaleType.RATIO)
                .minimumAmount(new BigDecimal("2000"))
                .discountLimit(new BigDecimal("7000"))
                .discountRatio(0)
                .isStackable(false)
                .couponScope("category")
                .startDate(LocalDateTime.now().minusDays(2))
                .endDate(LocalDateTime.now().plusDays(5))
                .couponActive(true)
                .build();
    }

    @Test
    void findByCouponActiveTest() {
        when(couponPolicyRepository.findByCouponActive(true)).thenReturn(Arrays.asList(policy1, policy2));
        List<CouponPolicy> activePolicies = couponPolicyRepository.findByCouponActive(true);
        assertThat(activePolicies).hasSize(2);
        assertThat(activePolicies).extracting("name").containsExactly("Policy1", "Policy2");
    }

    @Test
    void findByNameTest() {
        when(couponPolicyRepository.findByName("Policy1")).thenReturn(Optional.of(policy1));
        Optional<CouponPolicy> policy = couponPolicyRepository.findByName("Policy1");
        assertThat(policy).isPresent();
        assertThat(policy.get().getName()).isEqualTo("Policy1");
    }

    @Test
    void findByMinimumAmountLessThanEqualTest() {
        when(couponPolicyRepository.findByMinimumAmountLessThanEqual(new BigDecimal("1000"))).thenReturn(Collections.singletonList(policy1));
        List<CouponPolicy> policyList = couponPolicyRepository.findByMinimumAmountLessThanEqual(new BigDecimal("1000"));
        assertThat(policyList).hasSize(1);
        assertThat(policyList).extracting("name").containsExactly("Policy1");
    }

    @Test
    void findAllByTest() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<CouponPolicy> mockPage = new PageImpl<>(Arrays.asList(policy1, policy2), pageable, 2);
        when(couponPolicyRepository.findAllBy(pageable)).thenReturn(mockPage);
        Page<CouponPolicy> page = couponPolicyRepository.findAllBy(pageable);
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent()).extracting("name").containsExactly("Policy1","Policy2");
    }
}
