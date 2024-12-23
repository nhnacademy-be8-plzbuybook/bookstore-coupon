package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.MemberCoupon;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.error.CouponException;
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
        mockCoupon = new Coupon();
        mockMemberCoupon = new MemberCoupon();
    }

    @DisplayName("회원쿠폰 발급")
    @Test
    void createMemberCoupon() {
        Long memberId = 3L;
        Long couponId = 1L;
        int page = 0;
        int pageSize = 5;
        MemberCouponCreateRequestDto requestDto = new MemberCouponCreateRequestDto(memberId, couponId, page, pageSize);
        mockCoupon = mock(Coupon.class);
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));
        when(memberCouponRepository.existsByMcMemberIdAndMemberCouponId(memberId, couponId)).thenReturn(false);
        memberCouponService.createMemberCoupon(requestDto);
        verify(couponRepository, times(1)).findById(couponId);
        verify(memberCouponRepository, times(1)).existsByMcMemberIdAndMemberCouponId(memberId, couponId);
        verify(memberCouponRepository, times(1)).save(any(MemberCoupon.class));
    }

    @DisplayName("회원쿠폰 생성시 해당 쿠폰을 못찾을 경우 예외발생")
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
        verify(memberCouponRepository, never()).existsByMcMemberIdAndMemberCouponId(anyLong(), anyLong());
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
        when(memberCouponRepository.existsByMcMemberIdAndMemberCouponId(memberId, couponId)).thenReturn(true);
        MemberCouponException exception = assertThrows(MemberCouponException.class,
                () -> memberCouponService.createMemberCoupon(requestDto));
        assertEquals("회원에게 이미 발급된 쿠폰입니다", exception.getMessage());
        verify(couponRepository, times(1)).findById(couponId);
        verify(memberCouponRepository, times(1)).existsByMcMemberIdAndMemberCouponId(memberId, couponId);
        verify(memberCouponRepository, never()).save(any(MemberCoupon.class));
    }

    @DisplayName("회원이 쿠폰사용")
    @Test
    void useMemberCoupon() {
        Long couponId = 1L;
        Long memberId = 1L;
        MemberCouponUseRequestDto requestDto = new MemberCouponUseRequestDto(couponId, memberId);
        mockMemberCoupon = mock(MemberCoupon.class);
        mockCoupon = mock(Coupon.class);
        when(memberCouponRepository.findById(memberId)).thenReturn(Optional.of(mockMemberCoupon));
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));
        when(mockCoupon.getStatus()).thenReturn(Status.UNUSED);
        doNothing().when(couponService).useCoupon(requestDto);
        memberCouponService.useMemberCoupon(requestDto);
        verify(memberCouponRepository, times(1)).findById(memberId);
        verify(couponRepository, times(1)).findById(couponId);
        verify(mockCoupon, times(1)).getStatus();
        verify(couponService, times(1)).useCoupon(requestDto);
        verify(memberCouponRepository, times(1)).save(mockMemberCoupon);
    }

    @DisplayName("회원의 ID 를 찾지 못하는 경우 예외발생")
    @Test
    void useMemberCoupon_NotFoundMemberId() {
        Long couponId = 1L;
        Long memberId = 1L;
        MemberCouponUseRequestDto requestDto = new MemberCouponUseRequestDto(couponId, memberId);
        MemberCouponException exception = assertThrows(MemberCouponException.class,
                () -> memberCouponService.useMemberCoupon(requestDto));

        assertEquals("회원쿠폰 ID 에 해당되는 것을 찾을 수 없습니다: " + memberId, exception.getMessage());
        verify(memberCouponRepository, times(1)).findById(memberId);
        verifyNoInteractions(couponRepository);
        verifyNoInteractions(couponService);
    }

    @DisplayName("회원 ID 에 해당되는 쿠폰 ID 를 찾지 못하는 경우 예외발생")
    @Test
    void useMemberCoupon_NotFoundMemberCoupon() {
        Long couponId = 1L;
        Long memberId = 1L;
        MemberCouponUseRequestDto requestDto = new MemberCouponUseRequestDto(couponId, memberId);
        when(memberCouponRepository.findById(memberId)).thenReturn(Optional.of(mock(MemberCoupon.class)));
        when(couponRepository.findById(couponId)).thenReturn(Optional.empty());
        NotFoundCouponException exception = assertThrows(NotFoundCouponException.class,
                () -> memberCouponService.useMemberCoupon(requestDto));
        assertEquals("쿠폰 ID 에 해당되는 것을 찾을 수 없습니다: " + couponId, exception.getMessage());
        verify(memberCouponRepository, times(1)).findById(memberId);
        verify(couponRepository, times(1)).findById(couponId);
        verifyNoInteractions(couponService);
    }
}