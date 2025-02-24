package com.demo.trading.store;

import lombok.Data;

/**
 * @author mohitrawat0123
 */
@Data
public class Stock {
    private Long id;
    private String name;
    private String symbol;
    private Double price;
}
