package com.nhnacademy.boostorenginx.repository;

import com.nhnacademy.boostorenginx.entity.Coupon;
import com.nhnacademy.boostorenginx.entity.MemberCoupon;
import com.nhnacademy.boostorenginx.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MemberCouponRepositoryTest {

    @Mock
    MemberCouponRepository memberCouponRepository;

    private Coupon coupon;
    private MemberCoupon memberCoupon1;
    private MemberCoupon memberCoupon2;

    @BeforeEach
    void setUp() {
        coupon = new Coupon(
                Status.UNUSED,
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().plusDays(5),
                null
        );

        memberCoupon1 = new MemberCoupon(1L, coupon);
        memberCoupon2 = new MemberCoupon(2L, coupon);
    }

    @Test
    void findAllByCoupon() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<MemberCoupon> mockPage = new PageImpl<>(List.of(memberCoupon1, memberCoupon2), pageable, 2);
        when(memberCouponRepository.findAllByCoupon(coupon, pageable)).thenReturn(mockPage);
        Page<MemberCoupon> result = memberCouponRepository.findAllByCoupon(coupon, pageable);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).extracting("mcMemberId").containsExactly(1L, 2L);
    }

    @Test
    void findMemberCouponByCoupon() {
        when(memberCouponRepository.findMemberCouponByCoupon(coupon)).thenReturn(List.of(memberCoupon1, memberCoupon2));
        List<MemberCoupon> result = memberCouponRepository.findMemberCouponByCoupon(coupon);
        assertThat(result).hasSize(2);
        assertThat(result).extracting("mcMemberId").containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void findByMcMemberId() {
        when(memberCouponRepository.findByMcMemberId(1L)).thenReturn(List.of(memberCoupon1));
        List<MemberCoupon> result = memberCouponRepository.findByMcMemberId(1L);
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getMcMemberId()).isEqualTo(1L);
    }
}
