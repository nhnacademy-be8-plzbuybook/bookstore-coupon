package com.nhnacademy.bookstorecoupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.nhnacademy.bookstorecoupon.dto.couponpolicy.*;
import com.nhnacademy.bookstorecoupon.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.bookstorecoupon.enums.SaleType;
import com.nhnacademy.bookstorecoupon.service.CouponPolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponPolicyController.class)
class CouponPolicyControllerTest {

    @MockBean
    private CouponPolicyService couponPolicyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    private CouponPolicySaveRequestDto saveRequestDto;
    private CouponPolicyNameRequestDto nameRequestDto;
    private CouponPolicyIdRequestDto idRequestDto;
    private CouponPolicyResponseDto responseDto;
    private CouponTargetResponseDto mockResponseDto;
    private Page<CouponPolicyResponseDto> responsePage;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        responseDto = new CouponPolicyResponseDto(
                1L,
                "test",
                SaleType.RATIO.toString(),
                new BigDecimal("1000"),
                new BigDecimal("5000"),
                10,
                true,
                "CATEGORY",
                now.minusDays(1),
                now.plusDays(1),
                true
        );

        responsePage = new PageImpl<>(List.of(responseDto));

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS")); // 6자리 소수점 강제 설정
    }

    @DisplayName("쿠폰정책 생성")
    @Test
    void createCouponPolicy() throws Exception {
        saveRequestDto = new CouponPolicySaveRequestDto(
                "test",
                SaleType.RATIO,
                new BigDecimal("1000"),
                new BigDecimal("5000"),
                10,
                true,
                "CATEGORY",
                now.minusDays(1),
                now.plusDays(1),
                true
        );

        when(couponPolicyService.createCouponPolicy(any(CouponPolicySaveRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/coupon-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(responseDto.id()))
                .andExpect(jsonPath("$.name").value(responseDto.name()))
                .andExpect(jsonPath("$.saleType").value(responseDto.saleType()))
                .andExpect(jsonPath("$.minimumAmount").value(responseDto.minimumAmount()))
                .andExpect(jsonPath("$.discountLimit").value(responseDto.discountLimit()))
                .andExpect(jsonPath("$.discountRatio").value(responseDto.discountRatio()))
                .andExpect(jsonPath("$.isStackable").value(responseDto.isStackable()))
                .andExpect(jsonPath("$.couponScope").value(responseDto.couponScope()))
                .andExpect(jsonPath("$.couponActive").value(responseDto.couponActive()));

        verify(couponPolicyService, times(1)).createCouponPolicy(any(CouponPolicySaveRequestDto.class));
    }

    @DisplayName("쿠폰정책 등록 - 요청 Dto 가 잘못된 경우 ")
    @Test
    void createCouponPolicy_badRequest() throws Exception {
        saveRequestDto = new CouponPolicySaveRequestDto(
                null,
                null,
                null,
                null,
                null,
                true,
                null,
                null,
                null,
                true
        );

        mockMvc.perform(post("/api/coupon-policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("활성화된 쿠폰정책 목록 조회")
    @Test
    void findActiveCouponPolicies() throws Exception {
        boolean couponActive = true;
        int page = 0;
        int pageSize = 10;

        Page<CouponPolicyResponseDto> mockPage = new PageImpl<>(List.of(
                new CouponPolicyResponseDto(1L, "Policy 1", "RATIO", new BigDecimal("1000"), new BigDecimal("5000"), 10, true, "ALL", now, now.plusDays(10), true),
                new CouponPolicyResponseDto(2L, "Policy 2", "RATIO", new BigDecimal("2000"), new BigDecimal("6000"), 15, true, "CATEGORY", now, now.plusDays(5), true)
        ));

        when(couponPolicyService.findActiveCouponPolicy(any(CouponPolicyActiveRequestDto.class)))
                .thenReturn(mockPage);

        mockMvc.perform(get("/api/coupon-policies/active")
                        .param("couponActive", String.valueOf(couponActive))
                        .param("page", String.valueOf(page))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Policy 1"))
                .andExpect(jsonPath("$.content[1].name").value("Policy 2"));

        verify(couponPolicyService, times(1)).findActiveCouponPolicy(any(CouponPolicyActiveRequestDto.class));

    }

    @DisplayName("쿠폰정책 ID로 쿠폰정책 검색")
    @Test
    void findById() throws Exception {
        idRequestDto = new CouponPolicyIdRequestDto(1L);

        when(couponPolicyService.findById(any(CouponPolicyIdRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(get("/api/coupon-policies/{id}", idRequestDto.id())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.id()))
                .andExpect(jsonPath("$.name").value(responseDto.name()))
                .andExpect(jsonPath("$.saleType").value(responseDto.saleType()))
                .andExpect(jsonPath("$.minimumAmount").value(responseDto.minimumAmount()))
                .andExpect(jsonPath("$.discountLimit").value(responseDto.discountLimit()))
                .andExpect(jsonPath("$.discountRatio").value(responseDto.discountRatio()))
                .andExpect(jsonPath("$.isStackable").value(responseDto.isStackable()))
                .andExpect(jsonPath("$.couponScope").value(responseDto.couponScope()))
                .andExpect(jsonPath("$.couponActive").value(responseDto.couponActive()));


        verify(couponPolicyService, times(1)).findById(any(CouponPolicyIdRequestDto.class));
    }

    @DisplayName("쿠폰정책 ID로 쿠폰정책 검색 - 잘못된 id 인 경우")
    @Test
    void findById_badRequest() throws Exception {
        mockMvc.perform(get("/api/coupon-policies/{id}", -1))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("쿠폰정책 이름으로 쿠폰정책 검색")
    @Test
    void findByName() throws Exception {
        String name = "test";

        when(couponPolicyService.findByName(any(CouponPolicyNameRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(get("/api/coupon-policies/search")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseDto.id()))
                .andExpect(jsonPath("$.name").value(responseDto.name()))
                .andExpect(jsonPath("$.saleType").value(responseDto.saleType()))
                .andExpect(jsonPath("$.minimumAmount").value(responseDto.minimumAmount()))
                .andExpect(jsonPath("$.discountLimit").value(responseDto.discountLimit()))
                .andExpect(jsonPath("$.discountRatio").value(responseDto.discountRatio()))
                .andExpect(jsonPath("$.isStackable").value(responseDto.isStackable()))
                .andExpect(jsonPath("$.couponScope").value(responseDto.couponScope()))
                .andExpect(jsonPath("$.couponActive").value(responseDto.couponActive()));

        verify(couponPolicyService, times(1)).findByName(any(CouponPolicyNameRequestDto.class));
    }

    @DisplayName("쿠폰정책 ID로 쿠폰정책 검색 - 입력받은 name 이 잘못된 경우")
    @Test
    void findByName_badRequest() throws Exception {
        nameRequestDto = new CouponPolicyNameRequestDto("test");

        mockMvc.perform(get("/api/coupon-policies/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("쿠폰정책에 쿠폰대상 추가")
    @Test
    void addCouponTargets() throws Exception {
        Long policyId = 1L;
        Long ctTargetId = 100L;

        mockResponseDto = new CouponTargetResponseDto(
                1L,
                policyId,
                ctTargetId
        );

        when(couponPolicyService.addTargetToPolicy(any(CouponTargetAddRequestDto.class))).thenReturn(mockResponseDto);

        mockMvc.perform(post("/api/coupon-policies/{policy-id}/targets", policyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ctTargetId)))
                .andExpect(status().isCreated())
                .andExpect(content().string("쿠폰정책에 쿠폰대상이 성공적으로 추가되었습니다"));

        verify(couponPolicyService, times(1)).addTargetToPolicy(argThat(argument -> {
            assertEquals(policyId, argument.policyId());
            assertEquals(ctTargetId, argument.ctTargetId());
            return true;
        }));
    }

    @DisplayName("쿠폰정책에 쿠폰대상을 추가할때 요청 dto 가 잘못된 경우")
    @Test
    void addCouponTargets_badRequest() throws Exception {
        Long ctTargetId = 100L;

        mockMvc.perform(post("/api/coupon-policies/{policy-id}/targets", -1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ctTargetId)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("쿠폰정책 전체 조회")
    @Test
    void findAllCouponPolicies_Success() throws Exception {
        responseDto = new CouponPolicyResponseDto(
                1L, "Test Policy", "AMOUNT", BigDecimal.valueOf(5000), BigDecimal.valueOf(1000),
                10, true, "ALL", now, now.plusDays(10), true
        );
        Pageable pageable = PageRequest.of(0, 10);
        Page<CouponPolicyResponseDto> mockPage = new PageImpl<>(Collections.singletonList(responseDto), pageable, 1);

        when(couponPolicyService.findAllCouponPolicies(any(Pageable.class))).thenReturn(mockPage);


        mockMvc.perform(get("/api/coupon-policies")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Policy"))
                .andExpect(jsonPath("$.content[0].minimumAmount").value(5000));

        Mockito.verify(couponPolicyService, Mockito.times(1)).findAllCouponPolicies(any(Pageable.class));
    }

    @DisplayName("쿠폰 ID로 쿠폰정책 조회")
    @Test
    void findCouponPolicyByCouponId_Success() throws Exception {
        responseDto = new CouponPolicyResponseDto(
                1L, "Test Policy", "AMOUNT", BigDecimal.valueOf(5000), BigDecimal.valueOf(1000),
                10, true, "ALL", now, now.plusDays(10), true
        );

        when(couponPolicyService.findCouponPolicyById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/coupon-policies/coupon/{coupon-id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Policy"))
                .andExpect(jsonPath("$.minimumAmount").value(5000));

        Mockito.verify(couponPolicyService, Mockito.times(1)).findCouponPolicyById(1L);
    }

}