package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationRequestDto;
import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyIdRequestDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.error.CouponCalculationExcption;
import com.nhnacademy.boostorenginx.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CouponCalculationServiceImpl implements CouponCalculationService {
    private final MemberCouponService memberCouponService;
    private final CouponService couponService;
    private final CouponPolicyService couponPolicyService;
    private final CouponTargetService couponTargetService;

    public CouponCalculationResponseDto applyOrderProductCoupon(CouponCalculationRequestDto couponCalculationRequestDto) {

        Long memberId = couponCalculationRequestDto.memberId(); // 회원 ID
        Long couponId = couponCalculationRequestDto.couponId(); // 쿠폰 ID
        Long couponPolicyId = couponCalculationRequestDto.couponPolicy(); // 쿠폰정책 ID

        BigDecimal productPrice = couponCalculationRequestDto.productPrice(); // 주문상품가격(개당)
        Integer quantity = couponCalculationRequestDto.quantity(); // 주문상품수량
        BigDecimal totalPrice = productPrice.multiply(BigDecimal.valueOf(quantity)); // 주문금액 합계

        // 쿠폰정책 조회
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.findById(new CouponPolicyIdRequestDto(couponPolicyId));
        if (!couponPolicyResponseDto.couponActive()){
            throw new CouponCalculationExcption("사용 불가능한 쿠폰입니다");
        }

        // 쿠폰적용 최소금액이 합계보다 작다면
        BigDecimal minimumAmount = couponPolicyResponseDto.minimumAmount(); // 쿠폰적용 최소 금액
        if (minimumAmount.compareTo(totalPrice) > 0) {
            throw new CouponCalculationExcption("주문 금액이 쿠폰을 적용할 수 있는 최소 금액보다 적습니다");
        }

        BigDecimal discountLimit = couponPolicyResponseDto.discountLimit(); // 최대 할인 금액
        BigDecimal discountRatio = BigDecimal.valueOf(couponPolicyResponseDto.discountRatio()); // 할인율 (0이면 고정 할인)

        // 할인 금액 계산
        BigDecimal discountAmount;
        if (discountRatio.compareTo(BigDecimal.ZERO) > 0) {
            log.debug("퍼센트 할인 쿠폰입니다.");
            discountAmount = totalPrice.multiply(discountRatio.divide(BigDecimal.valueOf(100))); // 퍼센트 할인 계산
        } else {
            log.debug("고정 할인 쿠폰입니다.");
            discountAmount = discountLimit; // 고정 할인 금액 적용
        }

        // 최대 할인 금액 제한
        if (discountAmount.compareTo(discountLimit) > 0) {
            discountAmount = discountLimit; // 할인 금액을 최대 할인 금액으로 제한
        }

        // 할인 적용 후 가격
        BigDecimal calculationPrice = totalPrice.subtract(discountAmount);
        if (calculationPrice.compareTo(BigDecimal.ZERO) < 0) {
            calculationPrice = BigDecimal.ZERO; // 음수 방지
        }

        log.debug("원래 가격: {}, 할인 금액: {}, 계산된 가격: {}", totalPrice, discountAmount, calculationPrice);

        CouponCalculationResponseDto couponCalculationResponseDto = new CouponCalculationResponseDto(discountAmount,
                totalPrice,
                calculationPrice);

        // 회원이 쿠폰 사용 -> 쿠폰 상태 변경
        MemberCouponUseRequestDto memberCouponUseRequestDto = new MemberCouponUseRequestDto(memberId, couponId);
        couponService.useCoupon(memberCouponUseRequestDto);

        return couponCalculationResponseDto;
    }
}
