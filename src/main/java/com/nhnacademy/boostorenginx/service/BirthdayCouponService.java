//package com.nhnacademy.boostorenginx.service;
//
//
//import com.nhnacademy.boostorenginx.dto.birthday.BirthdayCouponRequestDto;
//import com.nhnacademy.boostorenginx.dto.coupon.CouponCreateRequestDto;
//import com.nhnacademy.boostorenginx.dto.coupon.CouponResponseDto;
//import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
//import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
//import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
//import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
//import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponCreateRequestDto;
//
//import com.nhnacademy.boostorenginx.enums.SaleType;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//
//@RequiredArgsConstructor
//@Service
//public class BirthdayCouponService {
//
//    private final CouponPolicyService couponPolicyService;
//    private final CouponTargetService couponTargetService;
//    private final MemberCouponService memberCouponService;
//    private final CouponService couponService;
//
//    @Transactional
//    public void issueBirthdayCoupon(BirthdayCouponRequestDto requestDto) {
//
//        // Birthday 쿠폰요청 생성
//        CouponPolicySaveRequestDto couponPolicySaveRequestDto = new CouponPolicySaveRequestDto(
//                "BIRTHDAY_COUPON",
//                SaleType.AMOUNT,
//                new BigDecimal("10000"),
//                new BigDecimal("1000"),
//                0,
//                true,
//                "ALL",
//                LocalDateTime.now(),
//                LocalDateTime.now().plusDays(30),
//                true
//        );
//
//        // Birthday 쿠폰정책 생성
//        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);
//
//        // Birthday 쿠폰대상 생성
//        CouponTargetAddRequestDto couponTargetAddRequestDto = new CouponTargetAddRequestDto(
//                couponPolicyResponseDto.id(),
//                requestDto.memberId()
//        );
//        couponTargetService.createCouponTarget(couponTargetAddRequestDto);
//
//        // Birthday 쿠폰 생성
//        CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto(
//                couponPolicyResponseDto.id(),
//                requestDto.registerAt()
//        );
//        CouponResponseDto couponResponseDto = couponService.createCoupon(couponCreateRequestDto);
//
//        // 회원쿠폰 발급
//
//}
