package com.nhnacademy.boostorecoupon.service.impl;

import com.nhnacademy.boostorecoupon.dto.couponhistory.CouponHistoryDuringRequestDto;
import com.nhnacademy.boostorecoupon.dto.couponhistory.CouponHistoryFindRequestDto;
import com.nhnacademy.boostorecoupon.dto.couponhistory.CouponHistoryStatusRequestDto;
import com.nhnacademy.boostorecoupon.entity.CouponHistory;
import com.nhnacademy.boostorecoupon.enums.Status;
import com.nhnacademy.boostorecoupon.error.CouponHistoryException;
import com.nhnacademy.boostorecoupon.repository.CouponHistoryRepository;
import com.nhnacademy.boostorecoupon.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CouponHistoryServiceImpl implements CouponHistoryService {

    private final CouponHistoryRepository couponHistoryRepository;

    @Transactional(readOnly = true)
    @Override
    public Page<CouponHistory> getHistoryByCouponId(CouponHistoryFindRequestDto dto) {
        Pageable pageable = Pageable.ofSize(dto.pageSize()).withPage(dto.page());
        Long historyId = dto.couponHistoryId();
        if (historyId < 0) {
            throw new CouponHistoryException("잘못된 ID 입니다: " + historyId);
        }

        return couponHistoryRepository.findByCoupon_idOrderByCouponIdAsc(historyId, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CouponHistory> getHistoryByStatus(CouponHistoryStatusRequestDto dto) {
        Pageable pageable = Pageable.ofSize(dto.pageSize()).withPage(dto.page());
        Status status = Status.valueOf(dto.status());

        return couponHistoryRepository.findByStatusOrderByChangeDateAsc(status, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CouponHistory> getHistoryDate(CouponHistoryDuringRequestDto dto) {
        Pageable pageable = Pageable.ofSize(dto.pageSize()).withPage(dto.page());

        return couponHistoryRepository.findChangeDate(dto.start(), dto.end(), pageable);
    }
}
