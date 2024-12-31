package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryDuringRequestDto;
import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryFindRequestDto;
import com.nhnacademy.boostorenginx.dto.couponhistory.CouponHistoryStatusRequestDto;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.error.CouponHistoryException;
import com.nhnacademy.boostorenginx.repository.CouponHistoryRepository;
import com.nhnacademy.boostorenginx.service.CouponHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CouponHistoryServiceImpl implements CouponHistoryService {

    private final CouponHistoryRepository couponHistoryRepository;

    @Override
    public Page<CouponHistory> getHistoryByCouponId(CouponHistoryFindRequestDto dto) {
        Pageable pageable = Pageable.ofSize(dto.pageSize()).withPage(dto.page());
        Long historyId = dto.couponHistoryId();
        if (historyId < 0) {
            throw new CouponHistoryException("잘못된 ID 입니다: " + historyId);
        }
        return couponHistoryRepository.findByCoupon_idOrderByCouponIdAsc(historyId, pageable);
    }

    @Override
    public Page<CouponHistory> getHistoryByStatus(CouponHistoryStatusRequestDto dto) {
        Pageable pageable = Pageable.ofSize(dto.pageSize()).withPage(dto.page());
        Status status = Status.valueOf(dto.status());
        return couponHistoryRepository.findByStatusOrderByChangeDateAsc(status, pageable);
    }

    @Override
    public Page<CouponHistory> getHistoryDate(CouponHistoryDuringRequestDto dto) {
        Pageable pageable = Pageable.ofSize(dto.pageSize()).withPage(dto.page());
        return couponHistoryRepository.findChangeDate(dto.start(), dto.end(), pageable);
    }
}
