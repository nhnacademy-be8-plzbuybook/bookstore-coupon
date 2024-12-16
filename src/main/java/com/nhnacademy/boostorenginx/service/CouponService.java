package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.entity.Coupon;

public interface CouponService {

    void createCoupon(Coupon coupon); // 쿠폰생성
    void issueCouponToMember(); // 회원에게 쿠폰발급 -> 회원번호 어떻게 가져올건데... -> 임시로 회원객체를 만들어서 테스트
    // 쿠폰 수정(전체 수정) ->
    // 쿠폰번호로 조회 ->
    // 쿠폰코드로 쿠폰 조회?
    // 쿠폰 리스트 조회 -> 페이지블 설정 필요
    // 회원아이디로 쿠폰리스트조회 -> 이것도 임시로 회원객체 만들어서 테스트
    // 쿠폰 사용 -> '쿠폰사용여부' 필드 수정
}
