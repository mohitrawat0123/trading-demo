package com.demo.trading.dto.request;

import com.demo.trading.constants.OrderExpiry;
import com.demo.trading.constants.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author mohitrawat0123
 */
@Data
public class OrderRequestDTO {
    @NotNull(message = "UserId cannot be null")
    private Long userId;

    @NotBlank(message = "Stock Symbol cannot be blank")
    private String stockSymbol;

    @NotNull(message = "Price cannot be null")
    @Min(value = 1, message = "Price cannot be below 1")
    private Double price;

    @NotNull(message = "Order Type cannot be null")
    private OrderType orderType;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity cannot be below 1")
    private Integer quantity;

    @NotNull(message = "Order Expiry cannot be null")
    private OrderExpiry orderExpiry;
}
