package com.nhnacademy.bookstorecoupon.service.impl;

import com.nhnacademy.bookstorecoupon.dto.couponhistory.CouponHistoryDuringRequestDto;
import com.nhnacademy.bookstorecoupon.dto.couponhistory.CouponHistoryFindRequestDto;
import com.nhnacademy.bookstorecoupon.dto.couponhistory.CouponHistoryStatusRequestDto;
import com.nhnacademy.bookstorecoupon.entity.CouponHistory;
import com.nhnacademy.bookstorecoupon.enums.Status;
import com.nhnacademy.bookstorecoupon.error.CouponHistoryException;
import com.nhnacademy.bookstorecoupon.repository.CouponHistoryRepository;
import com.nhnacademy.bookstorecoupon.service.CouponHistoryService;
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
