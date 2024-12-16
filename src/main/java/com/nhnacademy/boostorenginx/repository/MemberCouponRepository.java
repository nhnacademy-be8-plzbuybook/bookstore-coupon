package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.MemberCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
    Page<MemberCoupon> findAllByCoupon(Coupon coupon, Pageable pageable); // 특정 쿠폰으로 페이징 조회

    List<MemberCoupon> findMemberCouponByCoupon(Coupon coupon); // 쿠폰으로 회원쿠폰들 조회

    List<MemberCoupon> findByMcMemberId(Long mcMemberId); // 특정 회원 ID 로 멤버쿠폰 객체 조회
}
