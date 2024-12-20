package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.coupon.*;
import com.nhnacademy.boostorenginx.dto.couponpolicy.CouponPolicyIdRequestDto;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.error.CouponException;
import com.nhnacademy.boostorenginx.error.NotFoundCouponException;
import com.nhnacademy.boostorenginx.error.NotFoundCouponPolicyException;
import com.nhnacademy.boostorenginx.repository.CouponHistoryRepository;
import com.nhnacademy.boostorenginx.repository.CouponRepository;
import com.nhnacademy.boostorenginx.service.CouponPolicyService;
import com.nhnacademy.boostorenginx.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CouponPolicyService couponPolicyService;
    private final CouponHistoryRepository couponHistoryRepository;

    @Override
    public Long createCoupon(CouponCreateRequestDto requestDto) {
        CouponPolicyIdRequestDto couponPolicyIdRequestDto = new CouponPolicyIdRequestDto(
                requestDto.couponPolicyId()
        );
        CouponPolicy couponPolicy = couponPolicyService.findById(couponPolicyIdRequestDto)
                .orElseThrow(() -> new NotFoundCouponPolicyException("ID 에 해당하는 CouponPolicy 를 찾지 못했습니다: " + requestDto.couponPolicyId()));

        Coupon coupon = new Coupon(
                Status.UNUSED,
                LocalDateTime.now(),
                requestDto.expiredAt(),
                couponPolicy
        );
        Coupon saveCoupon = couponRepository.save(coupon);

        return saveCoupon.getId();
    }

    @Override
    public Optional<Coupon> getCouponByCode(CouponCodeRequestDto requestDto) {
        String code = requestDto.code();

        if (code == null || code.isBlank()) {
            throw new NotFoundCouponException("입력받은 code 에 해당하는 Coupon 을 찾지 못헀습니다" + code);
        }

        return couponRepository.findByCode(code);
    }

    @Override
    public Page<Coupon> getExpiredCoupons(CouponExpiredRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.page(), requestDto.pageSize());
        LocalDateTime expiredAt = requestDto.expiredAt();

        if (expiredAt == null) {
            throw new IllegalArgumentException("입력받은 currentDateTime 이 null 입니다");
        }

        return couponRepository.findByExpiredAtBeforeOrderByExpiredAtAsc(expiredAt, pageable);
    }

    @Override
    public Page<Coupon> getActiveCoupons(CouponActiveRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.page(), requestDto.pageSize());
        LocalDateTime currentDateTime = requestDto.currentDateTime();

        if (currentDateTime == null) {
            throw new IllegalArgumentException("입력받은 currentDateTime 이 null 입니다");
        }

        return couponRepository.findActiveCoupons(currentDateTime, pageable);
    }

    @Override
    public Page<Coupon> getCouponsByPolicy(CouponFindCouponPolicyIdRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.page(), requestDto.pageSize());

        CouponPolicyIdRequestDto couponPolicyIdRequestDto = new CouponPolicyIdRequestDto(
                requestDto.policyId()
        );

        CouponPolicy couponPolicy = couponPolicyService.findById(couponPolicyIdRequestDto).orElseThrow(
                () -> new NotFoundCouponPolicyException("해당 ID 의 CouponPolicy 를 찾을 수 없습니다")
        );

        if (couponPolicy == null) {
            throw new NotFoundCouponPolicyException("입력받은 couponPolicy 가 null 입니다");
        }

        return couponRepository.findByCouponPolicyOrderByIdAsc(couponPolicy, pageable);
    }

    @Override
    public Page<Coupon> getCouponsByStatus(CouponFindStatusRequestDto requestDto) {
        Pageable pageable = PageRequest.of(requestDto.page(), requestDto.pageSize());

        if (requestDto.status() == null) {
            throw new CouponException("Status 가 null 입니다");
        }

        Status status = Status.valueOf(requestDto.status());

        return couponRepository.findByStatusOrderByStatusAsc(status, pageable);
    }

    @Override
    public void updateExpiredCoupon(CouponUpdateExpiredRequestDto requestDto) {
        if (requestDto.status() == null) {
            throw new CouponException("Status 가 null 입니다");
        }

        Status status = Status.valueOf(requestDto.status());
        Pageable pageable = PageRequest.of(requestDto.page(), requestDto.pageSize());

        Page<Coupon> expiredCoupon = couponRepository.findByExpiredAtBeforeAndStatusOrderByExpiredAtAsc(requestDto.expiredDate(), status, pageable);

        List<CouponHistory> couponHistories = new ArrayList<>();

        for (Coupon coupon : expiredCoupon.getContent()) {
            CouponHistory history = coupon.changeStatus(Status.EXPIRED, LocalDateTime.now(), "EXPIRED");
            couponHistories.add(history);
        }

        couponRepository.saveAll(expiredCoupon.getContent());
        couponHistoryRepository.saveAll(couponHistories);
    }

    @Override
    public void useCoupon(MemberCouponUseRequestDto requestDto) {
        Coupon coupon = couponRepository.findById(requestDto.couponId()).orElseThrow(() -> new NotFoundCouponException("해당 ID 의 쿠폰을 찾을 수 없습니다" + requestDto.couponId()));
        LocalDateTime useTime = LocalDateTime.now();
        if (!coupon.getStatus().equals(Status.UNUSED)) {
            throw new CouponException("현재 쿠폰 상태: " + coupon.getStatus());
        }
        CouponHistory history = coupon.changeStatus(Status.USED, useTime, "USED");
        couponRepository.save(coupon);
        couponHistoryRepository.save(history);
    }

}
