package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class CouponTargetTest {

    @Mock
    CouponTargetRepository couponTargetRepository;

    private CouponPolicy couponPolicy;
    private CouponTarget couponTarget1;
    private CouponTarget couponTarget2;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicy.builder()
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

        couponTarget1 = CouponTarget.builder().targetId(1L).build();
        couponTarget1.setCouponPolicy(couponPolicy);

        couponTarget2 = CouponTarget.builder().targetId(2L).build();
        couponTarget2.setCouponPolicy(couponPolicy);
    }

    @Test
    void findByCouponPolicy_IdTest() {
        when(couponTargetRepository.findByCouponPolicy_Id(couponPolicy.getId())).thenReturn(Arrays.asList(couponTarget1, couponTarget2));
        List<CouponTarget> targetList = couponTargetRepository.findByCouponPolicy_Id(couponPolicy.getId());
        assertThat(targetList).hasSize(2);
        assertThat(targetList).extracting("targetId").containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void findByTargetId() {
        when(couponTargetRepository.findByTargetId(1L)).thenReturn(Optional.of(couponTarget1));
        Optional<CouponTarget> couponTarget = couponTargetRepository.findByTargetId(1L);
        assertThat(couponTarget).isPresent();
        assertThat(couponTarget.get().getTargetId()).isEqualTo(1L);
    }

    @Test
    void findByCouponPolicy() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<CouponTarget> mockPage = new PageImpl<>(List.of(couponTarget1), pageable, 2);
        when(couponTargetRepository.findByCouponPolicy(couponPolicy, pageable)).thenReturn(mockPage);
        Page<CouponTarget> page = couponTargetRepository.findByCouponPolicy(couponPolicy, pageable);
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent()).extracting("targetId").containsExactly(1L);
        assertThat(page.getTotalElements()).isEqualTo(2);
    }
}
