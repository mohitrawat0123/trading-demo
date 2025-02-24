package com.demo.trading.supports;

import com.demo.trading.store.Order;

/**
 * @author mohitrawat0123
 */
public interface StockExchange {
    void addOrder(Order order);

    void modifyOrder(Order order);

    void removeOrder(Order order);

    void matchOrders(String stockSymbol);
}
