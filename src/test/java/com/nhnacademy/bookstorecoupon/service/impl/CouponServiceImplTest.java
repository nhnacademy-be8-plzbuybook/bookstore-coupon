package com.nhnacademy.bookstorecoupon.service.impl;

import com.nhnacademy.bookstorecoupon.dto.coupon.*;
import com.nhnacademy.bookstorecoupon.entity.Coupon;
import com.nhnacademy.bookstorecoupon.entity.CouponHistory;
import com.nhnacademy.bookstorecoupon.entity.CouponPolicy;
import com.nhnacademy.bookstorecoupon.enums.SaleType;
import com.nhnacademy.bookstorecoupon.enums.Status;
import com.nhnacademy.bookstorecoupon.error.CouponException;
import com.nhnacademy.bookstorecoupon.error.NotFoundCouponException;
import com.nhnacademy.bookstorecoupon.error.NotFoundCouponPolicyException;
import com.nhnacademy.bookstorecoupon.repository.CouponHistoryRepository;
import com.nhnacademy.bookstorecoupon.repository.CouponPolicyRepository;
import com.nhnacademy.bookstorecoupon.repository.CouponRepository;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    private CouponHistory mockHistory;
    @Mock
    private CouponPolicy mockPolicy;
    @Mock
    private Coupon mockCoupon;

    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        mockHistory = new CouponHistory();

        mockPolicy = CouponPolicy.builder()
                .name("test1")
                .saleType(SaleType.RATIO)
                .minimumAmount(new BigDecimal("1000"))
                .discountLimit(new BigDecimal("10000"))
                .discountRatio(10)
                .isStackable(true)
                .couponScope("ALL")
                .startDate(now.minusDays(1))
                .endDate(now.plusDays(10))
                .couponActive(true)
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
        LocalDateTime expiredAt = now.plusDays(5);
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
        LocalDateTime expiredAt = now.plusDays(5);
        CouponCreateRequestDto requestDto = new CouponCreateRequestDto(policyId, expiredAt);

        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.empty());

        NotFoundCouponPolicyException exception = assertThrows(NotFoundCouponPolicyException.class,
                () -> couponService.createCoupon(requestDto));

        assertEquals("ID 에 해당하는 CouponPolicy 를 찾을 수 없습니다: " + policyId, exception.getMessage());
        verify(couponPolicyRepository, times(1)).findById(policyId);
    }

    @DisplayName("쿠폰 ID 로 쿠폰정책 찾기")
    @Test
    void findCouponPolicyByCouponId() {
        Long couponId = 1L;

        when(couponRepository.findCouponPolicyByCouponId(couponId)).thenReturn(Optional.of(mockPolicy));
        CouponPolicy result = couponService.findCouponPolicyByCouponId(couponId);

        assertEquals(mockPolicy, result);
        verify(couponRepository, times(1)).findCouponPolicyByCouponId(couponId);
    }

    @DisplayName("쿠폰 ID 로 쿠폰정책 찾을때 해당 쿠폰정책이 없을경우")
    @Test
    void findCouponPolicyByCouponId_ThrowNotFoundCouponException() {
        Long couponId = 1L;

        when(couponRepository.findCouponPolicyByCouponId(couponId)).thenReturn(Optional.empty());
        NotFoundCouponException exception = assertThrows(
                NotFoundCouponException.class,
                () -> couponService.findCouponPolicyByCouponId(couponId)
        );

        assertEquals(
                "쿠폰 ID에 해당하는 쿠폰정책을 찾을 수 없습니다: " + couponId,
                exception.getMessage()
        );
        verify(couponRepository, times(1)).findCouponPolicyByCouponId(couponId);
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
                new Coupon(Status.UNUSED, now.minusDays(5), now.plusDays(5), mockPolicy),
                new Coupon(Status.UNUSED, now.minusDays(10), now.plusDays(10), mockPolicy)
        );

        Page<Coupon> mockPage = new PageImpl<>(coupons, pageable, coupons.size());

        when(couponPolicyRepository.findById(policyId)).thenReturn(Optional.of(mockPolicy));
        when(couponRepository.findByCouponPolicyOrderByIdAsc(mockPolicy, pageable)).thenReturn(mockPage);

        Page<CouponResponseDto> result = couponService.getCouponsByPolicy(requestDto);

        assertEquals(2, result.getTotalElements());
        assertEquals(Status.UNUSED, result.getContent().getFirst().status());
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
                new Coupon(status, now.minusDays(5), now.plusDays(5), mockPolicy),
                new Coupon(status, now.minusDays(10), now.plusDays(10), mockPolicy)
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
        LocalDateTime expiredDate = now.minusDays(1);
        String status = "UNUSED";
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size);

        CouponUpdateExpiredRequestDto requestDto = new CouponUpdateExpiredRequestDto(expiredDate, status, page, size);

        Coupon mockCoupon1 = new Coupon(Status.UNUSED, now.minusDays(10), expiredDate.minusDays(5), mockPolicy);
        Coupon mockCoupon2 = new Coupon(Status.UNUSED, now.minusDays(15), expiredDate.minusDays(10), mockPolicy);

        List<Coupon> couponList = List.of(mockCoupon1, mockCoupon2);
        Page<Coupon> mockPage = new PageImpl<>(couponList, pageable, couponList.size());

        when(couponRepository.findByExpiredAtBeforeAndStatusOrderByExpiredAtAsc(expiredDate, Status.UNUSED, pageable)).thenReturn(mockPage);
        couponService.updateExpiredCoupon(requestDto);

        verify(couponRepository, times(1)).findByExpiredAtBeforeAndStatusOrderByExpiredAtAsc(expiredDate, Status.UNUSED, pageable);
        verify(couponRepository, times(1)).saveAll(couponList);
        verify(couponHistoryRepository, times(1)).saveAll(anyList());
    }

    @DisplayName("쿠폰을 사용할 경우")
    @Test
    void useCoupon() {
        Long couponId = 100L;
        LocalDateTime fixedTime = LocalDateTime.of(2024, 12, 26, 13, 43, 54);

        mockCoupon = mock(Coupon.class);
        mockHistory = new CouponHistory(Status.UNUSED, fixedTime, "USED", mockCoupon);

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));
        when(mockCoupon.getStatus()).thenReturn(Status.UNUSED);
        when(mockCoupon.changeStatus(eq(Status.USED), any(LocalDateTime.class), eq("USED")))
                .thenReturn(mockHistory);

        couponService.useCoupon(couponId);

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

        when(couponRepository.findById(couponId)).thenReturn(Optional.empty());

        NotFoundCouponException exception = assertThrows(NotFoundCouponException.class,
                () -> couponService.useCoupon(couponId));

        assertEquals("해당 ID 의 쿠폰을 찾을 수 없습니다" + couponId, exception.getMessage());

        verify(couponRepository, times(1)).findById(couponId);
        verifyNoInteractions(couponHistoryRepository);
    }


    @DisplayName("쿠폰을 사용할때 쿠폰의 상태가 UNUSED 가 아닌 경우")
    @Test
    void useCoupon_ThrowsCouponException_WhenStatusIsNotUnused() {
        Long couponId = 1L;

        mockCoupon = mock(Coupon.class);

        when(couponRepository.findById(couponId)).thenReturn(Optional.of(mockCoupon));
        when(mockCoupon.getStatus()).thenReturn(Status.EXPIRED);

        CouponException exception = assertThrows(CouponException.class,
                () -> couponService.useCoupon(couponId));

        assertEquals("현재 쿠폰을 사용할 수 없는 상태입니다: " + Status.EXPIRED, exception.getMessage());
        verify(couponRepository, times(1)).findById(couponId);
        verify(mockCoupon, times(1)).getStatus();
        verifyNoMoreInteractions(mockCoupon);
        verifyNoInteractions(couponHistoryRepository);
    }

    @DisplayName("모든 쿠폰 조회")
    @Test
    void getAllCoupons() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Coupon> couponPage = new PageImpl<>(Collections.singletonList(mockCoupon));

        when(couponRepository.findAll(pageable)).thenReturn(couponPage);

        Page<CouponResponseDto> result = couponService.getAllCoupons(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(Status.UNUSED, result.getContent().getFirst().status());
        verify(couponRepository, times(1)).findAll(pageable);
    }

    @DisplayName("쿠폰 ID로 쿠폰 조회")
    @Test
    void getCouponById_Success() {
        Long couponId = 1L;

        when(couponRepository.findCouponById(couponId)).thenReturn(Optional.of(mockCoupon));

        Coupon result = couponService.getCouponById(couponId);

        assertEquals(Status.UNUSED, result.getStatus());
        verify(couponRepository, times(1)).findCouponById(couponId);
    }

    @DisplayName("쿠폰 ID로 쿠폰 조회 실패")
    @Test
    void getCouponById_NotFound() {
        Long couponId = 1L;

        when(couponRepository.findCouponById(couponId)).thenReturn(Optional.empty());

        NotFoundCouponException exception = assertThrows(NotFoundCouponException.class, () -> couponService.getCouponById(couponId));

        assertEquals("ID 에 해당하는 Coupon 를 찾을 수 없습니다: " + couponId, exception.getMessage());
        verify(couponRepository, times(1)).findCouponById(couponId);
    }

    @DisplayName("쿠폰 ID로 쿠폰 Response 조회")
    @Test
    void findCouponById() {
        Long couponId = 1L;

        when(couponRepository.findCouponById(couponId)).thenReturn(Optional.of(mockCoupon));

        CouponResponseDto result = couponService.findCouponById(couponId);

        assertEquals(Status.UNUSED, result.status());
        verify(couponRepository, times(1)).findCouponById(couponId);
    }

    @DisplayName("쿠폰정책 ID로 쿠폰 조회")
    @Test
    void getCouponsByPolicyId() {
        Long policyId = 1L;
        int page = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        CouponFindCouponPolicyIdRequestDto requestDto = new CouponFindCouponPolicyIdRequestDto(policyId, page, pageSize);

        Page<Coupon> couponPage = new PageImpl<>(Collections.singletonList(mockCoupon));
        when(couponRepository.findByCouponPolicy_Id(policyId, pageable)).thenReturn(couponPage);

        Page<Coupon> result = couponService.getCouponsByPolicyId(requestDto);

        assertEquals(1, result.getTotalElements());
        verify(couponRepository, times(1)).findByCouponPolicy_Id(policyId, pageable);
    }

    @DisplayName("쿠폰 존재 여부 확인")
    @Test
    void existsById() {
        Long couponId = 1L;

        when(couponRepository.existsById(couponId)).thenReturn(true);

        boolean exists = couponService.existsById(couponId);

        assertTrue(exists);
        verify(couponRepository, times(1)).existsById(couponId);
    }

    @DisplayName("쿠폰 존재하지 않음")
    @Test
    void existsById_NotExists() {
        Long couponId = 1L;

        when(couponRepository.existsById(couponId)).thenReturn(false);

        boolean exists = couponService.existsById(couponId);

        assertFalse(exists);
        verify(couponRepository, times(1)).existsById(couponId);
    }
}