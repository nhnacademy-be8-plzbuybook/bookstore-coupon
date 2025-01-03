//package com.nhnacademy.boostorenginx.service.impl;
//
//import com.nhnacademy.boostorenginx.dto.coupon.CouponCreateRequestDto;
//import com.nhnacademy.boostorenginx.dto.coupon.CouponResponseDto;
//import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
//import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
//import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetSaveRequestDto;
//import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponCreateRequestDto;
//import com.nhnacademy.boostorenginx.dto.welcome.WelcomeCouponRequestDto;
//import com.nhnacademy.boostorenginx.entity.Coupon;
//import com.nhnacademy.boostorenginx.entity.MemberCoupon;
//import com.nhnacademy.boostorenginx.enums.SaleType;
//import com.nhnacademy.boostorenginx.repository.CouponRepository;
//import com.nhnacademy.boostorenginx.repository.MemberCouponRepository;
//import com.nhnacademy.boostorenginx.service.CouponPolicyService;
//import com.nhnacademy.boostorenginx.service.CouponService;
//import com.nhnacademy.boostorenginx.service.CouponTargetService;
//import com.nhnacademy.boostorenginx.service.MemberCouponService;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.YearMonth;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class WelcomeCouponServiceImpl {
//    private final CouponPolicyService couponPolicyService;
//    private final CouponTargetService couponTargetService;
//    private final CouponService couponService;
//    private final MemberCouponRepository memberCouponRepository;
//    private final CouponRepository couponRepository;
//
//    @Transactional
//    public void issueWelcomeCoupon(WelcomeCouponRequestDto requestDto) {
//        Long memberId = requestDto.memberId();
//
//        Long couponPolicyId = createWelcomeCouponPolicy();
//
//        createWelcomeCouponTarget(couponPolicyId, memberId);
//
//        LocalDateTime couponExpiryDate = calculateCouponExpiryDate(requestDto.registeredAt());
//
//        // Welcome 쿠폰 생성
//        CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto(
//                couponPolicyId,
//                couponExpiryDate
//        );
//        CouponResponseDto couponResponseDto = couponService.createCoupon(couponCreateRequestDto);
//        Coupon coupon = couponRepository.findById(couponPolicyId).orElse(null);
//        // 회원쿠폰 발급
//        memberCouponRepository.save(new MemberCoupon(memberId, coupon));
//
//    }
//
//    // Welcome 쿠폰정책 생성
//    private Long createWelcomeCouponPolicy() {
//        CouponPolicySaveRequestDto couponPolicySaveRequestDto = new CouponPolicySaveRequestDto(
//                "WELCOME_COUPON",
//                SaleType.AMOUNT,
//                new BigDecimal("50000"),
//                new BigDecimal("10000"),
//                0,
//                true,
//                "ALL",
//                LocalDateTime.now(),
//                LocalDateTime.now().plusDays(30),
//                true
//        );
//        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);
//
//        return couponPolicyResponseDto.id();
//    }
//
//    // Welcome 쿠폰대상 생성
//    private void createWelcomeCouponTarget(Long couponPolicyId, Long memberId) {
//        couponTargetService.createCouponTarget(
//                new CouponTargetSaveRequestDto(couponPolicyId, memberId)
//        );
//    }
//
//    // 유효기간 월 계산
//    private LocalDateTime calculateCouponExpiryDate(LocalDateTime registeredAt) {
//        YearMonth registeredMonth = YearMonth.from(registeredAt);
//        return registeredMonth.atEndOfMonth().atTime(23, 59, 59);
//    }
//}
