package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.category.CategorySimpleResponseDto;
import com.nhnacademy.boostorenginx.dto.coupon.CouponCreateRequestDto;
import com.nhnacademy.boostorenginx.dto.coupon.CouponResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.feign.ShoppingMallClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryCouponService {
    private final CouponPolicyService couponPolicyService;
    private final CouponTargetService couponTargetService;
    private final CouponService couponService;

    private final ShoppingMallClient shoppingMallClient;

    // 카테고리 쿠폰 생성 및 발급
    @Transactional
    public void issueCategoryCoupon(String keyword, CouponPolicySaveRequestDto couponPolicySaveRequestDto) {

        // 쇼핑몰 서버에서 카테고리 정보(카테고리 ID, 카테고리 이름) 가져오기
        List<CategorySimpleResponseDto> categories = shoppingMallClient.searchCategories(keyword);

        // 카테고리별 쿠폰정책 생성
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.createCouponPolicy(couponPolicySaveRequestDto);
        Long couponPolicyId = couponPolicyResponseDto.id();

        for (CategorySimpleResponseDto category : categories) {
            // 카테고리별 쿠폰대상 생성
            CouponTargetAddRequestDto couponTargetAddRequestDto = new CouponTargetAddRequestDto(couponPolicyId, category.getCategoryId());
            CouponTargetResponseDto couponTargetResponseDto = couponTargetService.createCouponTarget(couponTargetAddRequestDto);

            // 카테고리별 쿠폰생성
            CouponCreateRequestDto couponCreateRequestDto = new CouponCreateRequestDto(couponPolicyId, couponPolicySaveRequestDto.endDate());
            CouponResponseDto couponResponseDto = couponService.createCoupon(couponCreateRequestDto);
            log.info("카테고리 쿠폰 발급 완료 CouponId = {}", couponResponseDto.id());
        }
    }

}
