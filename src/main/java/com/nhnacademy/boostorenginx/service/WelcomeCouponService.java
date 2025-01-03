package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.welcome.WelcomeCouponRequestDto;

public interface WelcomeCouponService {

    void issueWelcomeCoupon(WelcomeCouponRequestDto requestDto); // Welcome 쿠폰 발급

}
