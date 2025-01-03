package com.nhnacademy.boostorenginx.service;

import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetGetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetResponseDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetSaveRequestDto;
import com.nhnacademy.boostorenginx.dto.coupontarget.CouponTargetSeearchReqeustDto;
import org.springframework.data.domain.Page;

public interface CouponTargetService {

    CouponTargetResponseDto createCouponTarget(CouponTargetSaveRequestDto couponTargetSaveRequestDto); // 쿠폰대상 생성

    Page<CouponTargetGetResponseDto> getCouponTargetsByPolicyId(CouponTargetSeearchReqeustDto couponTargetSeearchReqeustDto); // 쿠폰대상 목록 조회
}
