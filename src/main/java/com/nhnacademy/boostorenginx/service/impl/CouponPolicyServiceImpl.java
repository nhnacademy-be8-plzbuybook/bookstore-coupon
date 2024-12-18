package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.CouponPolicyNameRequestDto;
import com.nhnacademy.boostorenginx.dto.CouponPolicyResponseDto;
import com.nhnacademy.boostorenginx.dto.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import com.nhnacademy.boostorenginx.error.CouponPolicyException;
import com.nhnacademy.boostorenginx.error.NotFoundCouponPolicyException;
import com.nhnacademy.boostorenginx.repository.CouponPolicyRepository;
import com.nhnacademy.boostorenginx.repository.CouponTargetRepository;
import com.nhnacademy.boostorenginx.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponPolicyServiceImpl implements CouponPolicyService {
    private final CouponPolicyRepository couponPolicyRepository;
    private final CouponTargetRepository couponTargetRepository;

    @Override
    public Long createCouponPolicy(CouponPolicySaveRequestDto requestDto) {
        if (requestDto == null) {
            throw new NotFoundCouponPolicyException("requestDto is null");
        }
        CouponPolicy couponPolicy = couponPolicyRepository.save(requestDto.toEntity());
        return couponPolicy.getId();
    }

    @Override
    public CouponPolicy findByName(CouponPolicyNameRequestDto requestDto) {
        String name = requestDto.couponPolicyName();
        return couponPolicyRepository.findByName(name).orElseThrow(() -> new NotFoundCouponPolicyException("해당 이름의 CouponPolicy 를 찾을 수 없습니다"));
    }

    @Override
    public CouponPolicy findById(Long couponPolicyId) {
        return couponPolicyRepository.findById(couponPolicyId).orElseThrow(() -> new NotFoundCouponPolicyException("해당 ID 의 CouponPolicy 를 찾을 수 없습니다"));
    }

    @Override
    public void addTargetToPolicy(CouponTargetAddRequestDto requestDto) {
        CouponPolicy couponPolicy = couponPolicyRepository.findById(requestDto.policyId())
                .orElseThrow( () -> new NotFoundCouponPolicyException("잘못된 쿠폰정책 ID: " + requestDto.policyId()));

        CouponTarget couponTarget = new CouponTarget();
        couponTarget.setTargetId(requestDto.targetId());
        couponPolicy.addCouponTarget(couponTarget);
        couponTargetRepository.save(couponTarget);
    }

}
