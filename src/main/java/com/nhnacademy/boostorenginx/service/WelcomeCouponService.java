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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class WelcomeCouponService {
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
        log.debug("CouponPolicyId: {}, MemberId: {}", couponPolicyId, requestDto.memberId());
        // Welcome 쿠폰대상 생성
        CouponTargetAddRequestDto couponTargetAddRequestDto = new CouponTargetAddRequestDto(
                couponPolicyId, requestDto.memberId()
        );
        CouponTargetResponseDto couponTargetResponseDto = couponTargetService.createCouponTarget(couponTargetAddRequestDto);

        Long couponTargetId = couponTargetResponseDto.couponTargetId();

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
}
