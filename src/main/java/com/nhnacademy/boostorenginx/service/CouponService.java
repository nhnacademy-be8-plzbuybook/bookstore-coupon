package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.coupon.*;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponService {

    Coupon getCouponByCode(CouponCodeRequestDto dto); // 쿠폰 코드로 조회

    CouponPolicy findCouponPolicyByCouponId(Long couponId); // 쿠폰 ID 로 쿠폰에 해당하는 쿠폰정책 조회

    CouponResponseDto createCoupon(CouponCreateRequestDto dto); // 쿠폰 생성

    Page<CouponResponseDto> getExpiredCoupons(CouponExpiredRequestDto dto); // 현재시각 기준으로 만료된 쿠폰목록 조회

    Page<CouponResponseDto> getActiveCoupons(CouponActiveRequestDto dto); // 현재시각 기준으로 기한이 남아있는 쿠폰목록 조회 (현재 사용가능한 쿠폰 목록조회)

    Page<CouponResponseDto> getCouponsByPolicy(CouponFindCouponPolicyIdRequestDto dto); // 쿠폰정책 ID 로 쿠폰목록 조회

    Page<CouponResponseDto> getCouponsByStatus(CouponFindStatusRequestDto dto); // 쿠폰상태로 쿠폰목록 조회

    void updateExpiredCoupon(CouponUpdateExpiredRequestDto dto); // 만료된 쿠폰 상태 업데이트

    void useCoupon(MemberCouponUseRequestDto dto); // 회원이 쿠폰을 사용할 경우 쿠폰 상태 업데이트

    Page<CouponResponseDto> getAllCoupons(Pageable pageable); // 모든 쿠폰 조회
}
