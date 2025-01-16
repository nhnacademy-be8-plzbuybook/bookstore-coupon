package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.coupon.CouponResponseDto;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.refundcoupon.RefundCouponRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.error.NotFoundCouponException;
import com.nhnacademy.boostorenginx.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class RefundCouponServiceImpl implements RefundCouponService {
    private final CouponService couponService;
    private final MemberCouponService memberCouponService;

    @Override
    public void refundCoupon(RefundCouponRequestDto refundCouponRequestDto) {
        Long couponId = refundCouponRequestDto.getCouponId(); // 쿠폰 식별키
        Long mcMemberId = refundCouponRequestDto.getMemberId(); // 회원 식별키

        // 쿠폰 ID 에 해당하는 쿠폰이 있는지 검사
        if (!couponService.existsById(couponId)) {
            throw new NotFoundCouponException("쿠폰 ID 에 해당하는 쿠폰이 존재하지 않습니다: " + couponId);
        }

        // 쿠폰 ID 에 해당하는 쿠폰정보를 가져옴
        Coupon coupon = couponService.getCouponById(couponId);

        // 쿠폰이력 갱신
        coupon.changeStatus(Status.CANCEL, LocalDateTime.now(), "REFUND");

        // 새 쿠폰객체 생성
        CouponResponseDto couponResponseDto = couponService.createCoupon(new CouponCreateRequestDto(coupon.getCouponPolicy().getId(), coupon.getExpiredAt()));

        // 새 쿠폰객체를 회원에게 발급
        memberCouponService.createMemberCoupon(new MemberCouponCreateRequestDto(mcMemberId, couponResponseDto.id()));
    }
}
