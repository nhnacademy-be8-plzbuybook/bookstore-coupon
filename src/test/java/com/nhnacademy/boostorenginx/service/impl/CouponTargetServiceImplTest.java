package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.repository.CouponTargetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponTargetServiceImplTest {

    @InjectMocks
    private CouponTargetServiceImpl couponTargetService;

    @Mock
    private CouponTargetRepository couponTargetRepository;

    @Mock
    private CouponTarget couponTarget;
    @Mock
    private CouponPolicy couponPolicy;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        couponPolicy = CouponPolicy.builder()
                .name("test")
                .saleType(SaleType.RATIO)
                .minimumAmount(new BigDecimal("1000"))
                .discountLimit(new BigDecimal("5000"))
                .discountRatio(10)
                .isStackable(true)
                .couponScope("Category")
                .startDate(now.minusDays(2))
                .endDate(now.plusDays(2))
                .couponActive(true)
                .build();

        Long targetId = 1L; // 테스트용 쿠폰적용대상 ID

        couponTarget = CouponTarget.builder()
                .couponPolicy(couponPolicy)
                .targetId(targetId)
                .build();

    }

//    @Test
//    void createCouponTarget() {
//        CouponTargetAddRequestDto requestDto = new CouponTargetAddRequestDto(1L, 1L);
//
//        CouponTarget mockTarget = mock(CouponTarget.class);
//        when(mockTarget.getTargetId()).thenReturn(1L);
//        when(couponTargetRepository.save(any(CouponTarget.class))).thenReturn(mockTarget);
//        couponTargetService.createCouponTarget(requestDto);
//        assertEquals(1L, mockTarget.getTargetId());
//        verify(couponTargetRepository, times(1)).save(any(CouponTarget.class));
//    }
}