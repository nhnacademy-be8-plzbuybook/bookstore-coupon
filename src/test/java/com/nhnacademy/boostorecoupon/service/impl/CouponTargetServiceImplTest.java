package com.nhnacademy.boostorecoupon.service.impl;

import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetSearchRequestDto;
import com.nhnacademy.boostorecoupon.entity.CouponPolicy;
import com.nhnacademy.boostorecoupon.entity.CouponTarget;
import com.nhnacademy.boostorecoupon.repository.CouponPolicyRepository;
import com.nhnacademy.boostorecoupon.repository.CouponTargetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CouponTargetServiceImplTest {

    @InjectMocks
    private CouponTargetServiceImpl couponTargetService;

    @Mock
    private CouponTargetRepository couponTargetRepository;

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @Mock
    private CouponTarget mockTarget;

    @Mock
    private CouponPolicy mockPolicy;

    @BeforeEach
    void setUp() {
        mockPolicy = mock(CouponPolicy.class);
    }

    @DisplayName("쿠폰정책 ID 로 쿠폰대상 목록 조회 ")
    @Test
    void getCouponTargetsByPolicyId() {
        mockTarget = CouponTarget.builder()
                .ctTargetId(1001L)
                .couponPolicy(mockPolicy)
                .build();

        Long policyId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        CouponTargetSearchRequestDto requestDto = new CouponTargetSearchRequestDto(policyId, 0, 10);

        Page<CouponTarget> mockPage = new PageImpl<>(Collections.singletonList(mockTarget));
        when(couponTargetRepository.findByCouponPolicy_IdOrderByIdAsc(policyId, pageable)).thenReturn(mockPage);

        Page<CouponTargetGetResponseDto> result = couponTargetService.getCouponTargetsByPolicyId(requestDto);

        assertEquals(1, result.getTotalElements());
        assertEquals(mockTarget.getId(), result.getContent().getFirst().couponTargetId());
        assertEquals(mockTarget.getCtTargetId(), result.getContent().getFirst().ctTargetId());
        assertEquals(mockPolicy.getId(), result.getContent().getFirst().couponPolicyId());
        assertEquals(mockPolicy.getCouponScope(), result.getContent().getFirst().scope());

        verify(couponTargetRepository, times(1)).findByCouponPolicy_IdOrderByIdAsc(policyId, pageable);
    }
}