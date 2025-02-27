package com.demo.trading.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author mohitrawat0123
 */
@Data
@Builder
public class TradeDTO {
    private String buyOrderId;
    private String sellOrderId;
    private Integer quantity;
    private Double price;
    private Date executedTimeStamp;
}
