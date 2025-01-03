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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponPolicyRepository couponPolicyRepository;

    @Mock
    private CouponHistoryRepository couponHistoryRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    private CouponHistory mockHistory;
    private CouponPolicy mockPolicy;
    private Coupon mockCoupon;

    private LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        mockHistory = new CouponHistory();

        mockPolicy = CouponPolicy.builder()
                .name("Test Policy")
                .build();

        mockCoupon = new Coupon(
                Status.UNUSED,
                now,
                now.plusDays(5),
                mockPolicy
        );
    }

    @DisplayName("쿠폰 생성")
    @Test
    void createCoupon() {
        Long policyId = 1L;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(5);
        CouponCreateRequestDto requestDto = new CouponCreateRequestDto(policyId, expiredAt);

        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.of(mockPolicy));
        when(couponRepository.save(any(Coupon.class))).thenReturn(mockCoupon);

        CouponResponseDto responseDto = couponService.createCoupon(requestDto);

        assertEquals(mockCoupon.getId(), responseDto.id());
        verify(couponPolicyRepository, times(1)).findById(policyId);
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @DisplayName("쿠폰정책 ID 에 해당하지 않는 쿠폰정책이 없을경우")
    @Test
    void createCoupon_ThrowNotFoundPolicyException() {
        Long policyId = 1L;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(5);
        CouponCreateRequestDto requestDto = new CouponCreateRequestDto(policyId, expiredAt);

        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.empty());

        NotFoundCouponPolicyException exception = assertThrows(NotFoundCouponPolicyException.class,
                () -> couponService.createCoupon(requestDto));

        assertEquals("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + policyId, exception.getMessage());
        verify(couponPolicyRepository, times(1)).findById(policyId);
        verify(couponRepository, never()).save(any(Coupon.class));

    }

    @DisplayName("쿠폰코드로 쿠폰조회")
    @Test
    void getCouponByCode() {
        String couponCode = "TEST123456";
        CouponCodeRequestDto requestDto = new CouponCodeRequestDto(couponCode);
        when(couponRepository.findByCode(couponCode)).thenReturn(Optional.of(mockCoupon));
        Coupon coupon = couponService.getCouponByCode(requestDto);
        assertEquals(mockCoupon.getId(), coupon.getId());
        assertEquals(mockCoupon.getStatus(), coupon.getStatus());
        verify(couponRepository, times(1)).findByCode(couponCode);
    }

    @DisplayName("쿠폰코드로 조회실패할 경우")
    @Test
    void getCouponByCode_ThrowNotFoundCouponException() {
        String couponCode = "fail";
        CouponCodeRequestDto requestDto = new CouponCodeRequestDto(couponCode);

        when(couponRepository.findByCode(couponCode)).thenReturn(Optional.empty());

        NotFoundCouponException exception = assertThrows(NotFoundCouponException.class,
                () -> couponService.getCouponByCode(requestDto));

        assertEquals("CODE 에 해당하는 Coupon 을 찾을 수 없습니다: " + couponCode, exception.getMessage());
        verify(couponRepository, times(1)).findByCode(couponCode);
    }

    @DisplayName("만료된 쿠폰 조회")
    @Test
    void getExpiredCoupons() {
        LocalDateTime expiredAt = LocalDateTime.now();
        int page = 0, size = 10;
        Pageable pageable = PageRequest.of(page, size);
        CouponExpiredRequestDto requestDto = new CouponExpiredRequestDto(expiredAt, page, size);

        List<Coupon> expiredCoupons = Arrays.asList(
                new Coupon(Status.EXPIRED, LocalDateTime.now().minusDays(10), expiredAt.minusDays(5), mockPolicy),
                new Coupon(Status.EXPIRED, LocalDateTime.now().minusDays(20), expiredAt.minusDays(15), mockPolicy)
        );

        Page<Coupon> mockPage = new PageImpl<>(expiredCoupons, pageable, expiredCoupons.size());
        when(couponRepository.findByExpiredAtBeforeOrderByExpiredAtAsc(expiredAt, pageable)).thenReturn(mockPage);

        Page<CouponResponseDto> result = couponService.getExpiredCoupons(requestDto);

        assertEquals(2, result.getTotalElements());
        assertEquals(expiredCoupons.get(0).getStatus(), result.getContent().get(0).status());
        verify(couponRepository, times(1)).findByExpiredAtBeforeOrderByExpiredAtAsc(expiredAt, pageable);
    }

    @DisplayName("활성화된 쿠폰 조회")
    @Test
    void getActiveCoupons() {
        now = LocalDateTime.now();
        int page = 0, size = 10;
        Pageable pageable = PageRequest.of(page, size);
        CouponActiveRequestDto requestDto = new CouponActiveRequestDto(now, page, size);

        List<Coupon> activeCoupons = Arrays.asList(
                new Coupon(Status.UNUSED, now.minusDays(5), now.plusDays(5), mockPolicy),
                new Coupon(Status.UNUSED, now.minusDays(10), now.plusDays(10), mockPolicy)
        );

        Page<Coupon> mockPage = new PageImpl<>(activeCoupons, pageable, activeCoupons.size());
        when(couponRepository.findActiveCoupons(now, pageable)).thenReturn(mockPage);

        Page<CouponResponseDto> result = couponService.getActiveCoupons(requestDto);

        assertEquals(2, result.getTotalElements());
        assertEquals(activeCoupons.get(0).getStatus(), result.getContent().get(0).status());
        verify(couponRepository, times(1)).findActiveCoupons(now, pageable);
    }

    @DisplayName("쿠폰정책으로 쿠폰조회")
    @Test
    void getCouponsByPolicy() {
        Long policyId = 1L;
        int page = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        CouponFindCouponPolicyIdRequestDto requestDto = new CouponFindCouponPolicyIdRequestDto(policyId, page, pageSize);

        List<Coupon> coupons = List.of(
                new Coupon(Status.UNUSED, LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(5), mockPolicy),
                new Coupon(Status.UNUSED, LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(10), mockPolicy)
        );

        Page<Coupon> mockPage = new PageImpl<>(coupons, pageable, coupons.size());

        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.of(mockPolicy));
        when(couponRepository.findByCouponPolicyOrderByIdAsc(mockPolicy, pageable)).thenReturn(mockPage);

        Page<CouponResponseDto> result = couponService.getCouponsByPolicy(requestDto);

        assertEquals(2, result.getTotalElements());
        assertEquals(coupons.get(0).getStatus(), result.getContent().get(0).status());
        verify(couponPolicyRepository, times(1)).findById(policyId);
        verify(couponRepository, times(1)).findByCouponPolicyOrderByIdAsc(mockPolicy, pageable);
    }

    @DisplayName("쿠폰정책으로 쿠폰을 조회할때 해당하는 쿠폰정책이 없는 경우")
    @Test
    void getCouponsByPolicy_ThrowNotFoundCouponPolicyException() {
        Long policyId = 1L;
        CouponFindCouponPolicyIdRequestDto requestDto = new CouponFindCouponPolicyIdRequestDto(policyId, 0, 10);

        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.empty());

        NotFoundCouponPolicyException exception = assertThrows(NotFoundCouponPolicyException.class,
                () -> couponService.getCouponsByPolicy(requestDto));

        assertEquals("해당 ID 의 CouponPolicy 를 찾을 수 없습니다", exception.getMessage());
        verify(couponPolicyRepository, times(1)).findById(policyId);
    }

    @DisplayName("쿠폰상태로 쿠폰조회")
    @Test
    void getCouponsByStatus() {
        String statusString = "UNUSED";
        int page = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        CouponFindStatusRequestDto requestDto = new CouponFindStatusRequestDto(statusString, page, pageSize);

        Status status = Status.UNUSED;
        List<Coupon> coupons = List.of(
                new Coupon(status, LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(5), mockPolicy),
                new Coupon(status, LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(10), mockPolicy)
        );

        Page<Coupon> mockPage = new PageImpl<>(coupons, pageable, coupons.size());

        when(couponRepository.findByStatusOrderByStatusAsc(status, pageable)).thenReturn(mockPage);

        Page<CouponResponseDto> result = couponService.getCouponsByStatus(requestDto);

        assertEquals(2, result.getTotalElements());
        assertEquals(status, result.getContent().get(0).status());
        verify(couponRepository, times(1)).findByStatusOrderByStatusAsc(status, pageable);
    }

    @DisplayName("만료된 쿠폰들의 상태 업데이트")
    @Test
    void updateExpiredCoupon() {
        LocalDateTime expiredDate = LocalDateTime.now().minusDays(1);
        String status = "UNUSED";
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);

        CouponUpdateExpiredRequestDto requestDto = new CouponUpdateExpiredRequestDto(expiredDate, status, page, size);

        Coupon mockCoupon1 = new Coupon(Status.UNUSED, LocalDateTime.now().minusDays(10), expiredDate.minusDays(5), mockPolicy);
        Coupon mockCoupon2 = new Coupon(Status.UNUSED, LocalDateTime.now().minusDays(15), expiredDate.minusDays(10), mockPolicy);

        List<Coupon> couponList = List.of(mockCoupon1, mockCoupon2);
        Page<Coupon> mockPage = new PageImpl<>(couponList, pageable, couponList.size());

        when(couponRepository.findByExpiredAtBeforeAndStatusOrderByExpiredAtAsc(expiredDate, Status.UNUSED, pageable))
                .thenReturn(mockPage);
        couponService.updateExpiredCoupon(requestDto);

        verify(couponRepository, times(1))
                .findByExpiredAtBeforeAndStatusOrderByExpiredAtAsc(expiredDate, Status.UNUSED, pageable);
        verify(couponRepository, times(1)).saveAll(couponList);
        verify(couponHistoryRepository, times(1)).saveAll(anyList());
    }

    @DisplayName("만료된 쿠폰들의 상태 업데이트 할때 Status 가 null 일 경우")
    @Test
    void updateExpiredCoupon_StatusIsNull() {
        LocalDateTime expiredDate = LocalDateTime.now().minusDays(1);
        CouponUpdateExpiredRequestDto requestDto = new CouponUpdateExpiredRequestDto(expiredDate, null, 0, 10);

        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.updateExpiredCoupon(requestDto));
        assertEquals("입력받은 Status 가 null 입니다", exception.getMessage());
    }

    @DisplayName("쿠폰을 사용할 경우")
    @Test
    void useCoupon() {
        Long couponId = 1L;
        Long memberId = 100L;
        LocalDateTime fixedTime = LocalDateTime.of(2024, 12, 26, 13, 43, 54);

        MemberCouponUseRequestDto requestDto = new MemberCouponUseRequestDto(couponId, memberId);

        mockCoupon = mock(Coupon.class);
        mockHistory = new CouponHistory(Status.UNUSED, fixedTime, "USED", mockCoupon);

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));
        when(mockCoupon.getStatus()).thenReturn(Status.UNUSED);
        when(mockCoupon.changeStatus(eq(Status.USED), any(LocalDateTime.class), eq("USED")))
                .thenReturn(mockHistory);

        couponService.useCoupon(requestDto);

        verify(couponRepository, times(1)).findById(couponId);
        verify(mockCoupon, times(1)).getStatus();
        verify(mockCoupon, times(1)).changeStatus(eq(Status.USED), any(LocalDateTime.class), eq("USED"));
        verify(couponRepository, times(1)).save(mockCoupon);
        verify(couponHistoryRepository, times(1)).save(mockHistory);
    }

    @DisplayName("쿠폰을 사용할때 해당 쿠폰을 찾지 못한 경우")
    @Test
    void useCoupon_NotFoundCouponException() {
        Long couponId = 1L;
        Long memberId = 100L;
        MemberCouponUseRequestDto memberCouponUseRequestDto = new MemberCouponUseRequestDto(couponId, memberId);

        when(couponRepository.findById(memberCouponUseRequestDto.couponId())).thenReturn(Optional.empty());

        NotFoundCouponException exception = assertThrows(NotFoundCouponException.class,
                () -> couponService.useCoupon(memberCouponUseRequestDto));

        assertEquals("해당 ID 의 쿠폰을 찾을 수 없습니다" + couponId, exception.getMessage());

        verify(couponRepository, times(1)).findById(memberCouponUseRequestDto.couponId());
        verifyNoInteractions(couponHistoryRepository);
    }

    @DisplayName("쿠폰을 사용할때 쿠폰의 상태가 UNUSED 가 아닌 경우")
    @Test
    void useCoupon_ThrowsCouponException_WhenStatusIsNotUnused() {
        Long couponId = 1L;
        Long memberId = 100L;
        MemberCouponUseRequestDto requestDto = new MemberCouponUseRequestDto(couponId, memberId);

        mockCoupon = mock(Coupon.class);

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));
        when(mockCoupon.getStatus()).thenReturn(Status.EXPIRED);

        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.useCoupon(requestDto));

        assertEquals("현재 쿠폰 상태: " + Status.EXPIRED, exception.getMessage());
        verify(couponRepository, times(1)).findById(couponId);
        verify(mockCoupon, times(1)).getStatus();
        verifyNoMoreInteractions(mockCoupon);
        verifyNoInteractions(couponHistoryRepository);
    }
}