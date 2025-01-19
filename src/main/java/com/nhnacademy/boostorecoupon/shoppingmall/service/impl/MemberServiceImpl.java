package com.nhnacademy.boostorecoupon.shoppingmall.service.impl;

import com.nhnacademy.boostorecoupon.error.feign.NotFoundMemberException;
import com.nhnacademy.boostorecoupon.shoppingmall.MemberClient;
import com.nhnacademy.boostorecoupon.shoppingmall.service.MemberService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberClient memberClient;

    @Override
    public Long getMemberIdByEmail(String email) {
        try {
            Long memberId = memberClient.getMemberIdByEmail(email).getBody();

            if (memberId == null || memberId <= 0) {
                throw new NotFoundMemberException("이메일에 해당하는 회원 식별키를 찾지 못했습니다: " + memberId);
            }

            return memberId;
        } catch (FeignException | NotFoundMemberException e) {
            log.error(e.getMessage());
            throw new NotFoundMemberException(e.getMessage());
        }
    }
}
