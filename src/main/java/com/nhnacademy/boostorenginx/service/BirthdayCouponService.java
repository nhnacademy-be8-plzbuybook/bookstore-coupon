package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.birthday.BirthdayCouponRequestDto;

public interface BirthdayCouponService {

    void issueBirthdayCoupon(BirthdayCouponRequestDto requestDto); // 생일쿠폰 발급

}
