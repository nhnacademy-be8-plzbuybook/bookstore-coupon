package com.nhnacademy.boostorenginx.dto.membercoupon;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.MemberCoupon;
import com.nhnacademy.boostorenginx.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MemberCouponResponseDto(
        Long memberCouponId,
        Long memberId,
        CouponResponseDto coupon
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
