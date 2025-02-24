package com.demo.trading.store;

import com.demo.trading.constants.OrderExpiry;
import com.demo.trading.constants.OrderStatus;
import com.demo.trading.constants.OrderType;
import lombok.*;

import java.util.Date;

/**
 * @author mohitrawat0123
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Order {
    private String id;
    private String stockSymbol;
    private Long userId;
    private Integer quantity;
    private Integer originalQuantity;
    private Double price;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private Date acceptedAt;
    private Date executedAt;
    private OrderExpiry orderExpiry;

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", stock=" + stockSymbol +
                ", userId=" + userId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", orderType=" + orderType +
                ", orderStatus=" + orderStatus +
                ", acceptedAt=" + acceptedAt +
                '}';
    }
}
