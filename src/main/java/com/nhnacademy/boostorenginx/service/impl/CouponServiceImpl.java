package com.nhnacademy.boostorenginx.service.impl;

import com.nhnacademy.boostorenginx.dto.coupon.*;
import com.nhnacademy.boostorenginx.dto.membercoupon.MemberCouponUseRequestDto;
import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.CouponHistory;
import com.nhnacademy.boostorenginx.entity.CouponPolicy;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.error.CouponException;
import com.nhnacademy.boostorenginx.error.NotFoundCouponException;
import com.nhnacademy.boostorenginx.error.NotFoundCouponPolicyException;
import com.nhnacademy.boostorenginx.repository.CouponHistoryRepository;
import com.nhnacademy.boostorenginx.repository.CouponPolicyRepository;
import com.nhnacademy.boostorenginx.repository.CouponRepository;
import com.nhnacademy.boostorenginx.service.CouponService;
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
        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponCreateRequestDto.couponPolicyId()).orElseThrow(
                () -> new NotFoundCouponPolicyException("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + couponCreateRequestDto.couponPolicyId())
        );

        Coupon coupon = new Coupon(
                Status.UNUSED,
                LocalDateTime.now(),
                couponCreateRequestDto.expiredAt(),
                couponPolicy
        );

        Coupon saveCoupon = couponRepository.save(coupon);

        CouponHistory couponHistory = CouponHistory.builder()
                .status(Status.USED)
                .changeDate(LocalDateTime.now())
                .reason("CREATE")
                .coupon(saveCoupon)
                .build();
        couponHistoryRepository.save(couponHistory);

        return CouponResponseDto.fromCoupon(saveCoupon);
    }

    @Override
    public CouponPolicy findCouponPolicyByCouponId(Long couponId) {
        return couponRepository.findCouponPolicyByCouponId(couponId)
                .orElseThrow(() -> new NotFoundCouponException("쿠폰 ID에 해당하는 쿠폰정책을 찾을 수 없습니다: " + couponId));
    }

    @Transactional(readOnly = true)
    @Override
    public Coupon getCouponByCode(CouponCodeRequestDto couponCodeRequestDto) {
        return couponRepository.findByCode(couponCodeRequestDto.code()).orElseThrow(
                () -> new NotFoundCouponException("CODE 에 해당하는 Coupon 을 찾을 수 없습니다: " + couponCodeRequestDto.code())
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CouponResponseDto> getExpiredCoupons(CouponExpiredRequestDto couponExpiredRequestDto) {
        Pageable pageable = PageRequest.of(couponExpiredRequestDto.page(), couponExpiredRequestDto.pageSize());
        LocalDateTime expiredAt = couponExpiredRequestDto.expiredAt();

        return couponRepository.findByExpiredAtBeforeOrderByExpiredAtAsc(expiredAt, pageable).map(CouponResponseDto::fromCoupon);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CouponResponseDto> getActiveCoupons(CouponActiveRequestDto couponActiveRequestDto) {
        Pageable pageable = PageRequest.of(couponActiveRequestDto.page(), couponActiveRequestDto.pageSize());
        LocalDateTime currentDateTime = couponActiveRequestDto.currentDateTime();

        return couponRepository.findActiveCoupons(currentDateTime, pageable).map(CouponResponseDto::fromCoupon);
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

    @Transactional(readOnly = true)
    @Override
    public void updateExpiredCoupon(CouponUpdateExpiredRequestDto couponUpdateExpiredRequestDto) {
        if (couponUpdateExpiredRequestDto.status() == null) {
            throw new CouponException("입력받은 Status 가 null 입니다");
        }

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
    public void useCoupon(MemberCouponUseRequestDto memberCouponUseRequestDto) {
        Coupon coupon = couponRepository.findById(memberCouponUseRequestDto.couponId()).orElseThrow(
                () -> new NotFoundCouponException("해당 ID 의 쿠폰을 찾을 수 없습니다" + memberCouponUseRequestDto.couponId())
        );
        LocalDateTime useTime = LocalDateTime.now();
        Status status = coupon.getStatus();

        if (!status.equals(Status.UNUSED)) {
            throw new CouponException("현재 쿠폰 상태: " + status);
        }

        CouponHistory history = coupon.changeStatus(Status.USED, useTime, "USED");
        couponRepository.save(coupon);
        couponHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    public Page<CouponResponseDto> getAllCoupons(Pageable pageable) {
        Page<CouponResponseDto> coupons = couponRepository.findAll(pageable).map(CouponResponseDto::fromCoupon);

        return coupons;
    }
}
