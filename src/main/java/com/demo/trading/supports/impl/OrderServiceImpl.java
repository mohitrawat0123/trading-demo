package com.demo.trading.supports.impl;

import com.demo.trading.constants.ErrorEnum;
import com.demo.trading.constants.OrderExpiry;
import com.demo.trading.constants.OrderStatus;
import com.demo.trading.dto.request.OrderRequestDTO;
import com.demo.trading.dto.response.OrderDTO;
import com.demo.trading.exception.CustomException;
import com.demo.trading.store.Order;
import com.demo.trading.supports.OrderService;
import com.demo.trading.supports.StockExchange;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mohitrawat0123
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final StockExchange stockExchange;

    //Keeping order lookup at broker side, for querying and initial lookup
    Map<String, Order> orderMap = new ConcurrentHashMap<>();

    @Override
    public void createOrder(OrderRequestDTO orderRequestDTO) {
        var order = Order.builder()
                .id("ORD:" + UUID.randomUUID())
                .userId(orderRequestDTO.getUserId())
                .stockSymbol(orderRequestDTO.getStockSymbol())
                .orderType(orderRequestDTO.getOrderType())
                .quantity(orderRequestDTO.getQuantity())
                .price(orderRequestDTO.getPrice())
                .orderExpiry(orderRequestDTO.getOrderExpiry())
                .acceptedAt(new Date())
                .orderStatus(OrderStatus.ACCEPTED)
                .build();
        orderMap.put(order.getId(), order);
        stockExchange.addOrder(order);
    }

    @Override
    @SneakyThrows
    public void modifyOrder(String orderId, Double price, Integer quantity) {
        if (orderMap.containsKey(orderId)) {
            var order = orderMap.get(orderId);
            order.setQuantity(quantity);
            order.setPrice(price);
            stockExchange.modifyOrder(order);
        }
        throw new CustomException(ErrorEnum.OrderNotFoundError);
    }

    @Override
    @SneakyThrows
    public void cancelOrder(String orderId) {
        if (orderMap.containsKey(orderId)) {
            var order = orderMap.get(orderId);
            stockExchange.removeOrder(order);
            order.setOrderStatus(OrderStatus.CANCELLED);
        }
        throw new CustomException(ErrorEnum.OrderNotFoundError);
    }

    @Override
    @SneakyThrows
    public OrderDTO getOrder(String orderId) {
        if (orderMap.containsKey(orderId)) {
            var order = orderMap.get(orderId);
            return OrderDTO.builder()
                    .orderId(order.getId())
                    .stockSymbol(order.getStockSymbol())
                    .price(order.getPrice())
                    .originalQuantity(order.getOriginalQuantity())
                    .quantity(order.getQuantity())
                    .orderType(order.getOrderType())
                    .acceptedTime(order.getAcceptedAt())
                    .executedTime(order.getExecutedAt())
                    .orderStatus(order.getOrderStatus())
                    .build();
        }
        throw new CustomException(ErrorEnum.OrderNotFoundError);
    }

    @Override
    public List<OrderDTO> getOrders(Long userId) {
        return orderMap.values().stream()
                .filter(order -> order.getUserId().equals(userId))
                .map(order -> OrderDTO.builder()
                        .orderId(order.getId())
                        .stockSymbol(order.getStockSymbol())
                        .price(order.getPrice())
                        .originalQuantity(order.getOriginalQuantity())
                        .quantity(order.getQuantity())
                        .orderType(order.getOrderType())
                        .acceptedTime(order.getAcceptedAt())
                        .executedTime(order.getExecutedAt())
                        .orderStatus(order.getOrderStatus())
                        .build())
                .toList();
    }

    @Override
    public List<OrderDTO> getStockOrders(String symbol) {
        return orderMap.values().stream()
                .filter(order -> order.getStockSymbol().equals(symbol) &&
                        (!OrderStatus.EXECUTED.equals(order.getOrderStatus()) &&
                                !OrderStatus.CANCELLED.equals(order.getOrderStatus())))
                .map(order -> OrderDTO.builder()
                        .orderId(order.getId())
                        .price(order.getPrice())
                        .quantity(order.getQuantity())
                        .acceptedTime(order.getAcceptedAt())
                        .orderType(order.getOrderType())
                        .build())
                .toList();
    }

    @Override
    public List<Order> getExpiringOrders(OrderExpiry orderExpiry) {
        return orderMap.values().stream()
                .filter(order -> (!OrderStatus.EXECUTED.equals(order.getOrderStatus()) &&
                        !OrderStatus.CANCELLED.equals(order.getOrderStatus())))
                .filter(order -> order.getOrderExpiry().equals(orderExpiry) && isExpired(order, orderExpiry))
                .toList();
    }

    private boolean isExpired(Order order, OrderExpiry orderExpiry) {
        var IST_ZONE = ZoneId.of("Asia/Kolkata");
        switch (orderExpiry) {
            case DAY -> {
                LocalDate today = LocalDate.now();
                LocalDate acceptedDate = order.getAcceptedAt()
                        .toInstant()
                        .atZone(IST_ZONE)
                        .toLocalDate();
                ;
                return today.isEqual(acceptedDate);
            }
            case YEAR -> {
                LocalDate today = LocalDate.now();
                LocalDate acceptedDate = order.getAcceptedAt()
                        .toInstant()
                        .atZone(IST_ZONE)
                        .toLocalDate();
                return today.isAfter(acceptedDate.plusYears(1));
            }
        }
        return false;
    }
}
