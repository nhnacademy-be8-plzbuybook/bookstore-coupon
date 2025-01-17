package com.nhnacademy.boostorecoupon.service.impl;

import com.nhnacademy.boostorecoupon.dto.couponpolicy.*;
import com.nhnacademy.boostorecoupon.entity.CouponPolicy;
import com.nhnacademy.boostorecoupon.entity.CouponTarget;
import com.nhnacademy.boostorecoupon.enums.SaleType;
import com.nhnacademy.boostorecoupon.error.NotFoundCouponPolicyException;
import com.nhnacademy.boostorecoupon.repository.CouponPolicyRepository;
import com.nhnacademy.boostorecoupon.repository.CouponTargetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
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

    @Mock
    private CouponPolicy mockCouponPolicy;

    @Mock
    private CouponPolicySaveRequestDto couponPolicySaveRequestDto;

    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        mockCouponPolicy = CouponPolicy.builder()
                .name("test")
                .saleType(SaleType.RATIO)
                .minimumAmount(new BigDecimal("1000"))
                .discountLimit(new BigDecimal("5000"))
                .discountRatio(10)
                .isStackable(true)
                .couponScope("CATEGORY")
                .startDate(now.minusDays(2))
                .endDate(now.plusDays(2))
                .couponActive(true)
                .build();

        couponPolicySaveRequestDto = new CouponPolicySaveRequestDto(
                "test",
                SaleType.RATIO,
                new BigDecimal("10000"),
                new BigDecimal("50000"),
                0,
                true,
                "BOOK",
                now.minusDays(1),
                now.plusDays(1),
                true
        );

    }

    @DisplayName("쿠폰정책 생성")
    @Test
    void createCouponPolicy() {
        when(couponPolicyRepository.save(any(CouponPolicy.class))).thenReturn(mockCouponPolicy);

        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);

        assertEquals("test", couponPolicyResponseDto.name());
        assertEquals(new BigDecimal("1000"), couponPolicyResponseDto.minimumAmount());
        assertEquals(new BigDecimal("5000"), couponPolicyResponseDto.discountLimit());

        verify(couponPolicyRepository, times(1)).save(any(CouponPolicy.class));
    }

    @DisplayName("이름으로 쿠폰정책 찾기")
    @Test
    void findByName() {
        CouponPolicyNameRequestDto couponPolicyNameRequestDto = new CouponPolicyNameRequestDto("test");

        when(couponPolicyRepository.findByName("test")).thenReturn(Optional.of(mockCouponPolicy));

        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.findByName(couponPolicyNameRequestDto);

        assertEquals("test", couponPolicyResponseDto.name());
        verify(couponPolicyRepository, times(1)).findByName("test");
    }

    @DisplayName("이름으로 쿠폰정책 찾기 실패")
    @Test
    void findByName_NotFoundCouponPolicy() {
        String name = "test";
        CouponPolicyNameRequestDto couponPolicyNameRequestDto = new CouponPolicyNameRequestDto(name);

        when(couponPolicyRepository.findByName(name)).thenReturn(Optional.empty());

        NotFoundCouponPolicyException exception = assertThrows(NotFoundCouponPolicyException.class,
                () -> couponPolicyService.findByName(couponPolicyNameRequestDto));

        assertEquals("이름에 해당하는 CouponPolicy 를 찾을 수 없습니다: test", exception.getMessage());
    }

    @DisplayName("ID 로 쿠폰정책 찾기")
    @Test
    void findById_Success() {
        CouponPolicyIdRequestDto couponPolicyIdRequestDto = new CouponPolicyIdRequestDto(1L);

        when(couponPolicyRepository.findById(1L)).thenReturn(Optional.of(mockCouponPolicy));

        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.findById(couponPolicyIdRequestDto);

        assertEquals("test", couponPolicyResponseDto.name());
        verify(couponPolicyRepository, times(1)).findById(1L);
    }

    @DisplayName("ID 로 쿠폰정책 찾기 실패")
    @Test
    void findById_NotFoundCouponPolicy() {
        CouponPolicyIdRequestDto couponPolicyIdRequestDto = new CouponPolicyIdRequestDto(1L);

        when(couponPolicyRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundCouponPolicyException exception = assertThrows(NotFoundCouponPolicyException.class,
                () -> couponPolicyService.findById(couponPolicyIdRequestDto));

        assertEquals("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: 1", exception.getMessage());
    }

    @DisplayName("활성화된 쿠폰정책 찾기")
    @Test
    void findActiveCouponPolicy() {
        Page<CouponPolicy> activePolicies = new PageImpl<>(Collections.singletonList(mockCouponPolicy));
        CouponPolicyActiveRequestDto couponPolicyActiveRequestDto = new CouponPolicyActiveRequestDto(true, 0, 10);
        Pageable pageable = PageRequest.of(couponPolicyActiveRequestDto.page(), couponPolicyActiveRequestDto.pageSize());

        when(couponPolicyRepository.findByCouponActiveOrderByIdAsc(true, pageable)).thenReturn(activePolicies);

        Page<CouponPolicyResponseDto> result = couponPolicyService.findActiveCouponPolicy(couponPolicyActiveRequestDto);

        assertEquals(1, result.getTotalElements());
        assertEquals("test", result.getContent().get(0).name());
        verify(couponPolicyRepository, times(1)).findByCouponActiveOrderByIdAsc(true, pageable);
    }

    @DisplayName("쿠폰정책에 쿠폰대상 추가")
    @Test
    void addTargetToPolicy() {
        CouponTargetAddRequestDto couponTargetAddRequestDto = new CouponTargetAddRequestDto(1L, 10L);

        when(couponPolicyRepository.findById(1L)).thenReturn(Optional.of(mockCouponPolicy));

        couponPolicyService.addTargetToPolicy(couponTargetAddRequestDto);

        verify(couponPolicyRepository, times(1)).findById(1L);
        verify(couponTargetRepository, times(1)).save(any(CouponTarget.class));
    }

    @DisplayName("쿠폰정책을 찾을수 없는 경우")
    @Test
    void addTargetToPolicy_NotFoundPolicy() {
        CouponTargetAddRequestDto couponTargetAddRequestDto = new CouponTargetAddRequestDto(1L, 10L);

        when(couponPolicyRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundCouponPolicyException exception = assertThrows(NotFoundCouponPolicyException.class,
                () -> couponPolicyService.addTargetToPolicy(couponTargetAddRequestDto));

        assertEquals("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: 1", exception.getMessage());
    }

}