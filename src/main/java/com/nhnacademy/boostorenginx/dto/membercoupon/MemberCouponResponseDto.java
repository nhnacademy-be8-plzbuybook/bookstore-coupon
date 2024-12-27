package com.nhnacademy.boostorenginx.dto.membercoupon;

import com.nhnacademy.boostorenginx.dto.coupon.CouponResponseDto;
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
            Long id,
            String code,
            Status status,
            LocalDateTime issuedAt,
            LocalDateTime expiredAt,
            String name,
            BigDecimal discountLimit,
            Integer discountRatio
    ) {
        public static CouponResponseDto fromCoupon(Coupon coupon) {
            return new CouponResponseDto(
                    coupon.getId(),
                    coupon.getCode(),
                    coupon.getStatus(),
                    coupon.getIssuedAt(),
                    coupon.getExpiredAt(),
                    coupon.getCouponPolicy().getName(),
                    coupon.getCouponPolicy().getDiscountLimit(),
                    coupon.getCouponPolicy().getDiscountRatio()
            );
        }
    }
}
