package com.nhnacademy.boostorenginx.dto.sellingbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Setter
@Getter
@AllArgsConstructor
public class BookDetailResponseDto {
    private Long bookId;
    private Long sellingBookId;         // 판매책 ID
    private String bookTitle;           // 책 제목
    private String bookIndex;           // 목차
    private String bookDescription;     // 설명
    private LocalDate bookPubDate;      // 출판일
    private BigDecimal bookPriceStandard; // 정가
    private BigDecimal sellingPrice;    // 판매가 (추가)
    private String bookIsbn13;          // ISBN
    private Long publisherId;           // 출판사 ID
    private String publisher;           // 출판사 이름 (추가)
    private String imageUrl;            // 이미지 URL
    private List<String> categories;    // 카테고리 목록
    private List<String> authorName;    // 작가 목록
    private String status;              // 판매 상태 (추가)
    private Long likeCount;              // 좋아요 수 (추가)
}