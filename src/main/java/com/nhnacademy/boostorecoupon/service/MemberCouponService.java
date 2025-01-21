package com.nhnacademy.boostorecoupon.service;

import com.nhnacademy.boostorecoupon.dto.membercoupon.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberCouponService {

    MemberCouponResponseDto createMemberCoupon(MemberCouponCreateRequestDto dto); // 회원쿠폰 생성

    Page<MemberCouponResponseDto> getAllMemberCoupons(Pageable pageable); // 모든 회원쿠폰 목록 조회

    Page<MemberCouponResponseDto> getMemberCouponsByCouponId(MemberCouponFindByCouponIdRequestDto requestDto); // 쿠폰 ID 로 회원쿠폰 목록 조회

    Page<MemberCouponGetResponseDto> getMemberCouponsByMemberId(MemberCouponFindByMemberIdRequestDto requestDto); // 회원 ID 에 연관된 회원쿠폰 목록 조회

    void useMemberCoupon(MemberCouponUseRequestDto dto); // 회원이 자신이 보유한 쿠폰 사용

    Page<MemberCouponGetResponseDto> getUnusedMemberCouponsByMemberId(MemberCouponFindByMemberIdRequestDto requestDto); // 회원 ID 와 Status 로 회원쿠폰 조회

}

