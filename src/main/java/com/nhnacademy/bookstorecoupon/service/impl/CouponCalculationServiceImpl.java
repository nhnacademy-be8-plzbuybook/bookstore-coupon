package com.nhnacademy.bookstorecoupon.service.impl;

import com.nhnacademy.bookstorecoupon.dto.calculation.CouponCalculationRequestDto;
import com.nhnacademy.bookstorecoupon.dto.calculation.CouponCalculationResponseDto;
import com.nhnacademy.bookstorecoupon.dto.calculation.ValidationCouponCalculation;
import com.nhnacademy.bookstorecoupon.dto.calculation.ValidationCouponCalculationRequestDto;
import com.nhnacademy.bookstorecoupon.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.bookstorecoupon.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.error.CouponCalculationExcption;
import com.nhnacademy.bookstorecoupon.service.CouponCalculationService;
import com.nhnacademy.bookstorecoupon.service.CouponService;
import com.nhnacademy.bookstorecoupon.service.MemberCouponService;
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

    public CouponCalculationResponseDto applyOrderProductCoupon(Long couponId, CouponCalculationRequestDto couponCalculationRequestDto) {
        // 주문금액
        BigDecimal price = couponCalculationRequestDto.price();

        // 쿠폰 ID 에 해당되는 쿠폰정책 조회 및 유효성 검사
        CouponPolicy couponPolicy = couponService.findCouponPolicyByCouponId(couponId);
        if (!couponPolicy.isCouponActive()) {
            throw new CouponCalculationExcption("사용 불가능한 쿠폰입니다");
        }

        // 쿠폰 할인의 최소금액이 합계보다 작은 경우 예외 처리
        BigDecimal minimumAmount = couponPolicy.getMinimumAmount();
        if (minimumAmount.compareTo(price) > 0) {
            throw new CouponCalculationExcption("주문 금액이 쿠폰을 적용할 수 있는 최소 금액보다 적습니다");
        }

        // 최대 할인 금액
        BigDecimal discountLimit = couponPolicy.getDiscountLimit();

        // 할인율 (0이면 고정 할인)
        BigDecimal discountRatio = BigDecimal.valueOf(couponPolicy.getDiscountRatio());

        // 할인 금액 계산
        BigDecimal discountAmount = calculateDiscountAmount(price, discountRatio, discountLimit);

        // 할인 적용 후 가격
        BigDecimal calculationPrice = price.subtract(discountAmount);

        // 계산 후 가격이 0보다 작을 경우 0원 고정
        if (calculationPrice.compareTo(BigDecimal.ZERO) < 0) {
            calculationPrice = BigDecimal.ZERO; // 음수 방지
        }

        log.debug("계산 전 가격: {}, 할인 금액: {}, 계산 후 가격: {}", price, discountAmount, calculationPrice);

        CouponCalculationResponseDto couponCalculationResponseDto = new CouponCalculationResponseDto(
                discountAmount,
                price,
                calculationPrice);

        // 쿠폰 사용으로 인한 상태 변경
        Long mcMemberId = couponCalculationRequestDto.memberId();
        MemberCouponUseRequestDto memberCouponUseRequestDto = new MemberCouponUseRequestDto(mcMemberId, couponId);
        memberCouponService.useMemberCoupon(memberCouponUseRequestDto);

        return couponCalculationResponseDto;
    }

    // 할인 금액 계산 (할인 금액이 최대할인금액 높을경우 최대 할인금액 반환)
    private BigDecimal calculateDiscountAmount(BigDecimal totalPrice, BigDecimal discountRatio, BigDecimal discountLimit) {
        if (discountRatio.compareTo(BigDecimal.ZERO) > 0) {
            // 할인비율 계산(소수점 3째자리에서 반올림처리)
            BigDecimal percentageDisCount = totalPrice.multiply(discountRatio.divide(BigDecimal.valueOf(100), 3, RoundingMode.HALF_UP));
            return percentageDisCount.min(discountLimit);
        }

        return discountLimit;
    }

    // 계산 검증용 함수
    public ValidationCouponCalculation validateCouponCalculation(Long couponId, ValidationCouponCalculationRequestDto validationCouponCalculationRequestDto){
        // 주문금액
        BigDecimal price = validationCouponCalculationRequestDto.price();

        // 쿠폰 ID 에 해당되는 쿠폰정책 조회 및 유효성 검사
        CouponPolicy couponPolicy = couponService.findCouponPolicyByCouponId(couponId);
        if (!couponPolicy.isCouponActive()) {
            throw new CouponCalculationExcption("사용 불가능한 쿠폰입니다");
        }

        // 최대 할인 금액
        BigDecimal discountLimit = couponPolicy.getDiscountLimit();

        // 할인율 (0이면 고정 할인)
        BigDecimal discountRatio = BigDecimal.valueOf(couponPolicy.getDiscountRatio());

        // 할인 금액 계산
        BigDecimal discountAmount = calculateDiscountAmount(price, discountRatio, discountLimit);

        // 할인 적용 후 가격
        BigDecimal calculationPrice = price.subtract(discountAmount);

        // 계산 후 가격이 0보다 작을 경우 0원 고정
        if (calculationPrice.compareTo(BigDecimal.ZERO) < 0) {
            calculationPrice = BigDecimal.ZERO; // 음수 방지
        }

        return new ValidationCouponCalculation(calculationPrice);
    }
}
