package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import com.nhnacademy.boostorenginx.error.CouponTargetException;
import com.nhnacademy.boostorenginx.error.NotFoundCouponPolicyException;
import com.nhnacademy.boostorenginx.repository.CouponPolicyRepository;
import com.nhnacademy.boostorenginx.repository.CouponTargetRepository;
import com.nhnacademy.boostorenginx.service.CouponTargetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class CouponTargetServiceImpl implements CouponTargetService {

    private final CouponTargetRepository couponTargetRepository;
    private final CouponPolicyRepository couponPolicyRepository;

    @Override
    public void createCouponTarget(CouponTargetAddRequestDto dto) {
        CouponPolicy couponPolicy = couponPolicyRepository.findById(dto.policyId()).orElseThrow(
                () -> new NotFoundCouponPolicyException("존재하지 않은 쿠폰 정책입니다: " + dto.policyId())
        );

        if (couponTargetRepository.findById(dto.targetId()).isPresent()) {
            throw new CouponTargetException("이미 등록된 쿠폰 대상 입니다");
        }

        CouponTarget couponTarget = CouponTarget.builder()
                .couponPolicy(couponPolicy)
                .targetId(dto.targetId())
                .build();

        couponTargetRepository.save(couponTarget);
    }
}
