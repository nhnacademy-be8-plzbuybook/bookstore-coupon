package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationRequestDto;
import com.nhnacademy.boostorenginx.dto.calculation.CouponCalculationResponseDto;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.error.CouponCalculationExcption;
import com.nhnacademy.boostorenginx.service.CouponCalculationService;
import com.nhnacademy.boostorenginx.service.CouponService;
import com.nhnacademy.boostorenginx.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CouponCalculationServiceImpl implements CouponCalculationService {
    private final MemberCouponService memberCouponService;
    private final CouponService couponService;

    public CouponCalculationResponseDto applyOrderProductCoupon(Long memberId, Long couponId, CouponCalculationRequestDto couponCalculationRequestDto) {
        BigDecimal productPrice = couponCalculationRequestDto.productPrice(); // 주문상품가격(개당)
        Integer quantity = couponCalculationRequestDto.quantity(); // 주문상품수량

        BigDecimal totalPrice = productPrice.multiply(BigDecimal.valueOf(quantity)); // 주문금액 합계

        // 쿠폰 ID 에 해당되는 쿠폰정책 조회
        CouponPolicy couponPolicy = couponService.findCouponPolicyByCouponId(couponId);
        if (!couponPolicy.isCouponActive()) {
            throw new CouponCalculationExcption("사용 불가능한 쿠폰입니다");
        }

        // 쿠폰 할인의 최소금액이 합계보다 작다면
        BigDecimal minimumAmount = couponPolicy.getMinimumAmount(); // 쿠폰적용 최소 금액
        if (minimumAmount.compareTo(totalPrice) > 0) {
            throw new CouponCalculationExcption("주문 금액이 쿠폰을 적용할 수 있는 최소 금액보다 적습니다");
        }

        BigDecimal discountLimit = couponPolicy.getDiscountLimit(); // 최대 할인 금액
        BigDecimal discountRatio = BigDecimal.valueOf(couponPolicy.getDiscountRatio()); // 할인율 (0이면 고정 할인)

        // 할인 금액 계산
        BigDecimal discountAmount = calculateDiscountAmount(totalPrice, discountRatio, discountLimit);

        // 할인 적용 후 가격
        BigDecimal calculationPrice = totalPrice.subtract(discountAmount);
        if (calculationPrice.compareTo(BigDecimal.ZERO) < 0) {
            calculationPrice = BigDecimal.ZERO; // 음수 방지
        }

        log.debug("원래 가격: {}, 할인 금액: {}, 계산된 가격: {}", totalPrice, discountAmount, calculationPrice);

        CouponCalculationResponseDto couponCalculationResponseDto = new CouponCalculationResponseDto(
                discountAmount,
                totalPrice,
                calculationPrice);

        // 회원이 쿠폰 사용 -> 회원쿠폰 서비스에서 쿠폰사용 로직 호출 -> 내부에서 쿠폰의 상태 변경하는 로직 호출
        MemberCouponUseRequestDto memberCouponUseRequestDto = new MemberCouponUseRequestDto(memberId, couponId);
        memberCouponService.useMemberCoupon(memberCouponUseRequestDto);

        return couponCalculationResponseDto;
    }

    // 할인 금액 계산
    private BigDecimal calculateDiscountAmount(BigDecimal totalPrice, BigDecimal discountRatio, BigDecimal discountLimit) {
        if (discountRatio.compareTo(BigDecimal.ZERO) > 0) {
            log.debug("퍼센트 할인 쿠폰입니다.");
            BigDecimal percentage = totalPrice.multiply(discountRatio.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
            return (percentage.compareTo(discountLimit) > 0) ? discountLimit : percentage;
        } else {
            log.debug("고정 할인 쿠폰입니다.");
            return discountLimit;
        }
    }
}
