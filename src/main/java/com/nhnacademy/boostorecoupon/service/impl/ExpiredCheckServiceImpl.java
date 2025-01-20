package com.nhnacademy.boostorecoupon.service.impl;

import com.nhnacademy.boostorecoupon.dto.coupon.CouponFindCouponPolicyIdRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupon.CouponUpdateExpiredRequestDto;
import com.nhnacademy.boostorecoupon.entity.Coupon;
import com.nhnacademy.boostorecoupon.enums.Status;
import com.nhnacademy.boostorecoupon.service.CouponPolicyService;
import com.nhnacademy.boostorecoupon.service.CouponService;
import com.nhnacademy.boostorecoupon.service.ExpiredCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExpiredCheckServiceImpl implements ExpiredCheckService {
    private final CouponPolicyService couponPolicyService;
    private final CouponService couponService;

    @Transactional
    @Override
    public void checkExpiredCoupon() {
        log.info("만료된 쿠폰 정책 조회 시작");
        List<Long> expiredPolicyIds = couponPolicyService.findExpiredCouponPolicies();
        if (expiredPolicyIds.isEmpty()) {
            log.info("만료된 쿠폰 정책 없음");
            return;
        }
        log.info("만료된 쿠폰 정책 수: {}", expiredPolicyIds.size());

        log.info("만료된 쿠폰 조회 및 상태 변경 시작");
        LocalDateTime now = LocalDateTime.now();
        expiredPolicyIds.forEach(policyId -> processExpiredCoupons(policyId, now));
        log.info("만료된 쿠폰 처리 완료");

    }

    private void processExpiredCoupons(Long policyId, LocalDateTime now) {
        int page = 0;
        int pageSize = 100;

        while (true) {
            Page<Coupon> coupons = couponService.getCouponsByPolicyId(new CouponFindCouponPolicyIdRequestDto(policyId, page, pageSize));

            couponService.updateExpiredCoupon(new CouponUpdateExpiredRequestDto(now, "EXPIRED", page, pageSize));

            coupons.forEach(coupon -> {
                coupon.changeStatus(Status.EXPIRED, now, "EXPIRED");
                log.info("쿠폰 ID: {} 상태가 EXPIRED로 변경되었습니다", coupon.getId());
            });

            if (coupons.isLast() || coupons.isEmpty()) {
                break;
            }
            page++;
        }
    }
}
