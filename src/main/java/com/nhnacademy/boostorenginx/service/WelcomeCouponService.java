package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.coupon.CouponResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.welcome.WelComeCouponRequestDto;
import com.nhnacademy.boostorenginx.enums.SaleType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class WelcomeCouponService {
    private final CouponPolicyService couponPolicyService;
    private final CouponTargetService couponTargetService;
    private final MemberCouponService memberCouponService;
    private final CouponService couponService;

    @Transactional
    public void issueWelcomeCoupon(WelComeCouponRequestDto requestDto) {

        // Welcome 쿠폰정책 Dto 생성
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

        // Welcome 쿠폰정책 생성
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);

        // Welcome 쿠폰대상 생성
        CouponTargetAddRequestDto couponTargetAddRequestDto = new CouponTargetAddRequestDto(
                couponPolicyResponseDto.id(), requestDto.memberId()
        );
        CouponTargetResponseDto couponTargetResponseDto = couponTargetService.createCouponTarget(couponTargetAddRequestDto);

        // Welcome 쿠폰 생성
        CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto(
                couponPolicyResponseDto.id(),
                requestDto.registeredAt()
        );
        CouponResponseDto couponResponseDto = couponService.createCoupon(couponCreateRequestDto);

        // 회원쿠폰 발급
        MemberCouponCreateRequestDto memberCouponCreateRequestDto = new MemberCouponCreateRequestDto(
                requestDto.memberId(),
                couponResponseDto.id(),
                0,
                1
        );
        memberCouponService.createMemberCoupon(memberCouponCreateRequestDto);

    }
}
