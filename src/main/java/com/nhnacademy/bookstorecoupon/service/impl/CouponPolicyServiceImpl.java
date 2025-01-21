package com.nhnacademy.bookstorecoupon.service.impl;

import com.nhnacademy.bookstorecoupon.dto.couponpolicy.*;
import com.nhnacademy.bookstorecoupon.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.bookstorecoupon.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.entity.CouponTarget;
import com.nhnacademy.bookstorecoupon.error.NotFoundCouponPolicyException;
import com.nhnacademy.bookstorecoupon.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.repository.CouponTargetRepository;
import com.nhnacademy.bookstorecoupon.service.CouponPolicyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponPolicyServiceImpl implements CouponPolicyService {
    private final CouponPolicyRepository couponPolicyRepository;
    private final CouponTargetRepository couponTargetRepository;

    @Transactional
    @Override
    public CouponPolicyResponseDto createCouponPolicy(@Valid CouponPolicySaveRequestDto requestDto) {
        CouponPolicy couponPolicy = couponPolicyRepository.save(requestDto.toEntity());
        couponPolicyRepository.flush();

        return CouponPolicyResponseDto.fromCouponPolicy(couponPolicy);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CouponPolicyResponseDto> findAllCouponPolicies(Pageable pageable) {
        Page<CouponPolicy> couponPolicies = couponPolicyRepository.findAll(pageable);

        if (couponPolicies.isEmpty()) {
            throw new NotFoundCouponPolicyException("쿠폰정책을 찾을 수 없습니다");
        }

        return couponPolicies.map(CouponPolicyResponseDto::fromCouponPolicy);
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
                () -> new NotFoundCouponPolicyException("쿠폰정책 ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + couponPolicyId)
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

    @Transactional(readOnly = true)
    @Override
    public CouponPolicyResponseDto findCouponPolicyById(Long couponId) {
        CouponPolicy couponPolicy = couponPolicyRepository.findCouponPolicyByCouponId(couponId).orElseThrow(
                () -> new NotFoundCouponPolicyException("쿠폰 ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + couponId)
        );

        return CouponPolicyResponseDto.fromCouponPolicy(couponPolicy);
    }

    @Transactional
    @Override
    public CouponTargetResponseDto addTargetToPolicy(CouponTargetAddRequestDto ctTargetAddRequestDto) {
        Long policyId = ctTargetAddRequestDto.policyId();
        Long ctTargetId = ctTargetAddRequestDto.ctTargetId();

        CouponPolicy couponPolicy = couponPolicyRepository.findById(policyId).orElseThrow(
                () -> new NotFoundCouponPolicyException("쿠폰정책 ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + policyId)
        );

        CouponTarget couponTarget = new CouponTarget();
        couponTarget.setCtTargetId(ctTargetId);
        couponPolicy.addCouponTarget(couponTarget);

        couponTargetRepository.save(couponTarget);

        return CouponTargetResponseDto.fromEntity(couponTarget);
    }

    @Transactional(readOnly = true)
    public List<Long> findExpiredCouponPolicies() {
        boolean couponActive = true;
        LocalDateTime now = LocalDateTime.now();
        int page = 0;
        int pageSize = 100;
        List<Long> ids = new ArrayList<>();

        while (true) {
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<CouponPolicy> expiredPolicies = couponPolicyRepository.findExpiredCouponPolicies(couponActive, now, pageable);

            expiredPolicies.forEach(policy -> {
                log.info("조회된 쿠폰정책 ID: {}", policy.getId());
                ids.add(policy.getId());
            });

            if (expiredPolicies.isLast() || expiredPolicies.isEmpty()) {
                break;
            }

            page++;
        }

        return ids;
    }

}
