package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;

public interface CategoryCouponService {

    void issueCategoryCoupon(String keyword, CouponPolicySaveRequestDto couponPolicySaveRequestDto); // 카테고리쿠폰 발급

}
