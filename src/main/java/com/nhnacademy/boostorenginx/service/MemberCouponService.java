package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.membercoupon.*;
import org.springframework.data.domain.Page;

public interface MemberCouponService {

    MemberCouponResponseDto createMemberCoupon(MemberCouponCreateRequestDto dto); // 회원쿠폰 생성

    void useMemberCoupon(MemberCouponUseRequestDto dto); // 회원이 쿠폰을 사용

    Page<MemberCouponResponseDto> getMemberCouponsByCouponId(MemberCouponFindByCouponIdRequestDto requestDto); // 쿠폰 ID 로 회원쿠폰 목록 조회

    Page<MemberCouponGetResponseDto> getMemberCouponsByMemberId(MemberCouponFindByMemberIdRequestDto requestDto); // 회원 ID 에 연관된 회원쿠폰 목록 조회

    Page<MemberCouponGetResponseDto> getUnusedMemberCouponsByMemberId(MemberCouponFindByMemberIdRequestDto requestDto); // 회원 ID 와 Status 로 회원쿠폰 조회
}

