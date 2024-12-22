package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyIdRequestDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyNameRequestDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.repository.CouponPolicyRepository;
import com.nhnacademy.boostorenginx.repository.CouponTargetRepository;
import com.nhnacademy.boostorenginx.service.CouponPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CouponPolicyServiceImplTest {

    @InjectMocks
    private CouponPolicyServiceImpl couponPolicyService;

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @Mock
    private CouponTargetRepository couponTargetRepository;

    private CouponPolicy couponPolicy;
    LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {

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
    }

    @DisplayName("쿠폰정책 생성 서비스 테스트")
    @Test
    void createCouponPolicy() {
        CouponPolicySaveRequestDto requestDto = new CouponPolicySaveRequestDto(
                "test",
                SaleType.RATIO,
                new BigDecimal("1000"),
                new BigDecimal("5000"),
                10,
                true,
                "Category",
                now.minusDays(2),
                now.plusDays(2),
                true
        );

        CouponPolicy mockPolicy = mock(CouponPolicy.class);
        when(mockPolicy.getId()).thenReturn(1L);
        when(couponPolicyRepository.save(any(CouponPolicy.class))).thenReturn(mockPolicy);
        Long result = couponPolicyService.createCouponPolicy(requestDto);

        assertEquals(1L, result);
        verify(couponPolicyRepository, times(1)).save(any(CouponPolicy.class));
    }

    @DisplayName("이름으로 쿠폰정책 찾기")
    @Test
    void findByName() {
        CouponPolicyNameRequestDto requestDto = new CouponPolicyNameRequestDto("test");

        CouponPolicy mockPolicy = mock(CouponPolicy.class);
        when(couponPolicyRepository.findByName(anyString())).thenReturn(Optional.of(mockPolicy));
        Optional<CouponPolicy> result = couponPolicyService.findByName(requestDto);

        assertEquals(mockPolicy, result.get());
        verify(couponPolicyRepository, times(1)).findByName(anyString());
    }

    @DisplayName("ID 로 쿠폰정책 찾기")
    @Test
    void findById() {
        CouponPolicyIdRequestDto requestDto = new CouponPolicyIdRequestDto(1L);
        CouponPolicy mockPolicy = mock(CouponPolicy.class);
        when(couponPolicyRepository.findById(anyLong())).thenReturn(Optional.of(mockPolicy));
        Optional<CouponPolicy> result = couponPolicyService.findById(requestDto);

        assertEquals(mockPolicy, result.get());
        verify(couponPolicyRepository, times(1)).findById(anyLong());
    }

    // 테스트 나중에 작성하기
//    @DisplayName("쿠폰정책으로 쿠폰대상 만들기")
//    @Test
//    void addTargetToPolicy_Success() {
//        CouponTargetAddRequestDto requestDto = new CouponTargetAddRequestDto(1L, 1L);
//
//        when(couponPolicyRepository.findById(1L)).thenReturn(Optional.of(couponPolicy));
//        when(couponTargetRepository.save(any(CouponTarget.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        couponPolicyService.addTargetToPolicy(requestDto);
//
//        verify(couponPolicyRepository, times(1)).findById(1L);
//        verify(couponTargetRepository, times(1)).save(any(CouponTarget.class));
//    }
}