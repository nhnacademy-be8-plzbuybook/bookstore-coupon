package com.nhnacademy.bookstorecoupon.dto.coupon;

import com.nhnacademy.bookstorecoupon.entity.Coupon;
import com.nhnacademy.bookstorecoupon.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CouponResponseDto(
        Long id, // 쿠폰 식별키
        String code, // 쿠폰 코드
        Status status, // 쿠폰 상태
        LocalDateTime issuedAt, // 쿠폰 생성일
        LocalDateTime expiredAt, // 쿠폰 만료일
        CouponPolicyDto couponPolicy // 쿠폰 정책
) {
    public static CouponResponseDto fromCoupon(Coupon coupon) {
        return new CouponResponseDto(
                coupon.getId(),
                coupon.getCode(),
                coupon.getStatus(),
                coupon.getIssuedAt(),
                coupon.getExpiredAt(),
                CouponPolicyDto.fromCouponPolicy(coupon.getCouponPolicy()) // Mapping CouponPolicy
        );
    }

    public record CouponPolicyDto(
            Long id, // 쿠폰정책 ID
            String name, // 쿠폰정책 이름
            String saleType, // 쿠폰정책 타입
            BigDecimal minimumAmount, // 쿠폰정책 적용 최소금액
            BigDecimal discountLimit, // 쿠폰정책 최대할인금액
            Integer discountRatio, // 쿠폰정책 할인율
            boolean isStackable, // 쿠폰정책 중복사용여부
            String couponScope, // 쿠폰정책 범위
            LocalDateTime startDate, // 쿠폰정책 시작일
            LocalDateTime endDate, // 쿠폰정책 종료일
            boolean couponActive // 쿠폰정책 활성화여부
    ) {
        public static CouponPolicyDto fromCouponPolicy(CouponPolicy couponPolicy) {
            return new CouponPolicyDto(
                    couponPolicy.getId(),
                    couponPolicy.getName(),
                    couponPolicy.getSaleType().name(),
                    couponPolicy.getMinimumAmount(),
                    couponPolicy.getDiscountLimit(),
                    couponPolicy.getDiscountRatio(),
                    couponPolicy.isStackable(),
                    couponPolicy.getCouponScope(),
                    couponPolicy.getStartDate(),
                    couponPolicy.getEndDate(),
                    couponPolicy.isCouponActive()
            );
        }
    }
}