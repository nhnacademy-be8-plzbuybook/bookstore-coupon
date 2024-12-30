package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyIdRequestDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyNameRequestDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyResponseDto;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicySaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetAddRequestDto;
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
        log.info("저장된 쿠폰정책 아이디: {}", couponPolicy.getId());
        return CouponPolicyResponseDto.fromCouponPolicy(couponPolicy);
    }

    @Override
    public CouponPolicyResponseDto findByName(CouponPolicyNameRequestDto requestDto) {
        if (requestDto == null) {
            throw new CouponPolicyException("requestDto 가 null 입니다!");
        }

        CouponPolicy couponPolicy = couponPolicyRepository.findByName(requestDto.couponPolicyName()).orElseThrow(
                () -> new NotFoundCouponPolicyException("이름에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + requestDto.couponPolicyName())
        );

        return CouponPolicyResponseDto.fromCouponPolicy(couponPolicy);
    }

    @Override
    public CouponPolicyResponseDto findById(CouponPolicyIdRequestDto requestDto) {
        if (requestDto == null) {
            throw new CouponPolicyException("requestDto 가 null 입니다!");
        }

        CouponPolicy couponPolicy = couponPolicyRepository.findById(requestDto.couponPolicyId()).orElseThrow(
                () -> new NotFoundCouponPolicyException("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + requestDto.couponPolicyId())
        );

        return CouponPolicyResponseDto.fromCouponPolicy(couponPolicy);
    }

    @Override
    public Page<CouponPolicyResponseDto> findActiveCouponPolicy(boolean couponActive, Pageable pageable) {
        return couponPolicyRepository.findByCouponActiveOrderByIdAsc(true, pageable).map(CouponPolicyResponseDto::fromCouponPolicy);
    }

    @Override
    public void addTargetToPolicy(CouponTargetAddRequestDto requestDto) {
        if (requestDto == null) {
            throw new CouponPolicyException("requestDto 가 null 입니다!");
        }

        CouponPolicy couponPolicy = couponPolicyRepository.findById(requestDto.policyId()).orElseThrow(
                () -> new NotFoundCouponPolicyException("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + requestDto.policyId())
        );

        // 쿠폰대상 생성 및 저장
        CouponTarget couponTarget = new CouponTarget();
        couponTarget.setCtTargetId(requestDto.ctTargetId());
        couponPolicy.addCouponTarget(couponTarget);

        couponTargetRepository.save(couponTarget);
    }
}
