package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.coupon.CouponResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetSaveRequestDto;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.welcome.WelComeCouponRequestDto;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.service.CouponPolicyService;
import com.nhnacademy.boostorenginx.service.CouponService;
import com.nhnacademy.boostorenginx.service.CouponTargetService;
import com.nhnacademy.boostorenginx.service.MemberCouponService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class WelcomeCouponServiceImpl {
    private final CouponPolicyService couponPolicyService;
    private final CouponTargetService couponTargetService;
    private final MemberCouponService memberCouponService;
    private final CouponService couponService;

    @Transactional
    public void issueWelcomeCoupon(WelComeCouponRequestDto requestDto) {

        // Welcome 쿠폰정책 생성
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

        Long couponPolicyId = couponPolicyResponseDto.id();

        // Welcome 쿠폰대상 생성
        CouponTargetSaveRequestDto couponTargetAddRequestDto = new CouponTargetSaveRequestDto(
                couponPolicyId, requestDto.memberId()
        );
        couponTargetService.createCouponTarget(couponTargetAddRequestDto);

        // 유효기간 월 계산


        // Welcome 쿠폰 생성
        CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto(
                couponPolicyId,
                requestDto.registeredAt().plusDays(30)
        );
        CouponResponseDto couponResponseDto = couponService.createCoupon(couponCreateRequestDto);

        Long couponId = couponResponseDto.id();

        // 회원쿠폰 발급
        MemberCouponCreateRequestDto memberCouponCreateRequestDto = new MemberCouponCreateRequestDto(
                requestDto.memberId(),
                couponId,
                0,
                1
        );
        memberCouponService.createMemberCoupon(memberCouponCreateRequestDto);

    }

    private int calculatemonth() {

    }
}
