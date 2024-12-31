package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.MemberCoupon;
import com.nhnacademy.boostorenginx.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

    Page<MemberCoupon> findByCoupon_IdOrderByIdAsc(Long couponId, Pageable pageable); // 쿠폰의 ID로 회원쿠폰 조회

    Page<MemberCoupon> findByMcMemberIdOrderByIdAsc(Long mcMemberId, Pageable pageable); // 회원 ID에 해당되는 회원쿠폰 관계들을 모두 조회

    Page<MemberCoupon> findByMcMemberIdAndCoupon_Status(Long mcMemberId, Status status, Pageable pageable); // 회원 ID 와 Status 기준으로 회원쿠폰 조회

    boolean existsByMcMemberIdAndId(Long mcMemberId, Long memberCouponId); // 회원 ID 와 회원쿠폰 ID 로 회원쿠폰이 DB 에 존재하는지 확인
}
