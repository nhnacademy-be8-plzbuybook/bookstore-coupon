package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
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


    // 쿠폰정책 생성
    @Override
    public Long createCouponPolicy(CouponPolicySaveRequestDto requestDto) {
        if (requestDto == null) {
            throw new NotFoundCouponPolicyException("요청 Dto 가 null 입니다");
        }
        CouponPolicy couponPolicy = couponPolicyRepository.save(requestDto.toEntity());
        return couponPolicy.getId();
    }

    @Override
    public void addCouponTargetList(Long couponPolicyId, List<Long> targetIdList) {
        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponPolicyId).orElseThrow(
                () -> new NotFoundCouponPolicyException("잘못된 쿠폰정책 ID: " + couponPolicyId));

        if (targetIdList != null) {
            List<CouponTarget> targetList = targetIdList.stream()
                    .map(targetId -> {
                        CouponTarget couponTarget = CouponTarget.builder().targetId(targetId).build();
                        couponTarget.setCouponPolicy(couponPolicy);
                        return couponTarget;
                    }).toList();
            couponTargetRepository.saveAll(targetList);
        }
    }

    // 이름으로 쿠폰정책 찾기
    @Override
    public CouponPolicy findByName(String name) {
        return couponPolicyRepository.findByName(name).orElseThrow(
                () -> new NotFoundCouponPolicyException("해당 이름의 쿠폰정책을 찾을 수 없습니다: " + name)
        );
    }
}
