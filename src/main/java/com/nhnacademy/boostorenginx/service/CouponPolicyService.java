//package com.nhnacademy.boostorenginx.service;
//
//import com.nhnacademy.boostorenginx.dto.CouponPolicySaveRequestDto;
//import com.nhnacademy.boostorenginx.entity.CouponPolicy;
//import com.nhnacademy.boostorenginx.entity.CouponTarget;
//import com.nhnacademy.boostorenginx.repository.CouponPolicyRepository;
//import com.nhnacademy.boostorenginx.repository.CouponTargetRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class CouponPolicyService {
//    private final CouponPolicyRepository couponPolicyRepository;
//    private final CouponTargetRepository couponTargetRepository;
//
//    public long createCouponPolicy(CouponPolicySaveRequestDto saveRequest) {
//        CouponPolicy couponPolicy = couponPolicyRepository.save(saveRequest.toEntity());
//        List<Long> targetIdList = saveRequest.targetIdList();
//
//        for (Long targetId: targetIdList) {
//            CouponTarget couponTarget = new CouponTarget(targetId);
//            couponTarget.setCouponPolicy(couponPolicy);
//            couponTargetRepository.save(couponTarget);
//        }
//
//        JPA에서는 연관 관계의 주인만이 데이터베이스에 변경 사항을 반영합니다.
//        for (Long targetId: targetIdList) {
//            CouponTarget couponTarget = couponTargetRepository.save(new CouponTarget(targetId));
//            couponPolicy.addCouponTarget(couponTarget);
//        }
//
//        return couponPolicy.getId();
//    }
//}
