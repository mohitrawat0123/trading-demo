package com.demo.trading.store;

import lombok.Data;

/**
 * @author mohitrawat0123
 */
@Data
public class Book {
//    private Map<Double, OrderChain> priceOrders;
//
//    public void addOrder(Long userId, Long stockId, OrderType orderType, Integer quantity, Double price) {
//        var order = Order.builder()
//                .userId(userId)
//                .stockId(stockId)
//                .quantity(quantity)
//                .price(price)
//                .orderStatus(OrderStatus.ACCEPTED)
//                .orderAcceptedTimeStamp(new Date())
//                .orderType(orderType)
//                .build();
//
//        OrderChain orderChain = priceOrders.getOrDefault(price, new OrderChain());
//        var head = orderChain.getHead();
//        order.setNext(head.getNext());
//        head.getNext().setPrev(order);
//        head.setNext(order);
//        order.setPrev(head);
//    }
}
