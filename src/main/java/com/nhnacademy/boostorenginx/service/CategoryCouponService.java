package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.category.CategorySimpleResponseDto;
import com.nhnacademy.boostorenginx.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.coupon.CouponResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponCreateRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.SaleType;
import com.nhnacademy.boostorenginx.feign.ShoppingMallClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryCouponService {
    private final CouponPolicyService couponPolicyService;
    private final CouponTargetService couponTargetService;
    private final MemberCouponService memberCouponService;
    private final CouponService couponService;

    private final ShoppingMallClient shoppingMallClient;

    // 카테고리 쿠폰 생성 및 발급 -> 나중에 dto 정리
    @Transactional
    public void issueCategoryCoupon(
            Long memberId, String keyword, SaleType saleType, BigDecimal minimumAmount, BigDecimal discountLimit, Integer discountRatio, boolean isStackable, LocalDateTime startDate ,LocalDateTime endDate) {

        // 쇼핑몰 서버에서 카테고리 정보 가져오기
        List<CategorySimpleResponseDto> categories = shoppingMallClient.searchCategories(keyword);

        // 카테고리별로 쿠폰발급
        categories.forEach(category -> {
            // 쿠폰 정책 생성
            CouponPolicySaveRequestDto couponPolicySaveRequestDto = new CouponPolicySaveRequestDto(
                    category.getCategoryName() + "_" + "COUPON",
                    saleType,
                    minimumAmount,
                    discountLimit,
                    discountRatio,
                    isStackable,
                    "CATEGORY",
                    startDate,
                    endDate,
                    true
            );
            CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);

            // 쿠폰 대상 생성
            CouponTargetAddRequestDto couponTargetAddRequestDto = new CouponTargetAddRequestDto(couponPolicyResponseDto.id(), memberId);
            couponTargetService.createCouponTarget(couponTargetAddRequestDto);

            // 쿠폰 생성
            CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto(couponPolicyResponseDto.id(), startDate);
            CouponResponseDto couponResponseDto = couponService.createCoupon(couponCreateRequestDto);

            // 회원쿠폰 발급
            MemberCouponCreateRequestDto memberCouponCreateRequestDto = new MemberCouponCreateRequestDto(
                    memberId,
                    couponResponseDto.id(),
                    0,
                    1
            );
            memberCouponService.createMemberCoupon(memberCouponCreateRequestDto);
        });
    }

}
