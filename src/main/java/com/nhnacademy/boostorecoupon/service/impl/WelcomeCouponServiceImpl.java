package com.nhnacademy.boostorecoupon.service.impl;

import com.nhnacademy.boostorecoupon.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupon.CouponResponseDto;
import com.nhnacademy.boostorecoupon.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorecoupon.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetSaveRequestDto;
import com.nhnacademy.boostorecoupon.dto.membercoupon.MemberCouponCreateRequestDto;
import com.nhnacademy.boostorecoupon.dto.welcome.WelcomeCouponRequestDto;
import com.nhnacademy.boostorecoupon.enums.SaleType;
import com.nhnacademy.boostorecoupon.service.CouponPolicyService;
import com.nhnacademy.boostorecoupon.service.CouponService;
import com.nhnacademy.boostorecoupon.service.CouponTargetService;
import com.nhnacademy.boostorecoupon.service.MemberCouponService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Slf4j
@RequiredArgsConstructor
@Service
public class WelcomeCouponServiceImpl {
    private final CouponPolicyService couponPolicyService;
    private final CouponTargetService couponTargetService;
    private final CouponService couponService;
    private final MemberCouponService memberCouponService;


    @Transactional
    public void issueWelcomeCoupon(WelcomeCouponRequestDto requestDto) {
        Long memberId = requestDto.memberId();
        LocalDateTime registeredAt = requestDto.registeredAt();

        // Welcome 쿠폰정책 생성
        Long couponPolicyId = createWelcomeCouponPolicy();

        // Welcome 쿠폰대상 생성
        createWelcomeCouponTarget(couponPolicyId, memberId);

        // 종료일 계산
        LocalDateTime endDate = calculateExpiredAt(registeredAt);

        // Welcome 쿠폰 생성
        CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto(
                couponPolicyId,
                endDate
        );
        CouponResponseDto couponResponseDto = couponService.createCoupon(couponCreateRequestDto);

        // 회원쿠폰 발급
        MemberCouponCreateRequestDto memberCouponCreateRequestDto = new MemberCouponCreateRequestDto(
                memberId,
                couponResponseDto.id()
        );
        memberCouponService.createMemberCoupon(memberCouponCreateRequestDto);

    }

    // Welcome 쿠폰정책 생성
    private Long createWelcomeCouponPolicy() {
        CouponPolicySaveRequestDto couponPolicySaveRequestDto = new CouponPolicySaveRequestDto(
                "WELCOME_COUPON",
                SaleType.AMOUNT,
                new BigDecimal("50000"),
                new BigDecimal("10000"),
                0,
                true,
                "ALL",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                true
        );
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);

        return couponPolicyResponseDto.id();
    }

    // Welcome 쿠폰대상 생성
    private void createWelcomeCouponTarget(Long couponPolicyId, Long memberId) {
        couponTargetService.createCouponTarget(
                new CouponTargetSaveRequestDto(couponPolicyId, memberId)
        );
    }

    // 유효기간 월 계산
    private LocalDateTime calculateExpiredAt(LocalDateTime registeredAt) {
        YearMonth registeredMonth = YearMonth.from(registeredAt);
        return registeredMonth.atEndOfMonth().atTime(23, 59, 59);
    }
}
