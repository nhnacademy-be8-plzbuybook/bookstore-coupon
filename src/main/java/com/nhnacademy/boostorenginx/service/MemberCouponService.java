package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;

public interface MemberCouponService {

    void createMemberCoupon(MemberCouponCreateRequestDto dto); // 1. 멤버쿠폰 객체 생성 == 회원에게 쿠폰 발급

    void useMemberCoupon(MemberCouponUseRequestDto dto); // 2. 멤버가 쿠폰을 사용 -> 쿠폰 테이블의 Status 변경 -> couponService 를 호출해서 Coupon update
}

