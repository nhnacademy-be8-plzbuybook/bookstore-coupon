package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.membercoupon.*;
import org.springframework.data.domain.Page;

public interface MemberCouponService {

    MemberCouponResponseDto createMemberCoupon(MemberCouponCreateRequestDto dto); // 회원쿠폰 생성

    void useMemberCoupon(MemberCouponUseRequestDto dto); // 회원이 쿠폰을 사용

    Page<MemberCouponResponseDto> getMemberCouponsByMemberId(MemberCouponFindByMemberIdRequestDto requestDto); // 쿠폰의 ID로 회원쿠폰 조회

    Page<MemberCouponResponseDto> getMemberCouponsByCouponId(MemberCouponFindByCouponIdRequestDto requestDto); // 회원 ID에 해당되는 회원쿠폰 관계들을 모두 조회

    Page<MemberCouponResponseDto> getUnusedMemberCouponsByMemberId(MemberCouponFindByMemberIdRequestDto requestDto); // 회원 ID 와 Status 로 회원쿠폰 조회
}

