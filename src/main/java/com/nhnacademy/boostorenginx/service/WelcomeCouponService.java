package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.welcome.WelComeCouponRequestDto;

public interface WelcomeCouponService {

    void issueWelcomeCoupon(WelComeCouponRequestDto requestDto); // Welcome 쿠폰 발급

}
