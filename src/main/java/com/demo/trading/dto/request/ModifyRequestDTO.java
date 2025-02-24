package com.demo.trading.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author mohitrawat0123
 */
@Data
public class ModifyRequestDTO {
    @NotNull(message = "Price cannot be null")
    @Min(value = 1, message = "Price cannot be below 1")
    private Double price;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity cannot be below 1")
    private Integer quantity;
}
