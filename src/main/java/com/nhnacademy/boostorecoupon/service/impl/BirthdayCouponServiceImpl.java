package com.nhnacademy.boostorecoupon.service.impl;


import com.nhnacademy.boostorecoupon.dto.birthday.BirthdayCouponRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupon.CouponResponseDto;
import com.nhnacademy.boostorecoupon.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorecoupon.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetSaveRequestDto;
import com.nhnacademy.boostorecoupon.dto.membercoupon.MemberCouponCreateRequestDto;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class BirthdayCouponServiceImpl {

    private final CouponPolicyService couponPolicyService;
    private final CouponTargetService couponTargetService;
    private final MemberCouponService memberCouponService;
    private final CouponService couponService;

    @Transactional
    public void issueBirthdayCoupon(BirthdayCouponRequestDto requestDto) {

        // Birthday 쿠폰정책 요청
        CouponPolicySaveRequestDto couponPolicySaveRequestDto = new CouponPolicySaveRequestDto(
                "BIRTHDAY_COUPON",
                SaleType.AMOUNT,
                new BigDecimal("10000"),
                new BigDecimal("5000"),
                0,
                true,
                "ALL",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(30),
                true
        );

        // Birthday 쿠폰정책 생성
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);
        Long couponPolicyId = couponPolicyResponseDto.id();
        log.debug("CouponPolicyId: {}, MemberId: {}", couponPolicyId, requestDto.memberId());

        // Birthday 쿠폰대상 생성
        CouponTargetSaveRequestDto couponTargetAddRequestDto = new CouponTargetSaveRequestDto(
                couponPolicyId,
                requestDto.memberId()
        );
        CouponTargetResponseDto couponTargetResponseDto = couponTargetService.createCouponTarget(couponTargetAddRequestDto);
        Long couponTargetId = couponTargetResponseDto.couponTargetId();
        log.debug("CouponTargetId: {}", couponTargetId);

        // Birthday 쿠폰 생성
        CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto(
                couponPolicyId,
                requestDto.registerAt().plusMonths(1)
        );
        CouponResponseDto couponResponseDto = couponService.createCoupon(couponCreateRequestDto);
        Long couponId = couponResponseDto.id();
        log.debug("CouponId: {}", couponId);

        // 회원쿠폰 발급
        MemberCouponCreateRequestDto memberCouponCreateRequestDto = new MemberCouponCreateRequestDto(
                requestDto.memberId(),
                couponId
        );
        memberCouponService.createMemberCoupon(memberCouponCreateRequestDto);
    }

}
