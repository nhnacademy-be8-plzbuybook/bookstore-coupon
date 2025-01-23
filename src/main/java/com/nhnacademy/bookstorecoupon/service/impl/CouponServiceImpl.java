package com.nhnacademy.bookstorecoupon.service.impl;

import com.nhnacademy.bookstorecoupon.dto.coupon.*;
import com.nhnacademy.bookstorecoupon.entity.Coupon;
import com.nhnacademy.bookstorecoupon.entity.CouponHistory;
import com.nhnacademy.bookstorecoupon.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.enums.Status;
import com.nhnacademy.bookstorecoupon.error.CouponException;
import com.nhnacademy.bookstorecoupon.error.NotFoundCouponException;
import com.nhnacademy.bookstorecoupon.error.NotFoundCouponPolicyException;
import com.nhnacademy.bookstorecoupon.repository.CouponHistoryRepository;
import com.nhnacademy.bookstorecoupon.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.repository.CouponRepository;
import com.nhnacademy.bookstorecoupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService {
    private final CouponRepository couponRepository;
    private final CouponPolicyRepository couponPolicyRepository;
    private final CouponHistoryRepository couponHistoryRepository;

    @Transactional
    @Override
    public CouponResponseDto createCoupon(CouponCreateRequestDto couponCreateRequestDto) {
        Long policyId = couponCreateRequestDto.couponPolicyId();

        CouponPolicy couponPolicy = couponPolicyRepository.findById(policyId).orElseThrow(
                () -> new NotFoundCouponPolicyException("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + policyId)
        );

        Coupon coupon = new Coupon(
                Status.UNUSED,
                LocalDateTime.now(),
                couponCreateRequestDto.expiredAt(),
                couponPolicy
        );

        Coupon saveCoupon = couponRepository.save(coupon);

        CouponHistory couponHistory = CouponHistory.builder()
                .status(Status.UNUSED)
                .changeDate(LocalDateTime.now())
                .reason("CREATE")
                .coupon(saveCoupon)
                .build();
        couponHistoryRepository.save(couponHistory);

        return CouponResponseDto.fromCoupon(saveCoupon);
    }

    @Transactional(readOnly = true)
    public Page<CouponResponseDto> getAllCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable).map(CouponResponseDto::fromCoupon);
    }

    @Transactional(readOnly = true)
    @Override
    public Coupon getCouponById(Long couponId) {
        return couponRepository.findCouponById(couponId).orElseThrow(
                () -> new NotFoundCouponException("ID 에 해당하는 Coupon 를 찾을 수 없습니다: " + couponId)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public CouponResponseDto findCouponById(Long couponId) {
        Coupon coupon = couponRepository.findCouponById(couponId).orElseThrow(
                () -> new NotFoundCouponException("ID 에 해당하는 Coupon 를 찾을 수 없습니다: " + couponId)
        );

        return CouponResponseDto.fromCoupon(coupon);
    }

    @Transactional(readOnly = true)
    @Override
    public CouponPolicy findCouponPolicyByCouponId(Long couponId) {
        return couponRepository.findCouponPolicyByCouponId(couponId)
                .orElseThrow(() -> new NotFoundCouponException("쿠폰 ID에 해당하는 쿠폰정책을 찾을 수 없습니다: " + couponId));
    }

    @Override
    public Page<Coupon> getCouponsByPolicyId(CouponFindCouponPolicyIdRequestDto couponFindCouponPolicyIdRequestDto) {
        Pageable pageable = PageRequest.of(couponFindCouponPolicyIdRequestDto.page(), couponFindCouponPolicyIdRequestDto.pageSize());
        return couponRepository.findByCouponPolicy_Id(couponFindCouponPolicyIdRequestDto.policyId(), pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CouponResponseDto> getCouponsByPolicy(CouponFindCouponPolicyIdRequestDto couponFindCouponPolicyIdRequestDto) {
        Pageable pageable = PageRequest.of(couponFindCouponPolicyIdRequestDto.page(), couponFindCouponPolicyIdRequestDto.pageSize());

        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponFindCouponPolicyIdRequestDto.policyId()).orElseThrow(
                () -> new NotFoundCouponPolicyException("해당 ID 의 CouponPolicy 를 찾을 수 없습니다")
        );

        return couponRepository.findByCouponPolicyOrderByIdAsc(couponPolicy, pageable).map(CouponResponseDto::fromCoupon);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CouponResponseDto> getCouponsByStatus(CouponFindStatusRequestDto couponFindStatusRequestDto) {
        Pageable pageable = PageRequest.of(couponFindStatusRequestDto.page(), couponFindStatusRequestDto.pageSize());
        Status status = Status.valueOf(couponFindStatusRequestDto.status());

        return couponRepository.findByStatusOrderByStatusAsc(status, pageable).map(CouponResponseDto::fromCoupon);
    }

    @Transactional
    @Override
    public void useCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new NotFoundCouponException("해당 ID 의 쿠폰을 찾을 수 없습니다" + couponId)
        );
        LocalDateTime useTime = LocalDateTime.now();

        CouponHistory history = coupon.changeStatus(Status.USED, useTime, "USED");

        coupon.setStatus(Status.USED);

        couponRepository.save(coupon);
        couponHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    @Override
    public void updateExpiredCoupon(CouponUpdateExpiredRequestDto couponUpdateExpiredRequestDto) {
        Status status = Status.valueOf(couponUpdateExpiredRequestDto.status());
        Pageable pageable = PageRequest.of(couponUpdateExpiredRequestDto.page(), couponUpdateExpiredRequestDto.size());

        Page<Coupon> expiredCoupon = couponRepository.findByExpiredAtBeforeAndStatusOrderByExpiredAtAsc(couponUpdateExpiredRequestDto.expiredDate(), status, pageable);

        List<CouponHistory> couponHistories = new ArrayList<>();

        for (Coupon coupon : expiredCoupon.getContent()) {
            CouponHistory history = coupon.changeStatus(Status.EXPIRED, LocalDateTime.now(), "EXPIRED");
            couponHistories.add(history);
        }

        couponRepository.saveAll(expiredCoupon.getContent());
        couponHistoryRepository.saveAll(couponHistories);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long couponId) {
        return couponRepository.existsById(couponId);
    }

    @Transactional
    @Override
    public void cancelCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                () -> new NotFoundCouponException("해당 ID 의 쿠폰을 찾을 수 없습니다" + couponId)
        );
        LocalDateTime cancelTime = LocalDateTime.now();

        coupon.setStatus(Status.UNUSED);

        CouponHistory history = coupon.changeStatus(Status.CANCEL, cancelTime, "CANCEL");
        couponRepository.save(coupon);
        couponHistoryRepository.save(history);
    }
}
