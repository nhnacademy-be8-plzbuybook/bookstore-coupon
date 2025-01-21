package com.nhnacademy.bookstorecoupon.shoppingmall.service.impl;

import com.nhnacademy.bookstorecoupon.error.feign.NotFoundMemberException;
import com.nhnacademy.bookstorecoupon.shoppingmall.MemberClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberClient memberClient;

    @DisplayName("이메일로 회원 식별키 조회")
    @Test
    void getMemberIdByEmail() {
        String email = "test@example.com";
        Long mockMemberId = 1L;

        when(memberClient.getMemberIdByEmail(email)).thenReturn(ResponseEntity.ok(mockMemberId));

        Long memberId = memberService.getMemberIdByEmail(email);

        assertNotNull(memberId);
        assertEquals(mockMemberId, memberId);
        verify(memberClient, times(1)).getMemberIdByEmail(email);
    }

    @DisplayName("이메일로 회원 식별키 조회에 실패한 경우")
    @Test
    void getMemberIdByEmail_NotFoundMemberException() {
        String email = "test@example.com";

        when(memberClient.getMemberIdByEmail(email)).thenReturn(ResponseEntity.ok(null));

        NotFoundMemberException exception = assertThrows(NotFoundMemberException.class, () -> {
            memberService.getMemberIdByEmail(email);
        });

        assertEquals("이메일에 해당하는 회원 식별키를 찾지 못했습니다: null", exception.getMessage());
        verify(memberClient, times(1)).getMemberIdByEmail(email);
    }

}