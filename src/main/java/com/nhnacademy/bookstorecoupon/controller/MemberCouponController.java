package com.nhnacademy.bookstorecoupon.controller;

import com.nhnacademy.bookstorecoupon.dto.membercoupon.*;
import com.nhnacademy.bookstorecoupon.service.MemberCouponService;
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
     * @param memberCouponCreateRequestDto : 회원 ID, 쿠폰 ID
     * @return : MemberCouponResponseDto 회원쿠폰 ID, 회원 ID, 쿠폰정보들
     */
    @PostMapping
    public ResponseEntity<MemberCouponResponseDto> createMemberCoupon(@RequestBody @Valid MemberCouponCreateRequestDto memberCouponCreateRequestDto) {
        MemberCouponResponseDto memberCouponResponseDto = memberCouponService.createMemberCoupon(memberCouponCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(memberCouponResponseDto);
    }

    /**
     * 회원쿠폰 목록 조회
     * GET /api/member-coupons
     * @param page 페이지 번호 (최소 0)
     * @param pageSize 페이지 크기 (최소 1)
     * @return : MemberCouponResponseDto 회원쿠폰 ID, 회원 ID, 쿠폰정보들
     */
    @GetMapping
    public ResponseEntity<Page<MemberCouponResponseDto>> getAllMemberCoupons(@RequestParam @NotNull @Min(0) int page, @RequestParam @NotNull @Min(1) int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<MemberCouponResponseDto> responseDto = memberCouponService.getAllMemberCoupons(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * 쿠폰 ID 로 회원쿠폰 조회
     * GET /api/member-coupons/{coupon-id}
     * @param couponId : 쿠폰 ID
     * @param page : 페이지 번호
     * @param pageSize : 페이지 크기
     * @return MemberCouponResponseDto 회원쿠폰 ID, 회원 ID, 쿠폰정보들
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
     * 회원 ID 로 회원쿠폰 조회
     * GET /api/member-coupons/member/{member-id}
     * @param memberId : 회원 ID
     * @param pageable : 페이지블 정보
     * @return : Page<MemberCouponGetResponseDto> 쿠폰정책을 포함한 쿠폰의 정보들
     */
    @GetMapping("/member/{member-id}")
    public ResponseEntity<Page<MemberCouponGetResponseDto>> getMemberCouponsByMemberId(@PathVariable("member-id") Long memberId, Pageable pageable) {
        MemberCouponFindByMemberIdRequestDto requestDto = new MemberCouponFindByMemberIdRequestDto(memberId, pageable.getPageNumber(), pageable.getPageSize());
        Page<MemberCouponGetResponseDto> responseDto = memberCouponService.getMemberCouponsByMemberId(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    /**
     * 회원이 보유한 쿠폰 사용
     * PATCH /api/member-coupons/members/{member-id}/coupons/{coupon-id}/use
     * @param memberId : 회원 ID
     * @param couponId : 쿠폰 ID
     * @return : String 쿠폰이 성공적으로 사용되었습니다
     */
    @PatchMapping("/members/{member-id}/coupons/{coupon-id}/use")
    public ResponseEntity<String> useMemberCoupon(@PathVariable("member-id") Long memberId, @PathVariable("coupon-id") Long couponId) {
        MemberCouponUseRequestDto memberCouponUseRequestDto = new MemberCouponUseRequestDto(memberId, couponId);
        memberCouponService.useMemberCoupon(memberCouponUseRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body("쿠폰이 성공적으로 사용되었습니다");
    }


    /**
     * 회원이 사용 가능한 쿠폰 목록 조회 (UNUSED 상태의 쿠폰)
     * GET /api/member-coupons/member/{member-id}/unused
     * @param memberId : 회원 ID
     * @param pageable : 페이지블
     * @return : Page<MemberCouponGetResponseDto> 쿠폰정책을 포함한 쿠폰의 정보들
     */
    @GetMapping("/member/{member-id}/unused")
    public ResponseEntity<Page<MemberCouponGetResponseDto>> getUnusedMemberCouponsByMemberId(@PathVariable("member-id") Long memberId, Pageable pageable) {
        MemberCouponFindByMemberIdRequestDto requestDto = new MemberCouponFindByMemberIdRequestDto(memberId, pageable.getPageNumber(), pageable.getPageSize());
        Page<MemberCouponGetResponseDto> responseDto = memberCouponService.getUnusedMemberCouponsByMemberId(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

}
