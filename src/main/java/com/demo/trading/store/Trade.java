package com.demo.trading.store;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author mohitrawat0123
 */
@Builder
@Data
public class Trade {
    private String id;
    private String buyerOrderId;
    private String sellerOrderId;
    private String stockId;
    private Integer quantity;
    private Double price;
    private Date tradeTimeStamp;
}
