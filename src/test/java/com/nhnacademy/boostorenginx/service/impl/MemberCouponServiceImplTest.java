package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.membercoupon.*;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.MemberCoupon;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.error.MemberCouponException;
import com.nhnacademy.boostorenginx.error.NotFoundCouponException;
import com.nhnacademy.boostorenginx.repository.CouponRepository;
import com.nhnacademy.boostorenginx.repository.MemberCouponRepository;
import com.nhnacademy.boostorenginx.service.CouponService;
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
class MemberCouponServiceImplTest {

    @InjectMocks
    private MemberCouponServiceImpl memberCouponService;

    @Mock
    private CouponService couponService;

    @Mock
    private MemberCouponRepository memberCouponRepository;

    @Mock
    private CouponRepository couponRepository;

    private Coupon mockCoupon;
    private MemberCoupon mockMemberCoupon;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        CouponPolicy couponPolicy = CouponPolicy.builder()
                .name("Test Policy")
                .saleType(SaleType.RATIO)
                .minimumAmount(new BigDecimal("1000"))
                .discountLimit(new BigDecimal("5000"))
                .discountRatio(20)
                .isStackable(true)
                .couponScope("ALL")
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(10))
                .couponActive(true)
                .build();

        mockCoupon = new Coupon(
                Status.UNUSED,
                now.minusDays(1),
                now.plusDays(10),
                couponPolicy
        );
        mockMemberCoupon = new MemberCoupon(
                1L,
                mockCoupon
        );
    }

    @DisplayName("회원쿠폰 발급")
    @Test
    void createMemberCoupon() {
        MemberCouponCreateRequestDto requestDto = new MemberCouponCreateRequestDto(1L, 100L, 0, 10);

        when(couponRepository.findById(100L)).thenReturn(Optional.of(mockCoupon));
        when(memberCouponRepository.existsByMcMemberIdAndId(1L, 100L)).thenReturn(false);
        when(memberCouponRepository.save(any(MemberCoupon.class))).thenReturn(mockMemberCoupon);

        MemberCouponResponseDto responseDto = memberCouponService.createMemberCoupon(requestDto);

        assertEquals(1L, responseDto.memberId());
        assertEquals(Status.UNUSED, responseDto.coupon().status());
        verify(memberCouponRepository, times(1)).save(any(MemberCoupon.class));
    }

    @DisplayName("회원쿠폰 발급시 발급할려는 쿠폰을 못찾을 경우")
    @Test
    void createMemberCoupon_NotFoundCoupon() {
        Long memberId = 1L;
        Long couponId = 1L;
        int page = 0;
        int pageSize = 5;
        MemberCouponCreateRequestDto requestDto = new MemberCouponCreateRequestDto(memberId, couponId, page, pageSize);
        when(couponRepository.findById(couponId)).thenReturn(Optional.empty());
        NotFoundCouponException exception = assertThrows(NotFoundCouponException.class,
                () -> memberCouponService.createMemberCoupon(requestDto));

        assertEquals("ID 에 해당하는 쿠폰이 존재하지 않습니다" + couponId, exception.getMessage());
        verify(couponRepository, times(1)).findById(couponId);
        verify(memberCouponRepository, never()).existsByMcMemberIdAndId(anyLong(), anyLong());
        verify(memberCouponRepository, never()).save(any(MemberCoupon.class));
    }

    @DisplayName("회원에게 이미 발급된 쿠폰생성시 예외발생")
    @Test
    void createMemberCoupon_AlreadyExistCoupon() {
        Long memberId = 1L;
        Long couponId = 1L;
        int page = 0;
        int pageSize = 5;
        MemberCouponCreateRequestDto requestDto = new MemberCouponCreateRequestDto(memberId, couponId, page, pageSize);
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(mock(Coupon.class)));
        when(memberCouponRepository.existsByMcMemberIdAndId(memberId, couponId)).thenReturn(true);
        MemberCouponException exception = assertThrows(MemberCouponException.class,
                () -> memberCouponService.createMemberCoupon(requestDto));
        assertEquals("회원에게 이미 발급된 쿠폰입니다", exception.getMessage());
        verify(couponRepository, times(1)).findById(couponId);
        verify(memberCouponRepository, times(1)).existsByMcMemberIdAndId(memberId, couponId);
        verify(memberCouponRepository, never()).save(any(MemberCoupon.class));
    }


    @DisplayName("회원 ID로 회원 쿠폰 조회 테스트")
    @Test
    void getMemberCouponsByMemberId() {
        MemberCouponFindByMemberIdRequestDto requestDto = new MemberCouponFindByMemberIdRequestDto(1L, 0, 10);
        Pageable pageable = PageRequest.of(0, 10);
        Page<MemberCoupon> memberCoupons = new PageImpl<>(Collections.singletonList(mockMemberCoupon), pageable, 1);

        when(memberCouponRepository.findByMcMemberIdOrderByIdAsc(1L, pageable)).thenReturn(memberCoupons);

        Page<MemberCouponGetResponseDto> response = memberCouponService.getMemberCouponsByMemberId(requestDto);

        assertEquals(1, response.getTotalElements());
        assertEquals("Test Policy", response.getContent().get(0).getName());
        assertEquals("UNUSED", response.getContent().get(0).getStatus());
        assertEquals("RATIO", response.getContent().get(0).getSaleType());
        assertEquals(new BigDecimal("1000"), response.getContent().get(0).getMinimumAmount());
        assertEquals(new BigDecimal("5000"), response.getContent().get(0).getDiscountLimit());
        assertEquals(20, response.getContent().get(0).getDiscountRatio());
        assertTrue(response.getContent().get(0).isStackable());
        assertEquals("ALL", response.getContent().get(0).getCouponScope());

        verify(memberCouponRepository, times(1)).findByMcMemberIdOrderByIdAsc(1L, pageable);
    }

    @DisplayName("쿠폰 ID로 회원 쿠폰 조회 테스트")
    @Test
    void getMemberCouponsByCouponId() {
        MemberCouponFindByCouponIdRequestDto requestDto = new MemberCouponFindByCouponIdRequestDto(100L, 0, 10);
        Pageable pageable = PageRequest.of(0, 10);
        Page<MemberCoupon> memberCoupons = new PageImpl<>(Collections.singletonList(mockMemberCoupon), pageable, 1);

        when(memberCouponRepository.findByCoupon_IdOrderByIdAsc(100L, pageable)).thenReturn(memberCoupons);

        Page<MemberCouponResponseDto> response = memberCouponService.getMemberCouponsByCouponId(requestDto);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        verify(memberCouponRepository, times(1)).findByCoupon_IdOrderByIdAsc(100L, pageable);
    }
}