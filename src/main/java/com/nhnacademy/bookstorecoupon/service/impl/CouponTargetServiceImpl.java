package com.nhnacademy.bookstorecoupon.service.impl;

import com.nhnacademy.bookstorecoupon.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.bookstorecoupon.dto.coupontarget.CouponTargetSearchRequestDto;
import com.nhnacademy.bookstorecoupon.repository.CouponTargetRepository;
import com.nhnacademy.bookstorecoupon.service.CouponTargetService;
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

    @Transactional(readOnly = true)
    @Override
    public Page<CouponTargetGetResponseDto> getCouponTargetsByPolicyId(CouponTargetSearchRequestDto couponTargetSearchRequestDto) {
        Long policyId = couponTargetSearchRequestDto.policyId();
        int page = couponTargetSearchRequestDto.page();
        int pageSize = couponTargetSearchRequestDto.pageSize();

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
