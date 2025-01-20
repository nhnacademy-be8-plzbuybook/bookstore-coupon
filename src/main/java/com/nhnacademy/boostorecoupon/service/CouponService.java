package com.nhnacademy.boostorecoupon.service;

import com.nhnacademy.boostorecoupon.dto.coupon.*;
import com.nhnacademy.boostorecoupon.entity.Coupon;
import com.nhnacademy.boostorecoupon.entity.CouponPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponService {

    CouponResponseDto createCoupon(CouponCreateRequestDto dto); // 쿠폰 생성

    Page<CouponResponseDto> getAllCoupons(Pageable pageable); // 모든 쿠폰 조회

    Coupon getCouponById(Long couponId); // 쿠폰 ID 로 쿠폰 조회 (coupon)

    CouponResponseDto findCouponById(Long couponId); // 쿠폰 ID 로 쿠폰 조회 (dto)

    CouponPolicy findCouponPolicyByCouponId(Long couponId); // 쿠폰 ID 로 쿠폰에 해당하는 쿠폰정책 조회

    Page<Coupon> getCouponsByPolicyId(CouponFindCouponPolicyIdRequestDto dto);

    Page<CouponResponseDto> getCouponsByPolicy(CouponFindCouponPolicyIdRequestDto dto); // 쿠폰정책 ID 로 쿠폰목록 조회

    Page<CouponResponseDto> getCouponsByStatus(CouponFindStatusRequestDto dto); // 쿠폰상태로 쿠폰목록 조회

    void useCoupon(Long couponId); // 회원이 쿠폰을 사용할 경우 쿠폰 상태 업데이트

    void updateExpiredCoupon(CouponUpdateExpiredRequestDto dto); // 만료된 쿠폰 상태 업데이트

    boolean existsById(Long couponId); // 쿠폰 ID 에 해당하는 쿠폰정책이 존재 여부 판별

}
