package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.sellingbook.SellingBookResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 페인클라이언트로 가져온 SellingBookResponseDto 을 가공하는 서비스로직
@RequiredArgsConstructor
@Service
public class SellingBookCouponService {
    private final CouponPolicyService couponPolicyService;
    private final CouponTargetService couponTargetService;
    private final CouponService couponService;

    // 쿠폰정책을 과 함꼐 입력받아서, 책에 적용가능한 쿠폰생성 -> 반환값: 쿠폰 ID
    public Long createCouponForSellingBook(SellingBookResponseDto sellingBookResponseDto,
                                           CouponPolicySaveRequestDto saveRequestDto) {
        Long couponPolicyId = couponPolicyService.createCouponPolicy(saveRequestDto);

        CouponTargetAddRequestDto addRequestDto = new CouponTargetAddRequestDto(couponPolicyId, sellingBookResponseDto.getSellingBookId());
        couponTargetService.createCouponTarget(addRequestDto);
        CouponCreateRequestDto createRequestDto = new CouponCreateRequestDto(couponPolicyId, saveRequestDto.endDate());
        return couponService.createCoupon(createRequestDto);
    }

}
