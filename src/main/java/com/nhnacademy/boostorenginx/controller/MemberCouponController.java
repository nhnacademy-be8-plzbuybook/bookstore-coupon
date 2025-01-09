package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.membercoupon.*;
import com.nhnacademy.boostorenginx.service.MemberCouponService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/member-coupons")
@RestController
public class MemberCouponController {
    private final MemberCouponService memberCouponService;

    /**
     * 회원에게 쿠폰 발급
     * POST /api/member-coupons
     */
    @PostMapping
    public ResponseEntity<MemberCouponResponseDto> createMemberCoupon(@RequestBody @Valid MemberCouponCreateRequestDto memberCouponCreateRequestDto) {
        MemberCouponResponseDto memberCouponResponseDto = memberCouponService.createMemberCoupon(memberCouponCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(memberCouponResponseDto);
    }

    /**
     * 쿠폰 ID로 회원쿠폰 조회
     * GET /api/member-coupons/{coupon-id}
     */
    @GetMapping("/coupons/{coupon-id}")
    public ResponseEntity<Page<MemberCouponResponseDto>> getMemberCouponsByCouponId(@PathVariable("coupon-id") Long couponId,
                                                                                    @RequestParam(defaultValue = "0") @Min(0) int page,
                                                                                    @RequestParam(defaultValue = "10") @Min(1) int pageSize) {
        MemberCouponFindByCouponIdRequestDto memberCouponFindByCouponIdRequestDto = new MemberCouponFindByCouponIdRequestDto(couponId, page, pageSize);
        Page<MemberCouponResponseDto> memberCouponResponseDto = memberCouponService.getMemberCouponsByCouponId(memberCouponFindByCouponIdRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(memberCouponResponseDto);
    }

    /**
     * 회원이 보유한 쿠폰 사용
     * PATCH /api/member-coupons/member/{member-id}/coupons/{coupon-id}/use
     */
    @PatchMapping("/members/{member-id}/coupons/{coupon-id}/use")
    public ResponseEntity<String> useMemberCoupon(@PathVariable("member-id") Long memberId, @PathVariable("coupon-id") Long couponId) {
        MemberCouponUseRequestDto memberCouponUseRequestDto = new MemberCouponUseRequestDto(memberId, couponId);
        memberCouponService.useMemberCoupon(memberCouponUseRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body("쿠폰이 성공적으로 사용되었습니다.");
    }

    /**
     * 회원 ID로 회원 쿠폰 조회
     * GET /api/member-coupons/member/{member-id}
     */
    @GetMapping("/member/{member-id}")
    public ResponseEntity<Page<MemberCouponGetResponseDto>> getMemberCouponsByMemberId(@PathVariable("member-id") Long memberId, Pageable pageable) {
        MemberCouponFindByMemberIdRequestDto requestDto = new MemberCouponFindByMemberIdRequestDto(memberId, pageable.getPageNumber(), pageable.getPageSize());
        Page<MemberCouponGetResponseDto> responseDto = memberCouponService.getMemberCouponsByMemberId(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 회원이 사용 가능한 쿠폰 목록 조회 (UNUSED 상태의 쿠폰)
     * GET /api/member-coupons/member/{member-id}/unused
     */
    @GetMapping("/member/{member-id}/unused")
    public ResponseEntity<Page<MemberCouponGetResponseDto>> getUnusedMemberCouponsByMemberId(@PathVariable("member-id") Long memberId, Pageable pageable) {
        MemberCouponFindByMemberIdRequestDto requestDto = new MemberCouponFindByMemberIdRequestDto(memberId, pageable.getPageNumber(), pageable.getPageSize());
        Page<MemberCouponGetResponseDto> responseDto = memberCouponService.getUnusedMemberCouponsByMemberId(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 회원쿠폰 목록 조회
     * GET /api/member-coupons
     * @param page 페이지 번호 (최소 0)
     * @param pageSize 페이지 크기 (최소 1)
     * @return 회원쿠폰 ID, 회원 ID, 쿠폰 정보 페이지네이션
     */
    @GetMapping
    public ResponseEntity<Page<MemberCouponResponseDto>> getAllMemberCoupons(@NotNull @Min(0) int page, @NotNull @Min(1) int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<MemberCouponResponseDto> responseDto = memberCouponService.getAllMemberCoupons(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
