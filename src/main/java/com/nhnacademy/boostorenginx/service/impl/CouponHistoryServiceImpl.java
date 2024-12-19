//package com.nhnacademy.boostorenginx.service.impl;
//
//import com.nhnacademy.boostorenginx.entity.Coupon;
//import com.nhnacademy.boostorenginx.entity.CouponHistory;
//import com.nhnacademy.boostorenginx.enums.Status;
//import com.nhnacademy.boostorenginx.error.CouponHistoryException;
//import com.nhnacademy.boostorenginx.repository.CouponHistoryRepository;
//import com.nhnacademy.boostorenginx.service.CouponHistoryService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class CouponHistoryServiceImpl implements CouponHistoryService {
//
//    private final CouponHistoryRepository couponHistoryRepository;
//
//    @Override
//    public CouponHistory createHistory(Coupon coupon, Status status, String reason) {
//        try {
//            if (coupon == null || status == null || reason.isBlank()) {
//                throw new NullPointerException();
//            }
//            CouponHistory couponHistory = CouponHistory.builder()
//                    .coupon(coupon)
//                    .status(status)
//                    .changeDate(LocalDateTime.now())
//                    .reason(reason)
//                    .build();
//            return couponHistoryRepository.save(couponHistory);
//        } catch (NullPointerException e) {
//            throw new CouponHistoryException("잘못된 인수입니다 " + "coupon: " + coupon + "status: " + status + "reason: " + reason);
//        }
//    }
//
//    @Override
//    public List<CouponHistory> getHistoryByCoupon(Coupon coupon) {
//        if (coupon == null) {
//            throw new CouponHistoryException("Coupon 이 null 입니다");
//        }
//        return couponHistoryRepository.findByCoupon(coupon);
//    }
//
//    @Override
//    public List<CouponHistory> getHistoryByCouponId(Long couponId) {
//        try {
//            if (couponId < 0) {
//                throw new CouponHistoryException("잘못된 couponId 입니다: " + couponId);
//            }
//            return couponHistoryRepository.findByCoupon_id(couponId);
//        } catch (NullPointerException e) {
//            throw new CouponHistoryException("잘못된 couponId 입니다: " + couponId);
//        }
//    }
//
//    @Override
//    public List<CouponHistory> getHistoryByCouponAndStatus(Coupon coupon, Status status) {
//        if (coupon == null || status == null) {
//            throw new CouponHistoryException("잘못된 인수입니다 " + "coupon: " + coupon + "status: " + status);
//        }
//        return couponHistoryRepository.findByCouponAndStatus(coupon, status);
//    }
//}
