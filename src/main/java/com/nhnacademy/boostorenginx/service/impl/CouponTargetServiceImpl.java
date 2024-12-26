package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
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
    public CouponTargetResponseDto createCouponTarget(CouponTargetAddRequestDto dto) {

        CouponPolicy couponPolicy = couponPolicyRepository.findById(dto.policyId()).orElseThrow(
                () -> new NotFoundCouponPolicyException("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + dto.policyId())
        );

        if (couponTargetRepository.existsById(dto.ctTargetId())) {
            throw new CouponTargetException("이미 등록된 쿠폰 대상 입니다");
        }

        CouponTarget couponTarget = CouponTarget.builder()
                .couponPolicy(couponPolicy)
                .ctTargetId(dto.ctTargetId())
                .build();

        CouponTarget saveTarget = couponTargetRepository.save(couponTarget);

        return CouponTargetResponseDto.fromEntity(saveTarget);
    }
}
