package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.coupon.*;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import org.springframework.data.domain.Page;

public interface CouponService {

    CouponResponseDto createCoupon(CouponCreateRequestDto dto); // 쿠폰 생성

    Coupon getCouponByCode(CouponCodeRequestDto dto); // 쿠폰 코드로 조회

    Page<CouponResponseDto> getExpiredCoupons(CouponExpiredRequestDto dto); // 현재시각 기준으로 만료된 쿠폰목록 조회

    Page<CouponResponseDto> getActiveCoupons(CouponActiveRequestDto dto); // 현재시각 기준으로 기한이 남아있는 쿠폰목록 조회(현재 사용가능한 쿠폰 목록조회)

    Page<CouponResponseDto> getCouponsByPolicy(CouponFindCouponPolicyIdRequestDto dto); // 쿠폰정책 ID 로 쿠폰 조회

    Page<CouponResponseDto> getCouponsByStatus(CouponFindStatusRequestDto dto); // 상태별로 쿠폰 조회 페이징

    void updateExpiredCoupon(CouponUpdateExpiredRequestDto dto); // 만료된 쿠폰 상태 업데이트

    void useCoupon(MemberCouponUseRequestDto dto); // 회원이 쿠폰을 사용할 경우 업데이트

}
