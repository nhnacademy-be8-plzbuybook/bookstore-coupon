package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.MemberCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {

    Page<MemberCoupon> findMemberCouponByMemberCouponIdOrderByMemberCouponIdAsc(Long couponId, Pageable pageable); // ID 로 회원쿠폰 조회

    Page<MemberCoupon> findByMcMemberIdOrderByMcMemberIdAsc(Long mcMemberId, Pageable pageable); // 특정 회원 ID 에 해당되는 쿠폰 ID 들을 조회

    boolean existsByMcMemberIdAndMemberCouponId(Long mcMemberId, Long memberCouponId); // 회원 ID 와 회원쿠폰 ID 가 DB 에 존재하는지 확인
}
