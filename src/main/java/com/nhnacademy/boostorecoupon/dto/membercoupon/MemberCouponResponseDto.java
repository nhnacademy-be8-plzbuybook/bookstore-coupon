package com.nhnacademy.boostorecoupon.dto.membercoupon;

import com.nhnacademy.boostorecoupon.entity.Coupon;
import com.nhnacademy.boostorecoupon.entity.MemberCoupon;
import com.nhnacademy.boostorecoupon.enums.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MemberCouponResponseDto(
        @Min(0)
        @NotNull
        Long memberCouponId, // 회원쿠폰 ID
        @Min(0)
        @NotNull
        Long memberId, // 회원 ID
        @NotNull
        CouponResponseDto coupon // 반환될 쿠폰관련 정보들
) {
    public static MemberCouponResponseDto fromEntity(MemberCoupon memberCoupon) {
        return new MemberCouponResponseDto(
                memberCoupon.getId(),
                memberCoupon.getMcMemberId(),
                CouponResponseDto.fromCoupon(memberCoupon.getCoupon())
        );
    }

    public record CouponResponseDto(
            Long couponId,
            String code,
            Status status,
            LocalDateTime issuedAt,
            LocalDateTime expiredAt,
            Long couponPolicyId,
            String name,
            String saleType,
            BigDecimal minimumAmount,
            BigDecimal discountLimit,
            Integer discountRatio,
            boolean isStackable,
            String couponScope,
            boolean couponActive
    ) {
        public static CouponResponseDto fromCoupon(Coupon coupon) {
            return new CouponResponseDto(
                    coupon.getId(),
                    coupon.getCode(),
                    coupon.getStatus(),
                    coupon.getIssuedAt(),
                    coupon.getExpiredAt(),
                    coupon.getCouponPolicy().getId(),
                    coupon.getCouponPolicy().getName(),
                    coupon.getCouponPolicy().getSaleType().toString(),
                    coupon.getCouponPolicy().getMinimumAmount(),
                    coupon.getCouponPolicy().getDiscountLimit(),
                    coupon.getCouponPolicy().getDiscountRatio(),
                    coupon.getCouponPolicy().isStackable(),
                    coupon.getCouponPolicy().getCouponScope(),
                    coupon.getCouponPolicy().isCouponActive()
            );
        }
    }
}
