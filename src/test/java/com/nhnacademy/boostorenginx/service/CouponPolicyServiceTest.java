package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.error.NotFoundCouponPolicyException;
import com.nhnacademy.boostorenginx.repository.CouponPolicyRepository;
import com.nhnacademy.boostorenginx.repository.CouponTargetRepository;
import com.nhnacademy.boostorenginx.service.impl.CouponPolicyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponPolicyServiceTest {

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @Mock
    private CouponTargetRepository couponTargetRepository;

    @InjectMocks
    private CouponPolicyServiceImpl couponPolicyService;

    private CouponPolicySaveRequestDto requestDto;
    private CouponPolicy mockPolicy;
    private List<Long> targetIdList;
    private List<CouponTarget> mockTargets;

    @BeforeEach
    void setUp() {
        requestDto = new CouponPolicySaveRequestDto(
                "test",
                SaleType.AMOUNT,
                BigDecimal.valueOf(1000),
                BigDecimal.valueOf(5000),
                0,
                true,
                "BOOK",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(10),
                true
        );

        mockPolicy = CouponPolicy.builder()
                .name(requestDto.name())
                .saleType(requestDto.saleType())
                .minimumAmount(requestDto.minimumAmount())
                .discountLimit(requestDto.discountLimit())
                .discountRatio(requestDto.discountRatio())
                .isStackable(requestDto.isStackable())
                .couponScope(requestDto.couponScope())
                .startDate(requestDto.startDate())
                .endDate(requestDto.endDate())
                .couponActive(requestDto.couponActive())
                .build();

        targetIdList = List.of(1L, 2L);

        mockTargets = targetIdList.stream().map(targetId -> {
            CouponTarget couponTarget = CouponTarget.builder().targetId(targetId).build();
            couponTarget.setCouponPolicy(mockPolicy);
            return couponTarget;
        }).toList();
    }

    @Test
    void createCouponPolicyTest() {
        when(couponPolicyRepository.save(Mockito.any(CouponPolicy.class))).thenReturn(mockPolicy);
        Long result = couponPolicyService.createCouponPolicy(requestDto);
        assertThat(result).isEqualTo(mockPolicy.getId());
        verify(couponPolicyRepository, times(1)).save(Mockito.any(CouponPolicy.class));
    }

    @Test
    void createCouponPolicyTest_ShouldThrowException_WhenRequestDtoIsNull() {
        NotFoundCouponPolicyException exception = assertThrows(NotFoundCouponPolicyException.class,
                () -> couponPolicyService.createCouponPolicy(null));
        assertThat(exception.getMessage()).isEqualTo("요청 Dto 가 null 입니다");
    }

    @Test
    void addCouponTargetListTest() {
        when(couponPolicyRepository.findById(mockPolicy.getId())).thenReturn(Optional.of(mockPolicy));
        couponPolicyService.addCouponTargetList(mockPolicy.getId(), targetIdList);
        verify(couponPolicyRepository, times(1)).findById(mockPolicy.getId());
        verify(couponTargetRepository, times(1)).saveAll(anyList());
    }

    @Test
    void addCouponTargetListTest_ShouldThrowException_WhenCouponPolicyNotFound() {
        when(couponPolicyRepository.findById(mockPolicy.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundCouponPolicyException.class,
                () -> couponPolicyService.addCouponTargetList(mockPolicy.getId(), targetIdList));
        verify(couponPolicyRepository, times(1)).findById(mockPolicy.getId());
    }

    @Test
    void findByNameTest() {
        String name = "test";
        when(couponPolicyRepository.findByName(name)).thenReturn(Optional.of(mockPolicy));
        assertThat(couponPolicyService.findByName(name).getName()).isEqualTo(name);
        verify(couponPolicyRepository, times(1)).findByName(name);
    }

    @Test
    void findByNameShouldThrowExceptionTest() {
        String name = "test";
        when(couponPolicyRepository.findByName(name)).thenReturn(Optional.empty());
        assertThrows(NotFoundCouponPolicyException.class, () -> couponPolicyService.findByName(name));
        verify(couponPolicyRepository, times(1)).findByName(name);
    }
}
