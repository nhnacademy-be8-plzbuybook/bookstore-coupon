package com.nhnacademy.boostorenginx.entity;

import com.nhnacademy.boostorenginx.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @ManyToOne
    @JoinColumn(name = "coupon_policy_id", nullable = false)
    private CouponPolicy couponPolicy;

    @OneToMany(mappedBy = "coupon")
    private List<CouponHistory> couponHistoryList = new ArrayList<>();

    public Coupon(Status status, LocalDateTime issuedAt, LocalDateTime expiredAt, CouponPolicy couponPolicy) {
        this.code = UUID.randomUUID().toString();
        this.status = status;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
        this.couponPolicy = couponPolicy;
    }

    // 쿠폰에서 CouponHistory 객체 생성 -> coupon 생성자를 바로 넘길 수 있음 -> 불변객체 유지 가능
    public void addHistory(Status status, LocalDateTime changeDate, String reason) {
        CouponHistory history = CouponHistory.builder()
                .coupon(this)
                .status(status)
                .changeDate(changeDate)
                .reason(reason)
                .build();

        couponHistoryList.add(history); // 양방향관계이므로 coupon 에서 couponHistory 를 저장함
    }

}
