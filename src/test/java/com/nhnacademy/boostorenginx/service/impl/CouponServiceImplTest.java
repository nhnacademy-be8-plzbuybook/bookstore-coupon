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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponPolicyService couponPolicyService;

    @Mock
    private CouponHistoryRepository couponHistoryRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    private CouponHistory mockHistory;
    private CouponPolicy mockPolicy;
    private Coupon mockCoupon;

    @BeforeEach
    void setUp() {
        mockHistory = new CouponHistory();
        mockPolicy = new CouponPolicy();
        mockCoupon = new Coupon();
    }

    @DisplayName("쿠폰 생성")
    @Test
    void createCoupon() {
        Long policyId = 1L;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(5);
        CouponCreateRequestDto requestDto = new CouponCreateRequestDto(policyId, expiredAt);

        when(couponPolicyService.findById(new CouponPolicyIdRequestDto(policyId)))
                .thenReturn(Optional.of(mockPolicy));

        mockCoupon = new Coupon(Status.UNUSED, LocalDateTime.now(), expiredAt, mockPolicy);
        when(couponRepository.save(any(Coupon.class))).thenReturn(mockCoupon);

        couponService.createCoupon(requestDto);
        verify(couponPolicyService, times(1)).findById(new CouponPolicyIdRequestDto(policyId));
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @DisplayName("쿠폰정책 ID 가 존재하지않은 예외")
    @Test
    void createCoupon_ThrowNotFoundPolicyException() {
        Long policyId = 1L;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(5);
        CouponCreateRequestDto requestDto = new CouponCreateRequestDto(policyId, expiredAt);
        when(couponPolicyService.findById(new CouponPolicyIdRequestDto(policyId)))
                .thenReturn(Optional.empty());
        NotFoundCouponPolicyException exception = assertThrows(NotFoundCouponPolicyException.class,
                () -> couponService.createCoupon(requestDto));

        assertEquals("ID 에 해당하는 CouponPolicy 를 찾지 못했습니다: " + policyId, exception.getMessage());
        verify(couponPolicyService, times(1)).findById(new CouponPolicyIdRequestDto(policyId));
        verify(couponRepository, never()).save(any(Coupon.class));

    }

    @DisplayName("쿠폰 코드로 쿠폰 조회")
    @Test
    void getCouponByCode() {
        String couponCode = "96481f01-b4ca-4c0f-ba5c-42c0d44956d2";
        CouponCodeRequestDto requestDto = new CouponCodeRequestDto(couponCode);

        Coupon mockCoupon = new Coupon(
                Status.UNUSED,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                mockPolicy
        );
        when(couponRepository.findByCode(couponCode)).thenReturn(Optional.of(mockCoupon));
        Optional<Coupon> result = couponService.getCouponByCode(requestDto);

        assertTrue(result.isPresent());
        assertEquals(mockCoupon, result.get());
        verify(couponRepository, times(1)).findByCode(couponCode);
    }

    @DisplayName("쿠폰 코드가 null 인 경우 예외발생")
    @Test
    void getCouponByCode_NullCode() {
        CouponCodeRequestDto nullRequestDto = new CouponCodeRequestDto(null);
        CouponCodeRequestDto blankRequestDto = new CouponCodeRequestDto(" ");

        NotFoundCouponException nullException = assertThrows(NotFoundCouponException.class,
                () -> couponService.getCouponByCode(nullRequestDto));
        assertEquals("입력받은 code 에 해당하는 Coupon 을 찾지 못헀습니다null", nullException.getMessage());

        NotFoundCouponException blankException = assertThrows(NotFoundCouponException.class,
                () -> couponService.getCouponByCode(blankRequestDto));
        assertEquals("입력받은 code 에 해당하는 Coupon 을 찾지 못헀습니다 ", blankException.getMessage());
    }

    @DisplayName("만료된 쿠폰들 조회")
    @Test
    void getExpiredCoupons() {
        LocalDateTime expiredAt = LocalDateTime.now();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        CouponExpiredRequestDto requestDto = new CouponExpiredRequestDto(expiredAt, page, size);
        Coupon coupon1 = new Coupon(Status.EXPIRED, LocalDateTime.now().minusDays(10), expiredAt.minusDays(5), null);
        Coupon coupon2 = new Coupon(Status.EXPIRED, LocalDateTime.now().minusDays(20), expiredAt.minusDays(15), null);
        List<Coupon> couponList = Arrays.asList(coupon1, coupon2);
        Page<Coupon> mockPage = new PageImpl<>(couponList, pageable, couponList.size());
        when(couponRepository.findByExpiredAtBeforeOrderByExpiredAtAsc(expiredAt, pageable)).thenReturn(mockPage);
        Page<Coupon> result = couponService.getExpiredCoupons(requestDto);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(coupon1, result.getContent().get(0));
        verify(couponRepository, times(1)).findByExpiredAtBeforeOrderByExpiredAtAsc(expiredAt, pageable);
    }

    @DisplayName("만료일자가 null 인 경우 예외발생")
    @Test
    void getExpiredCoupons_NullRequestDto() {
        int page = 0;
        int size = 10;
        CouponExpiredRequestDto requestDto = new CouponExpiredRequestDto(null, page, size);
        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.getExpiredCoupons(requestDto));

        assertEquals("입력받은 currentDateTime 이 null 입니다", exception.getMessage());
    }

    @DisplayName("활성화된 쿠폰 조회")
    @Test
    void getActiveCoupons() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        CouponActiveRequestDto requestDto = new CouponActiveRequestDto(currentDateTime, page, size);

        Coupon coupon1 = new Coupon(Status.UNUSED, LocalDateTime.now().minusDays(5), currentDateTime.plusDays(5), null);
        Coupon coupon2 = new Coupon(Status.UNUSED, LocalDateTime.now().minusDays(10), currentDateTime.plusDays(10), null);
        List<Coupon> couponList = Arrays.asList(coupon1, coupon2);
        Page<Coupon> mockPage = new PageImpl<>(couponList, pageable, couponList.size());
        when(couponRepository.findActiveCoupons(currentDateTime, pageable)).thenReturn(mockPage);
        Page<Coupon> result = couponService.getActiveCoupons(requestDto);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(coupon1, result.getContent().get(0));
        verify(couponRepository, times(1)).findActiveCoupons(currentDateTime, pageable);
    }

    @DisplayName("일자가 null 인 경우 예외발생")
    @Test
    void getActiveCoupons_NullRequestDto() {
        int page = 0;
        int size = 10;
        CouponActiveRequestDto requestDto = new CouponActiveRequestDto(null, page, size);
        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.getActiveCoupons(requestDto));

        assertEquals("입력받은 currentDateTime 이 null 입니다", exception.getMessage());
        verifyNoInteractions(couponRepository);
    }

    @DisplayName("쿠폰정책으로 쿠폰 조회")
    @Test
    void getCouponsByPolicy() {
        Long policyId = 1L;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        CouponFindCouponPolicyIdRequestDto requestDto = new CouponFindCouponPolicyIdRequestDto(policyId, page, size);
        mockPolicy = new CouponPolicy();
        List<Coupon> couponList = Arrays.asList(new Coupon(), new Coupon());
        Page<Coupon> mockPage = new PageImpl<>(couponList, pageable, couponList.size());
        when(couponPolicyService.findById(new CouponPolicyIdRequestDto(policyId))).thenReturn(Optional.of(mockPolicy));
        when(couponRepository.findByCouponPolicyOrderByIdAsc(mockPolicy, pageable)).thenReturn(mockPage);
        Page<Coupon> result = couponService.getCouponsByPolicy(requestDto);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(couponPolicyService, times(1)).findById(new CouponPolicyIdRequestDto(policyId));
        verify(couponRepository, times(1)).findByCouponPolicyOrderByIdAsc(mockPolicy, pageable);
    }

    @DisplayName("쿠폰정책으로 쿠폰조회 실패 예외발생")
    @Test
    void getCouponsByPolicy_ThrowNotFoundPolicyException() {
        Long policyId = 1L;
        CouponFindCouponPolicyIdRequestDto requestDto = new CouponFindCouponPolicyIdRequestDto(policyId, 0, 10);
        when(couponPolicyService.findById(new CouponPolicyIdRequestDto(policyId))).thenReturn(Optional.empty());
        NotFoundCouponPolicyException exception = assertThrows(NotFoundCouponPolicyException.class,
                () -> couponService.getCouponsByPolicy(requestDto));
        assertEquals("해당 ID 의 CouponPolicy 를 찾을 수 없습니다", exception.getMessage());
        verify(couponPolicyService, times(1)).findById(new CouponPolicyIdRequestDto(policyId));
        verify(couponRepository, never()).findByCouponPolicyOrderByIdAsc(any(), any());
    }

    @DisplayName("상태로 쿠폰조회")
    @Test
    void getCouponsByStatus() {
        String statusString = "UNUSED";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        CouponFindStatusRequestDto requestDto = new CouponFindStatusRequestDto(statusString, page, size);
        Status status = Status.UNUSED;
        List<Coupon> couponList = Arrays.asList(new Coupon(), new Coupon());
        Page<Coupon> mockPage = new PageImpl<>(couponList, pageable, couponList.size());
        when(couponRepository.findByStatusOrderByStatusAsc(status, pageable)).thenReturn(mockPage);
        Page<Coupon> result = couponService.getCouponsByStatus(requestDto);
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(couponRepository, times(1)).findByStatusOrderByStatusAsc(status, pageable);
    }

    @DisplayName("상태가 null 일 경우 ")
    @Test
    void getCouponsByStatus_NullStatus() {
        CouponFindStatusRequestDto requestDto = new CouponFindStatusRequestDto(null, 0, 10);
        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.getCouponsByStatus(requestDto));
        assertEquals("Status 가 null 입니다", exception.getMessage());
        verify(couponRepository, never()).findByStatusOrderByStatusAsc(any(), any());
    }

    // 테스트 코드 실패 -> 수정 필요
    @DisplayName("만료된 쿠폰 업데이트")
    @Test
    void updateExpiredCoupon() {
        LocalDateTime expiredDate = LocalDateTime.now().minusDays(1);
        int page = 1;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);
        CouponUpdateExpiredRequestDto requestDto = new CouponUpdateExpiredRequestDto(expiredDate, Status.UNUSED.toString(), page, size);

        Coupon coupon1 = new Coupon(Status.UNUSED, LocalDateTime.now().minusDays(10), expiredDate.minusDays(5), null);
        Coupon coupon2 = new Coupon(Status.UNUSED, LocalDateTime.now().minusDays(15), expiredDate.minusDays(10), null);
        List<Coupon> couponList = Arrays.asList(coupon1, coupon2);
        Page<Coupon> mockPage = new PageImpl<>(couponList, pageable, couponList.size());

        when(couponRepository.findByExpiredAtBeforeAndStatusOrderByExpiredAtAsc(expiredDate, Status.UNUSED, pageable))
                .thenReturn(mockPage);

        couponService.updateExpiredCoupon(requestDto);

        verify(couponRepository, times(1)).findByExpiredAtBeforeAndStatusOrderByExpiredAtAsc(expiredDate, Status.UNUSED, pageable);
        verify(couponRepository, times(1)).saveAll(couponList);
        verify(couponHistoryRepository, times(1)).saveAll(anyList());
    }

    @DisplayName("상태가 null 일 경우 예외발생")
    @Test
    void updateExpiredCoupon_ThrowsException_WhenStatusIsNull() {
        LocalDateTime expiredDate = LocalDateTime.now().minusDays(1);
        int page = 0;
        int size = 10;
        CouponUpdateExpiredRequestDto requestDto = new CouponUpdateExpiredRequestDto(expiredDate, null, page, size);

        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.updateExpiredCoupon(requestDto));

        assertEquals("Status 가 null 입니다", exception.getMessage());
        verifyNoInteractions(couponRepository);
        verifyNoInteractions(couponHistoryRepository);
    }

    @DisplayName("쿠폰사용")
    @Test
    void useCoupon() {
        Long couponId = 1L;
        Long memberId = 1L;
        LocalDateTime now = LocalDateTime.of(2024, 12, 23, 9, 0, 0);
        MemberCouponUseRequestDto request = new MemberCouponUseRequestDto(couponId, memberId);
        mockCoupon = mock(Coupon.class);
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));
        when(mockCoupon.getStatus()).thenReturn(Status.UNUSED);
        mockHistory = new CouponHistory(Status.UNUSED, now, "UNUSED", mockCoupon);
        when(mockCoupon.changeStatus(eq(Status.USED), any(LocalDateTime.class), eq("USED"))).thenReturn(mockHistory);
        couponService.useCoupon(request);
        verify(couponRepository, times(1)).findById(couponId);
        verify(mockCoupon, times(1)).getStatus();
        verify(mockCoupon, times(1)).changeStatus(eq(Status.USED), any(LocalDateTime.class), eq("USED"));
        verify(couponHistoryRepository, times(1)).save(mockHistory);
    }

    @DisplayName("쿠폰 ID 가 없는 경우 예외발생")
    @Test
    void useCoupon_ThrowsException_WhenCouponIdIsNotFound() {
        Long couponId = 1L;
        Long memberId = 1L;
        MemberCouponUseRequestDto request = new MemberCouponUseRequestDto(couponId, memberId);
        when(couponRepository.findById(couponId)).thenReturn(Optional.empty());
        NotFoundCouponException exception = assertThrows(NotFoundCouponException.class,
                () -> couponService.useCoupon(request));
        assertEquals("해당 ID 의 쿠폰을 찾을 수 없습니다" + couponId, exception.getMessage());
        verify(couponRepository, times(1)).findById(couponId);
    }

    @DisplayName("쿠폰 상태가 UNUSED 인 경우 예외발생")
    @Test
    void useCoupon_ThrowsException_WhenCouponStatusIsUnused() {
        Long couponId = 1L;
        Long memberId = 1L;
        MemberCouponUseRequestDto request = new MemberCouponUseRequestDto(couponId, memberId);
        mockCoupon = mock(Coupon.class);
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));
        when(mockCoupon.getStatus()).thenReturn(Status.EXPIRED);
        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.useCoupon(request));
        assertEquals("현재 쿠폰 상태: " + Status.EXPIRED, exception.getMessage());
        verify(couponRepository, times(1)).findById(couponId);
        verify(mockCoupon, times(2)).getStatus();
    }
}