package com.demo.trading.store;

import lombok.Data;

import java.util.Date;

/**
 * @author mohitrawat0123
 */
@Data
public class Trade {
    private Long id;
    private Long buyerOrderId;
    private Long sellerOrderId;
    private Long stockId;
    private Integer quantity;
    private Double price;
    private Date tradeTimeStamp;
}
