package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.welcome.WelComeCouponRequestDto;
import com.nhnacademy.boostorenginx.enums.SaleType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


// 회원가입 쿠폰 발급 서비스 -> 쿠폰 생성 까진 됬으나 회원에게 발급은 아직 되어있지 않음
@RequiredArgsConstructor
@Service
public class WelcomeCouponService {
    private final CouponPolicyService couponPolicyService;
    private final CouponTargetService couponTargetService;
    private final CouponService couponService;

    public void issueWelcomeCoupon(WelComeCouponRequestDto requestDto) {

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

        Long couponPolicyId = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);
        CouponTargetAddRequestDto couponTargetAddRequestDto = new CouponTargetAddRequestDto(
                couponPolicyId, requestDto.memberId()
        );
        couponTargetService.createCouponTarget(couponTargetAddRequestDto);
        CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto(
                couponPolicyId,
                requestDto.registeredAt()
        );
        couponService.createCoupon(couponCreateRequestDto);
    }
}
