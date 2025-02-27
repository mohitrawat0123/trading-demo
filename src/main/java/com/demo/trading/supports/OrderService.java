package com.demo.trading.supports;

import com.demo.trading.constants.OrderExpiry;
import com.demo.trading.dto.request.OrderRequestDTO;
import com.demo.trading.dto.response.OrderDTO;
import com.demo.trading.store.Order;

import java.util.List;

/**
 * @author mohitrawat0123
 */
public interface OrderService {
    void createOrder(OrderRequestDTO orderRequestDTO);

    void modifyOrder(String orderId, Double price, Integer quantity);

    void cancelOrder(String orderId);

    OrderDTO getOrder(String orderId);

    List<OrderDTO> getOrders(Long userId);

    List<OrderDTO> getStockOrders(String symbol);

    List<Order> getExpiringOrders(OrderExpiry orderExpiry);

    void notifyExecution(String orderId);
}
