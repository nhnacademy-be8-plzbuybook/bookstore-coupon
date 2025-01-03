package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetSaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetSeearchReqeustDto;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.entity.CouponTarget;
import com.nhnacademy.boostorenginx.error.CouponTargetException;
import com.nhnacademy.boostorenginx.error.NotFoundCouponPolicyException;
import com.nhnacademy.boostorenginx.repository.CouponPolicyRepository;
import com.nhnacademy.boostorenginx.repository.CouponTargetRepository;
import com.nhnacademy.boostorenginx.service.CouponTargetService;
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

        if (couponTargetRepository.existsByCtTargetId(ctTargetId)) {
            throw new CouponTargetException("이미 등록된 쿠폰 대상 입니다");
        }

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
    public Page<CouponTargetGetResponseDto> getCouponTargetsByPolicyId(CouponTargetSeearchReqeustDto couponTargetSeearchReqeustDto) {
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
