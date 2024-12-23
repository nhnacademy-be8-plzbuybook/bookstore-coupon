package com.nhnacademy.boostorenginx.dto.sellingbook;

import com.nhnacademy.boostorenginx.enums.SellingBookStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Setter
@Getter
@AllArgsConstructor
public class SellingBookResponseDto {
    private Long sellingBookId;
    private Long bookId;
    private BigDecimal sellingBookPrice;
    private Boolean sellingBookPackageable;
    private Integer sellingBookStock;
    private SellingBookStatus sellingBookStatus;
    private Boolean used;
    private Long sellingBookViewCount;
}