package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.membercoupon.*;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberCouponServiceImpl implements MemberCouponService {
    private final MemberCouponRepository memberCouponRepository;
    private final CouponRepository couponRepository;
    private final CouponService couponService;

    @Transactional
    @Override
    public MemberCouponResponseDto createMemberCoupon(MemberCouponCreateRequestDto dto) {
        Long memberId = dto.memberId();
        Long couponId = dto.couponId();

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new NotFoundCouponException("ID 에 해당하는 쿠폰이 존재하지 않습니다: " + couponId)
        );

        if (memberCouponRepository.existsByMcMemberIdAndId(memberId, couponId)) {
            throw new MemberCouponException("회원에게 이미 발급된 쿠폰입니다");
        }

        MemberCoupon memberCoupon = new MemberCoupon(memberId, coupon);
        MemberCoupon saveMemberCoupon = memberCouponRepository.save(memberCoupon);

        return MemberCouponResponseDto.fromEntity(saveMemberCoupon);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MemberCouponResponseDto> getMemberCouponsByCouponId(MemberCouponFindByCouponIdRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.page(), requestDto.pageSize());
        return memberCouponRepository.findByCoupon_IdOrderByIdAsc(requestDto.couponId(), pageable)
                .map(MemberCouponResponseDto::fromEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MemberCouponGetResponseDto> getMemberCouponsByMemberId(MemberCouponFindByMemberIdRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.page(), requestDto.pageSize());

        return memberCouponRepository.findByMcMemberIdOrderByIdAsc(requestDto.memberId(), pageable)
                .map(this::fromEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MemberCouponGetResponseDto> getUnusedMemberCouponsByMemberId(MemberCouponFindByMemberIdRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.page(), requestDto.pageSize());

        return memberCouponRepository.findByMcMemberIdAndCoupon_StatusOrderByIdAsc(requestDto.memberId(), Status.UNUSED, pageable)
                .map(this::fromEntity);
    }

    private MemberCouponGetResponseDto fromEntity(MemberCoupon memberCoupon) {
        Coupon coupon = memberCoupon.getCoupon();
        CouponPolicy couponPolicy = coupon.getCouponPolicy();

        return new MemberCouponGetResponseDto(
                coupon.getCode(),
                coupon.getStatus().name(),
                coupon.getIssuedAt(),
                coupon.getExpiredAt(),
                couponPolicy.getName(),
                couponPolicy.getSaleType().name(),
                couponPolicy.getMinimumAmount(),
                couponPolicy.getDiscountLimit(),
                couponPolicy.getDiscountRatio(),
                couponPolicy.isStackable(),
                couponPolicy.getCouponScope()
        );
    }

    @Transactional
    @Override
    public void useMemberCoupon(MemberCouponUseRequestDto dto) {
        Long couponId = dto.couponId();
        Long memberId = dto.memberId();

        MemberCoupon memberCoupon = memberCouponRepository.findByMcMemberIdAndCoupon_Id(memberId, couponId).orElseThrow(
                () -> new MemberCouponException("회원쿠폰 ID 에 해당되는 것을 찾을 수 없습니다: " + memberId)
        );

        Coupon coupon = memberCoupon.getCoupon();

        if (coupon.getStatus() != Status.UNUSED) {
            throw new CouponException("쿠폰이 사용불가능한 상태입니다: " + coupon.getStatus().toString());
        }


        couponService.useCoupon(dto);
        memberCouponRepository.save(memberCoupon);
    }
}
