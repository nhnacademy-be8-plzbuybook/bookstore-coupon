package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.couponpolicy.*;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import com.nhnacademy.boostorenginx.error.CouponPolicyException;
import com.nhnacademy.boostorenginx.error.NotFoundCouponPolicyException;
import com.nhnacademy.boostorenginx.repository.CouponPolicyRepository;
import com.nhnacademy.boostorenginx.repository.CouponTargetRepository;
import com.nhnacademy.boostorenginx.service.CouponPolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponPolicyServiceImpl implements CouponPolicyService {
    private final CouponPolicyRepository couponPolicyRepository;
    private final CouponTargetRepository couponTargetRepository;

    @Transactional
    @Override
    public CouponPolicyResponseDto createCouponPolicy(CouponPolicySaveRequestDto requestDto) {
        if (requestDto == null) {
            throw new CouponPolicyException("requestDto 가 null 입니다!");
        }

        CouponPolicy couponPolicy = couponPolicyRepository.save(requestDto.toEntity());
        couponPolicyRepository.flush();

        return CouponPolicyResponseDto.fromCouponPolicy(couponPolicy);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CouponPolicyResponseDto> findActiveCouponPolicy(CouponPolicyActiveRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.page(), requestDto.pageSize());
        return couponPolicyRepository.findByCouponActiveOrderByIdAsc(requestDto.couponActive(), pageable).map(CouponPolicyResponseDto::fromCouponPolicy);
    }

    @Transactional(readOnly = true)
    @Override
    public CouponPolicyResponseDto findById(CouponPolicyIdRequestDto couponPolicyIdRequestDto) {
        Long couponPolicyId = couponPolicyIdRequestDto.id();

        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponPolicyId).orElseThrow(
                () -> new NotFoundCouponPolicyException("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + couponPolicyId)
        );

        return CouponPolicyResponseDto.fromCouponPolicy(couponPolicy);
    }

    @Transactional(readOnly = true)
    @Override
    public CouponPolicyResponseDto findByName(CouponPolicyNameRequestDto couponPolicyNameRequestDto) {
        String couponPolicyName = couponPolicyNameRequestDto.name();

        CouponPolicy couponPolicy = couponPolicyRepository.findByName(couponPolicyName).orElseThrow(
                () -> new NotFoundCouponPolicyException("이름에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + couponPolicyName)
        );

        return CouponPolicyResponseDto.fromCouponPolicy(couponPolicy);
    }

    @Transactional
    @Override
    public void addTargetToPolicy(CouponTargetAddRequestDto ctTargetAddRequestDto) {
        Long policyId = ctTargetAddRequestDto.policyId();
        Long ctTargetId = ctTargetAddRequestDto.ctTargetId();

        CouponPolicy couponPolicy = couponPolicyRepository.findById(policyId).orElseThrow(
                () -> new NotFoundCouponPolicyException("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + policyId)
        );

        CouponTarget couponTarget = new CouponTarget();
        couponTarget.setCtTargetId(ctTargetId);
        couponPolicy.addCouponTarget(couponTarget);

        couponTargetRepository.save(couponTarget);
    }
}
