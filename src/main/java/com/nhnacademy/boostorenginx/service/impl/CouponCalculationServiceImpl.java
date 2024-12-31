//package com.nhnacademy.boostorenginx.service.impl;
//
//import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationRequestDto;
//import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationResponseDto;
//import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyIdRequestDto;
//import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
//import com.nhnacademy.boostorenginx.error.CouponCalculationExcption;
//import com.nhnacademy.boostorenginx.service.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//
//
//@Slf4j
//@Transactional
//@RequiredArgsConstructor
//@Service
//public class CouponCalculationServiceImpl implements CouponCalculationService {
//    private final MemberCouponService memberCouponService;
//    private final CouponService couponService;
//    private final CouponPolicyService couponPolicyService;
//    private final CouponTargetService couponTargetService;
//
//    public CouponCalculationResponseDto applyOrderProductCoupon(CouponCalculationRequestDto couponCalculationRequestDto) {
//
//        Long memberId = couponCalculationRequestDto.memberId(); // 회원 ID
//        Long couponId = couponCalculationRequestDto.couponId(); // 쿠폰 ID
//        Long couponPolicyId = couponCalculationRequestDto.couponPolicy(); // 쿠폰정책 ID
//
//        BigDecimal originalPrice = null; // 할인 적용 전 가격
//        BigDecimal calculationPrice = null; // 할인 적용 후 가격
//        BigDecimal discountPrice = null;
//        BigDecimal discountAmount = null;
//        BigDecimal productPrice = couponCalculationRequestDto.productPrice(); // 주문상품가격(개당)
//        Integer quantity = couponCalculationRequestDto.quantity(); // 주문상품수량
//
//        BigDecimal totalPrice = productPrice.multiply(BigDecimal.valueOf(quantity)); // 주문금액 합계
//
//        // 쿠폰정책 조회
//        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.findById(new CouponPolicyIdRequestDto(couponPolicyId));
//        if (!couponPolicyResponseDto.couponActive()){
//            throw new CouponCalculationExcption("사용 불가능한 쿠폰입니다");
//        }
//
//
//        BigDecimal minimumAmount = couponPolicyResponseDto.minimumAmount(); // 쿠폰적용 최소 금액
//
//        // 쿠폰적용 최소금액이 합계보다 작다면
//        if (minimumAmount.compareTo(totalPrice) > 0) {
//            throw new CouponCalculationExcption("쿠폰적용 최소금액이 상품가격보다 작습니다");
//        }
//
//        BigDecimal discountLimit = couponPolicyResponseDto.discountLimit(); // 최대할인금액
//
//        // 최대할인금액이 주문금액 이상이면 할인 적용 후 가격을 0원으로 변경
//        if (discountLimit.compareTo(totalPrice) > 0) {
//            calculationPrice = BigDecimal.ZERO;
//        }
//
//        // 할인 적용 후 가격 계산
//        if (couponPolicyResponseDto.discountRatio().intValue() == 0 ) {
//            log.debug("고정 할인 쿠폰입니다");
//            originalPrice = totalPrice;
//            discountAmount = discountLimit;
//            calculationPrice = totalPrice.subtract(discountLimit);
//
//        } else {
//
//        }
//
//
//        return null;
//    }
//
//}
