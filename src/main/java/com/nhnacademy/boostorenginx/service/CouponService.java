package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.coupon.*;
import com.nhnacademy.boostorenginx.entity.Coupon;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface CouponService {

    Long createCoupon(CouponCreateRequestDto dto); // 쿠폰 생성 -> 쿠폰번호 반환

    Optional<Coupon> getCouponByCode(CouponCodeRequestDto dto); // 쿠폰 코드로 조회

    Page<Coupon> getExpiredCoupons(CouponExpiredRequestDto dto); // 현재시각 기준으로 만료된 쿠폰목록 조회

    Page<Coupon> getActiveCoupons(CouponActiveRequestDto dto); // 현재시각 기준으로 기한이 남아있는 쿠폰목록 조회(사실상 사용가능한 쿠폰 목록조회)

    Page<Coupon> getCouponsByPolicy(CouponFindCouponPolicyIdRequestDto dto); // 쿠폰정책 ID 로 쿠폰 조회

    Page<Coupon> getCouponsByStatus(CouponFindStatusRequestDto dto); // 상태별로 쿠폰 조회 페이징

    void updateExpiredCoupon(CouponUpdateExpiredRequestDto dto);

    void useCoupon(CouponUseRequestDto dto);

}
