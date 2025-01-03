package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;

public interface SellingBookCouponService {

    void createCouponForSellingBook(String searchKeyword, CouponPolicySaveRequestDto couponPolicySaveRequestDto); // 도서쿠폰 발급

}
