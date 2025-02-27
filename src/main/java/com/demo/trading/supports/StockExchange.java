package com.demo.trading.supports;

import com.demo.trading.dto.response.TradeDTO;
import com.demo.trading.store.Order;

import java.util.List;

/**
 * @author mohitrawat0123
 */
public interface StockExchange {
    void addOrder(Order order);

    void modifyOrder(Order order);

    void removeOrder(Order order);

    void matchOrders(String stockSymbol);

    List<TradeDTO> getTrades(String stockSymbol);
}
