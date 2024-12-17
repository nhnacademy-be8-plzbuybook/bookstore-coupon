package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

// 생일쿠폰, 회원가입 쿠폰 등은 별개의 서비스로 제작
public interface CouponService {
    Coupon getCouponByCode(String code); // 쿠폰 코드로 조회

    List<Coupon> getExpiredCoupons(LocalDateTime currentDateTime); // 만료된 쿠폰목록 조회

    List<Coupon> getActiveCoupons(LocalDateTime currentDateTime); // 기한이 남아있는 쿠폰목록 조회

    List<Coupon> getCouponsByPolicy(CouponPolicy couponPolicy); // 쿠폰정책 으로 쿠폰목록 조회

    Page<Coupon> getCouponsByStatus(Status status, Pageable pageable); // 상태별로 쿠폰 조회 페이징

    void updateCouponStatus(String code, Status newStatus, String reason); // 쿠폰의 상태가 변경될때, CouponHistory 생성
}