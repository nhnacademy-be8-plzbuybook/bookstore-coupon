//package com.nhnacademy.boostorenginx.service.impl;
//
//import com.nhnacademy.boostorenginx.dto.CouponCreateDto;
//import com.nhnacademy.boostorenginx.entity.Coupon;
//import com.nhnacademy.boostorenginx.entity.CouponHistory;
//import com.nhnacademy.boostorenginx.entity.CouponPolicy;
//import com.nhnacademy.boostorenginx.enums.Status;
//import com.nhnacademy.boostorenginx.error.NotFoundCouponException;
//import com.nhnacademy.boostorenginx.error.NotFoundCouponPolicyException;
//import com.nhnacademy.boostorenginx.repository.CouponHistoryRepository;
//import com.nhnacademy.boostorenginx.repository.CouponRepository;
//import com.nhnacademy.boostorenginx.service.CouponPolicyService;
//import com.nhnacademy.boostorenginx.service.CouponService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class CouponServiceImpl implements CouponService {
//    private final CouponRepository couponRepository;
//    private final CouponPolicyService couponPolicyService;
//    private final CouponHistoryRepository couponHistoryRepository;
//
//    @Override
//    public Long registerCoupon(CouponCreateDto dto) {
//        CouponPolicy couponPolicy = couponPolicyService.findById(dto.getCouponPolicyId());
//        Coupon coupon = new Coupon(
//                Status.UNUSED, // 쿠폰 발급할때 상태는 무조건 UNUSED
//                LocalDateTime.now(), // 발급시간는 서버시간기준
//                dto.getExpiredAt(), // 만료시간은 요청 기준
//                couponPolicy
//        );
//        return couponRepository.save(coupon).getId();
//    }
//
//    @Override
//    public Coupon getCouponByCode(String code) {
//        return couponRepository.findByCode(code).orElseThrow(() -> new NotFoundCouponException("코드에 해당하는 쿠폰을 찾을 수 없습니다: " + code));
//    }
//
//    @Override
//    public List<Coupon> getExpiredCoupons(LocalDateTime currentDateTime) {
//        if (currentDateTime == null) {
//            throw new IllegalArgumentException("입력받은 currentDateTime 이 null 입니다");
//        }
//        return couponRepository.findByExpiredAtBefore(currentDateTime);
//    }
//
//    @Override
//    public List<Coupon> getActiveCoupons(LocalDateTime currentDateTime) {
//        if (currentDateTime == null) {
//            throw new IllegalArgumentException("입력받은 currentDateTime 이 null 입니다");
//        }
//        return couponRepository.findActiveCoupons(currentDateTime);
//    }
//
//    @Override
//    public List<Coupon> getCouponsByPolicy(CouponPolicy couponPolicy) {
//        if (couponPolicy == null) {
//            throw new NotFoundCouponPolicyException("입력받은 couponPolicy 가 null 입니다");
//        }
//        return couponRepository.findByCouponPolicy(couponPolicy);
//    }
//
//    @Override
//    public Page<Coupon> getCouponsByStatus(Status status, Pageable pageable) {
//        return couponRepository.findByStatus(status, pageable);
//    }
//
//    @Override
//    public void updateCouponStatus(String code, Status newStatus, String reason) {
//        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new NotFoundCouponException("코드에 해당하는 쿠폰을 찾을 수 없습니다: " + code));
//        coupon.setStatus(newStatus);
//        CouponHistory history = CouponHistory.builder()
//                .coupon(coupon)
//                .status(newStatus)
//                .changeDate(LocalDateTime.now())
//                .reason(reason)
//                .build();
//        couponHistoryRepository.save(history);
//    }
//
//}
