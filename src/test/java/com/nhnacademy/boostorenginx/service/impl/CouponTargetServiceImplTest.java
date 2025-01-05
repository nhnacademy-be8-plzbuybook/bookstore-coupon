package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetSaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetSearchRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import com.nhnacademy.boostorenginx.error.CouponTargetException;
import com.nhnacademy.boostorenginx.error.NotFoundCouponPolicyException;
import com.nhnacademy.boostorenginx.repository.CouponPolicyRepository;
import com.nhnacademy.boostorenginx.repository.CouponTargetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        mockPolicy = CouponPolicy.builder()
                .name("Test Policy")
                .build();
    }

    @DisplayName("쿠폰대상 생성")
    @Test
    void createCouponTarget() {
        Long policyId = 1L;
        Long targetId = 1L;
        CouponTargetSaveRequestDto requestDto = new CouponTargetSaveRequestDto(policyId, targetId);

        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.of(mockPolicy));
        when(couponTargetRepository.existsByCtTargetId(targetId)).thenReturn(false);

        mockTarget = CouponTarget.builder()
                .couponPolicy(mockPolicy)
                .ctTargetId(targetId)
                .build();

        when(couponTargetRepository.save(any(CouponTarget.class))).thenReturn(mockTarget);

        CouponTargetResponseDto responseDto = couponTargetService.createCouponTarget(requestDto);

        assertEquals(targetId, responseDto.ctTargetId());

        verify(couponPolicyRepository, times(1)).findById(policyId);
        verify(couponTargetRepository, times(1)).existsByCtTargetId(targetId);
        verify(couponTargetRepository, times(1)).save(any(CouponTarget.class));
    }

    @DisplayName("쿠폰정책 ID 에 해당되는 값이 없어 예외발생")
    @Test
    void createCouponTarget_NotFoundPolicy() {
        Long policyId = 1L;
        Long targetId = 1L;
        CouponTargetSaveRequestDto requestDto = new CouponTargetSaveRequestDto(policyId, targetId);
        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.empty());
        NotFoundCouponPolicyException exception = assertThrows(NotFoundCouponPolicyException.class,
                () -> couponTargetService.createCouponTarget(requestDto));
        assertEquals("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + policyId, exception.getMessage());
    }

    @DisplayName("쿠폰대상 ID 가 이미 존재하는 경우")
    @Test
    void createCouponTarget_AlreadyExistTarget() {
        Long policyId = 1L;
        Long targetId = 1L;
        CouponTargetSaveRequestDto requestDto = new CouponTargetSaveRequestDto(policyId, targetId);

        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.of(mockPolicy));
        when(couponTargetRepository.existsByCtTargetId(targetId)).thenReturn(true);

        CouponTargetException exception = assertThrows(CouponTargetException.class,
                () -> couponTargetService.createCouponTarget(requestDto));

        assertEquals("이미 등록된 쿠폰 대상 입니다", exception.getMessage());

        verify(couponPolicyRepository, times(1)).findById(policyId);
        verify(couponTargetRepository, times(1)).existsByCtTargetId(targetId);
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