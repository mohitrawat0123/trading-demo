package com.demo.trading.store;

import lombok.Data;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author mohitrawat0123
 */
@Data
public class OrderBook {
    private PriorityQueue<Order> buyOrders;
    private PriorityQueue<Order> sellOrders;

//    private Map<Long, Order> orderMap;

    public OrderBook(Comparator<Order> buyOrderComparator, Comparator<Order> sellOrderComparator) {
        this.buyOrders = new PriorityQueue<>(buyOrderComparator);
        this.sellOrders = new PriorityQueue<>(sellOrderComparator);
    }
}
