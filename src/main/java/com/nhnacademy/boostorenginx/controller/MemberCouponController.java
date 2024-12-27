package com.nhnacademy.boostorenginx.controller;

import com.nhnacademy.boostorenginx.dto.membercoupon.*;
import com.nhnacademy.boostorenginx.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class MemberCouponController {
    private final MemberCouponService memberCouponService;

    // 회원에게 쿠폰 발급
    @PostMapping("/member-coupon")
    public ResponseEntity<MemberCouponResponseDto> createMemberCoupon(@RequestBody MemberCouponCreateRequestDto requestDto) {
        MemberCouponResponseDto responseDto = memberCouponService.createMemberCoupon(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 회원이 쿠폰 사용
    @PatchMapping("/member-coupon/use")
    public ResponseEntity<String> useMemberCoupon(@RequestBody MemberCouponUseRequestDto requestDto) {
        memberCouponService.useMemberCoupon(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body("쿠폰이 성공적으로 사용되었습니다.");
    }

    // 회원 ID 로 회원쿠폰 조회
    @GetMapping("/member-coupon/{memberId}")
    public ResponseEntity<Page<MemberCouponResponseDto>> getMemberCouponsByMemberId(@PathVariable("memberId") Long memberId, Pageable pageable) {
        MemberCouponFindByMemberIdRequestDto requestDto = new MemberCouponFindByMemberIdRequestDto(memberId, pageable.getPageNumber(), pageable.getPageSize());
        Page<MemberCouponResponseDto> responseDto = memberCouponService.getMemberCouponsByMemberId(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 쿠폰 ID 로 회원쿠폰 조회
    @GetMapping("/member-coupon/coupon/{couponId}")
    public ResponseEntity<Page<MemberCouponResponseDto>> getMemberCouponsByCouponId(@PathVariable("couponId") Long couponId, Pageable pageable) {
        MemberCouponFindByCouponIdRequestDto requestDto = new MemberCouponFindByCouponIdRequestDto(couponId, pageable.getPageNumber(), pageable.getPageSize());
        Page<MemberCouponResponseDto> responseDto = memberCouponService.getMemberCouponsByCouponId(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 회원이 자신이 사용가능한 쿠폰 목록(Status 가 UNUSED 인 쿠폰 목록 조회)
    @GetMapping("/member-coupon/member/{memberId}")
    public ResponseEntity<Page<MemberCouponResponseDto>> getUnusedMemberCouponsByMemberId(@PathVariable("memberId") Long memberId, Pageable pageable) {
        MemberCouponFindByMemberIdRequestDto requestDto = new MemberCouponFindByMemberIdRequestDto(memberId, pageable.getPageNumber(), pageable.getPageSize());
        Page<MemberCouponResponseDto> responseDto = memberCouponService.getUnusedMemberCouponsByMemberId(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
