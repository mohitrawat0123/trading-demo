package com.demo.trading.dto.response;

import com.demo.trading.constants.OrderStatus;
import com.demo.trading.constants.OrderType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author mohitrawat0123
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    private String orderId;
    private String stockSymbol;
    private Double price;
    private Integer quantity;
    private Integer originalQuantity;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private Date acceptedTime;
    private Date executedTime;
}
