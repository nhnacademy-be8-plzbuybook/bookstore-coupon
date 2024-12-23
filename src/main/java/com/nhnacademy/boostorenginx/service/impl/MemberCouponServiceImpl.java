package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.MemberCoupon;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.error.CouponException;
import com.nhnacademy.boostorenginx.error.MemberCouponException;
import com.nhnacademy.boostorenginx.error.NotFoundCouponException;
import com.nhnacademy.boostorenginx.repository.CouponRepository;
import com.nhnacademy.boostorenginx.repository.MemberCouponRepository;
import com.nhnacademy.boostorenginx.service.CouponService;
import com.nhnacademy.boostorenginx.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberCouponServiceImpl implements MemberCouponService {
    private final MemberCouponRepository memberCouponRepository;
    private final CouponRepository couponRepository;
    private final CouponService couponService;

    @Override
    public void createMemberCoupon(MemberCouponCreateRequestDto dto) {
        Long memberId = dto.memberId();
        Long couponId = dto.couponId();

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new NotFoundCouponException("ID 에 해당하는 쿠폰이 존재하지 않습니다" + couponId)
        );

        if (memberCouponRepository.existsByMcMemberIdAndMemberCouponId(memberId, couponId)) {
            throw new MemberCouponException("회원에게 이미 발급된 쿠폰입니다");
        }

        MemberCoupon memberCoupon = new MemberCoupon(memberId, coupon);
        memberCouponRepository.save(memberCoupon);
    }

    @Override
    public void useMemberCoupon(MemberCouponUseRequestDto dto) {
        Long couponId = dto.couponId();
        Long memberId = dto.memberId();

        MemberCoupon memberCoupon = memberCouponRepository.findById(memberId)
                .orElseThrow( () -> new MemberCouponException("회원쿠폰 ID 에 해당되는 것을 찾을 수 없습니다: " + memberId));

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow( () -> new NotFoundCouponException("쿠폰 ID 에 해당되는 것을 찾을 수 없습니다: " + couponId));

        if (coupon.getStatus() != Status.UNUSED) {
            throw new CouponException("쿠폰이 사용불가능한 상태입니다: " + coupon.getStatus().toString());
        }

        couponService.useCoupon(dto); // 쿠폰의 Status 를 USED 로 업데이트하는 코드

        memberCouponRepository.save(memberCoupon);
    }
}
