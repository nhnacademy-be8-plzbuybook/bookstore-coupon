package com.nhnacademy.boostorecoupon.repository;

import com.nhnacademy.boostorecoupon.entity.MemberCoupon;
import com.nhnacademy.boostorecoupon.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

    Optional<MemberCoupon> findByMcMemberIdAndCoupon_Id(Long mcMemberId, Long couponId); // 회원 ID 와 쿠폰 ID 로 회원쿠폰 조회

    Page<MemberCoupon> findByCoupon_IdOrderByIdAsc(Long couponId, Pageable pageable); // 쿠폰의 ID에 해당되는 회원쿠폰 목록 조회

    Page<MemberCoupon> findByMcMemberIdOrderByIdAsc(Long mcMemberId, Pageable pageable); // 회원 ID에 해당되는 회원쿠폰 목록 조회

    Page<MemberCoupon> findByMcMemberIdAndCoupon_StatusOrderByIdAsc(Long mcMemberId, Status couponStatus, Pageable pageable); // 회원 ID 와 쿠폰의 Status 로 회원쿠폰 목록 조회

    boolean existsByMcMemberIdAndId(Long mcMemberId, Long memberCouponId); // 회원 ID 와 회원쿠폰 ID 로 회원쿠폰 존재여부 확인

}
