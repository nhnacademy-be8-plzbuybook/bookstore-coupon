package com.nhnacademy.boostorenginx.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.boostorenginx.dto.membercoupon.*;
import com.nhnacademy.boostorenginx.enums.Status;
import com.nhnacademy.boostorenginx.service.MemberCouponService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberCouponController.class)
class MemberCouponControllerTest {

    @InjectMocks
    private MemberCouponController memberCouponController;

    @MockBean
    private MemberCouponService memberCouponService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MemberCouponCreateRequestDto memberCouponCreateRequestDto;
    private MemberCouponResponseDto memberCouponResponseDto;
    private MemberCouponUseRequestDto memberCouponUseRequestDto;
    private MemberCouponResponseDto mockResponseDto;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        memberCouponCreateRequestDto = new MemberCouponCreateRequestDto(1L, 100L, 0, 10);
        memberCouponResponseDto = new MemberCouponResponseDto(
                1L,
                1L,
                new MemberCouponResponseDto.CouponResponseDto(
                        100L,
                        "ABC123",
                        Status.UNUSED,
                        now.minusDays(1),
                        now.plusDays(10),
                        "Holiday Discount",
                        new BigDecimal("5000"),
                        10
                )
        );
        memberCouponUseRequestDto = new MemberCouponUseRequestDto(1L, 100L);

    }

    @DisplayName("회원에게 쿠폰발급")
    @Test
    void createMemberCoupon() throws Exception {
        when(memberCouponService.createMemberCoupon(any(MemberCouponCreateRequestDto.class))).thenReturn(memberCouponResponseDto);

        mockMvc.perform(post("/api/coupon/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberCouponCreateRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.memberCouponId").value(memberCouponResponseDto.memberCouponId()))
                .andExpect(jsonPath("$.memberId").value(memberCouponResponseDto.memberId()))
                .andExpect(jsonPath("$.coupon.id").value(memberCouponResponseDto.coupon().id()))
                .andExpect(jsonPath("$.coupon.code").value(memberCouponResponseDto.coupon().code()))
                .andExpect(jsonPath("$.coupon.status").value(memberCouponResponseDto.coupon().status().toString()))
                .andExpect(jsonPath("$.coupon.issuedAt").exists())
                .andExpect(jsonPath("$.coupon.expiredAt").exists())
                .andExpect(jsonPath("$.coupon.name").value(memberCouponResponseDto.coupon().name()))
                .andExpect(jsonPath("$.coupon.discountLimit").value(memberCouponResponseDto.coupon().discountLimit()))
                .andExpect(jsonPath("$.coupon.discountRatio").value(memberCouponResponseDto.coupon().discountRatio()));

        verify(memberCouponService, times(1)).createMemberCoupon(any(MemberCouponCreateRequestDto.class));
    }

    @DisplayName("회원이 쿠폰을 사용")
    @Test
    void useMemberCoupon() throws Exception {
        mockMvc.perform(patch("/api/coupon/member/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberCouponUseRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("쿠폰이 성공적으로 사용되었습니다."));

        verify(memberCouponService, times(1)).useMemberCoupon(any(MemberCouponUseRequestDto.class));
    }

    @DisplayName("회원 ID 로 회원쿠폰조회")
    @Test
    void getMemberCouponsByMemberId() {
        Long memberId = 1L;
        int page = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);

        MemberCouponResponseDto.CouponResponseDto mockCoupon = new MemberCouponResponseDto.CouponResponseDto(
                100L,
                "TESTCODE",
                Status.UNUSED,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(10),
                "Test Policy",
                BigDecimal.valueOf(5000),
                10
        );

        MemberCouponResponseDto mockResponseDto = new MemberCouponResponseDto(1L, memberId, mockCoupon);
        Page<MemberCouponResponseDto> mockPage = new PageImpl<>(List.of(mockResponseDto), pageable, 1);

        when(memberCouponService.getMemberCouponsByMemberId(any(MemberCouponFindByMemberIdRequestDto.class))).thenReturn(mockPage);

        ResponseEntity<Page<MemberCouponResponseDto>> response = memberCouponController.getMemberCouponsByMemberId(memberId, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getTotalElements());
        verify(memberCouponService, times(1)).getMemberCouponsByMemberId(any(MemberCouponFindByMemberIdRequestDto.class));
    }

    @Test
    void getMemberCouponsByCouponId() throws Exception {
        Long couponId = 100L;
        int page = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(page, pageSize);

        MemberCouponResponseDto.CouponResponseDto mockCoupon = new MemberCouponResponseDto.CouponResponseDto(
                couponId,
                "TESTCODE",
                Status.UNUSED,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(10),
                "Test Policy",
                BigDecimal.valueOf(5000),
                10
        );

        MemberCouponResponseDto mockResponseDto = new MemberCouponResponseDto(1L, 1L, mockCoupon);
        Page<MemberCouponResponseDto> mockPage = new PageImpl<>(List.of(mockResponseDto), pageable, 1);

        when(memberCouponService.getMemberCouponsByCouponId(any(MemberCouponFindByCouponIdRequestDto.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/member-coupon/coupon/{couponId}", couponId)
                        .queryParam("page", String.valueOf(page))
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].memberCouponId").value(1L))
                .andExpect(jsonPath("$.content[0].coupon.id").value(couponId))
                .andExpect(jsonPath("$.content[0].coupon.code").value("TESTCODE"))
                .andExpect(jsonPath("$.content[0].coupon.status").value("UNUSED"));

        verify(memberCouponService, times(1))
                .getMemberCouponsByCouponId(any(MemberCouponFindByCouponIdRequestDto.class));
    }
}