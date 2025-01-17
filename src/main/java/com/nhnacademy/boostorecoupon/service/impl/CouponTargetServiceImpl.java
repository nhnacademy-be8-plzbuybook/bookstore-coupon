package com.nhnacademy.boostorecoupon.service.impl;

import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetSaveRequestDto;
import com.nhnacademy.boostorecoupon.dto.coupontarget.CouponTargetSearchRequestDto;
import com.nhnacademy.boostorecoupon.entity.CouponPolicy;
import com.nhnacademy.boostorecoupon.entity.CouponTarget;
import com.nhnacademy.boostorecoupon.error.NotFoundCouponPolicyException;
import com.nhnacademy.boostorecoupon.repository.CouponPolicyRepository;
import com.nhnacademy.boostorecoupon.repository.CouponTargetRepository;
import com.nhnacademy.boostorecoupon.service.CouponTargetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CouponTargetServiceImpl implements CouponTargetService {

    private final CouponTargetRepository couponTargetRepository;
    private final CouponPolicyRepository couponPolicyRepository;

    @Transactional
    @Override
    public CouponTargetResponseDto createCouponTarget(CouponTargetSaveRequestDto couponTargetSaveRequestDto) {
        Long couponPolicyId = couponTargetSaveRequestDto.couponPolicyId();
        Long ctTargetId = couponTargetSaveRequestDto.ctTargetId();

        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponPolicyId).orElseThrow(
                () -> new NotFoundCouponPolicyException("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + couponPolicyId)
        );

        CouponTarget couponTarget = CouponTarget.builder()
                .couponPolicy(couponPolicy)
                .ctTargetId(ctTargetId)
                .build();

        CouponTarget saveTarget = couponTargetRepository.save(couponTarget);

        couponPolicyRepository.flush();

        return CouponTargetResponseDto.fromEntity(saveTarget);
    }

    @Transactional
    @Override
    public Page<CouponTargetGetResponseDto> getCouponTargetsByPolicyId(CouponTargetSearchRequestDto couponTargetSeearchReqeustDto) {
        Long policyId = couponTargetSeearchReqeustDto.policyId();
        int page = couponTargetSeearchReqeustDto.page();
        int pageSize = couponTargetSeearchReqeustDto.pageSize();

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").ascending());

        return couponTargetRepository.findByCouponPolicy_IdOrderByIdAsc(policyId, pageable)
                .map(couponTarget -> new CouponTargetGetResponseDto(
                        couponTarget.getId(),
                        couponTarget.getCtTargetId(),
                        couponTarget.getCouponPolicy().getId(),
                        couponTarget.getCouponPolicy().getCouponScope()
                ));
    }
}
