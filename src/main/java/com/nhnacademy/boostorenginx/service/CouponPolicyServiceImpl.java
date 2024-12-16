package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import com.nhnacademy.boostorenginx.repository.CouponPolicyRepository;
import com.nhnacademy.boostorenginx.repository.CouponTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponPolicyServiceImpl implements CouponPolicyService {
    private final CouponPolicyRepository couponPolicyRepository;
    private final CouponTargetRepository couponTargetRepository;

    @Override
    public Long createCouponPolicy(CouponPolicySaveRequestDto requestDto, List<Long> targetIdList) {
        CouponPolicy couponPolicy = couponPolicyRepository.save(requestDto.toEntity());
        if (targetIdList != null) {
            List<CouponTarget> targetList = targetIdList.stream().map(targetId -> {
                CouponTarget couponTarget = CouponTarget.builder().targetId(targetId).build();
                couponTarget.setCouponPolicy(couponPolicy);
                return couponTarget;
            }).toList();
            couponTargetRepository.saveAll(targetList);
        }
        return couponPolicy.getId();
    }
}
