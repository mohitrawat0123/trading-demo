package com.demo.trading.supports.impl;

import com.demo.trading.constants.OrderStatus;
import com.demo.trading.constants.OrderType;
import com.demo.trading.event.OrderEvent;
import com.demo.trading.store.Order;
import com.demo.trading.store.OrderBook;
import com.demo.trading.supports.StockExchange;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mohitrawat0123
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class StockExchangeImpl implements StockExchange {

    public static final Comparator<Order> buyOrderComparator = (o1, o2) -> {
        if (Objects.equals(o1.getPrice(), o2.getPrice())) {
            return o1.getAcceptedAt().compareTo(o2.getAcceptedAt());
        }
        return o2.getPrice().compareTo(o1.getPrice());
    };
    public static final Comparator<Order> sellOrderComparator = (o1, o2) -> {
        if (Objects.equals(o1.getPrice(), o2.getPrice())) {
            return o1.getAcceptedAt().compareTo(o2.getAcceptedAt());
        }
        return o1.getPrice().compareTo(o2.getPrice());
    };
    private final ApplicationEventPublisher eventPublisher;
    private final Map<String, OrderBook> stockBook = new ConcurrentHashMap<>();
    private final Map<String, Lock> stockLocks = new ConcurrentHashMap<>();

    @Override
    @SneakyThrows
    public void addOrder(Order order) {
        var stockSymbol = order.getStockSymbol();
        var stockLock = getLockForStock(stockSymbol);
        while (!stockLock.tryLock(1, TimeUnit.SECONDS)) {
            log.info("Waiting for stockLock on {} for ADD op.", stockSymbol);
        }
        try {
            var orderBook = stockBook.getOrDefault(stockSymbol, new OrderBook(buyOrderComparator, sellOrderComparator));
            if (OrderType.BUY.equals(order.getOrderType())) {
                orderBook.getBuyOrders().add(order);
            } else {
                orderBook.getSellOrders().add(order);
            }
            printOrderBook(stockSymbol);
            stockBook.put(stockSymbol, orderBook);
            eventPublisher.publishEvent(new OrderEvent(this, stockSymbol));
        } finally {
            stockLock.unlock();
        }
    }

    @Override
    @SneakyThrows
    public void modifyOrder(Order order) {
        var stockSymbol = order.getStockSymbol();
        var stockLock = getLockForStock(stockSymbol);
        while (!stockLock.tryLock(1, TimeUnit.SECONDS)) {
            log.info("Waiting for stockLock on {} for MOD op.", stockSymbol);
        }
        try {
            removeOrder(order);
            addOrder(order);
            printOrderBook(order.getStockSymbol());
            eventPublisher.publishEvent(new OrderEvent(this, stockSymbol));
        } finally {
            stockLock.unlock();
        }
    }

    @SneakyThrows
    @Override
    public void removeOrder(Order order) {
        var stockSymbol = order.getStockSymbol();
        var stockLock = getLockForStock(stockSymbol);
        while (!stockLock.tryLock(1, TimeUnit.SECONDS)) {
            log.info("Waiting for stockLock on {} for REM op.", stockSymbol);
        }
        try {
            var orderBook = stockBook.get(stockSymbol);
            if (null != orderBook) {
                if (OrderType.BUY.equals(order.getOrderType())) {
                    orderBook.getBuyOrders().remove(order);
                } else {
                    orderBook.getSellOrders().remove(order);
                }
                printOrderBook(stockSymbol);
            }
        } finally {
            stockLock.unlock();
        }
    }

    private void printOrderBook(String stockSymbol) {
        var orderBook = stockBook.get(stockSymbol);
        if (null != orderBook) {
            log.info("Order Book for: {}", stockSymbol);
            log.info("Buy Orders -");
            orderBook.getBuyOrders().forEach(order -> log.info("{}", order));
            log.info("Sell Orders -");
            orderBook.getSellOrders().forEach(order -> log.info("{}", order));
            log.info("<--------------------------------------------------------------------------------------------->");
        }
    }

    public void matchOrders(String stockSymbol) {
        var orderBook = stockBook.get(stockSymbol);
        var stockLock = getLockForStock(stockSymbol);
        if (null != orderBook && stockLock.tryLock()) {
            try {
                log.info("Acquired LOCK for: {}", stockSymbol);
                while (!orderBook.getBuyOrders().isEmpty() && !orderBook.getSellOrders().isEmpty()) {
                    var bestBuyOrder = orderBook.getBuyOrders().peek();
                    var bestSellOrder = orderBook.getSellOrders().peek();
                    if (bestBuyOrder.getPrice() >= bestSellOrder.getPrice()) {
                        executeOrder(bestBuyOrder, bestSellOrder, orderBook);
                    } else {
                        log.info("No trade can be performed. Either no buy order or no sell order.");
                        break;
                    }
                }
            } finally {
                log.info("Releasing LOCK for: {}", stockSymbol);
                stockLock.unlock();
            }
        }
    }

    private Lock getLockForStock(String stockSymbol) {
        return stockLocks.computeIfAbsent(stockSymbol, k -> new ReentrantLock());
    }

    @SneakyThrows
    private void executeOrder(Order buyOrder, Order sellOrder, OrderBook orderBook) {
        var tradeQuantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());
        var remainingBuyQuantity = buyOrder.getQuantity() - tradeQuantity;
        var remainingSellQuantity = sellOrder.getQuantity() - tradeQuantity;

        log.info("Executing trade for stock: {} at price: {} quantity: {} ...",
                buyOrder.getStockSymbol(), buyOrder.getPrice(), tradeQuantity);
        log.info("Buy Order: {} <> Sell Order: {}", buyOrder.getId(), sellOrder.getId());

        Thread.sleep(500);

        var now = new Date();
        buyOrder.setQuantity(remainingBuyQuantity);
        buyOrder.setExecutedAt(now);
        sellOrder.setQuantity(remainingSellQuantity);
        sellOrder.setExecutedAt(now);

        if (remainingBuyQuantity == 0) {
            buyOrder.setOrderStatus(OrderStatus.EXECUTED);
            orderBook.getBuyOrders().poll();
        } else {
            buyOrder.setOrderStatus(OrderStatus.PARTIALLY_EXECUTED);
        }

        if (remainingSellQuantity == 0) {
            sellOrder.setOrderStatus(OrderStatus.EXECUTED);
            orderBook.getSellOrders().poll();
        } else {
            sellOrder.setOrderStatus(OrderStatus.PARTIALLY_EXECUTED);
        }
        log.info("Executed trade for stock: {} at price: {} quantity: {}",
                buyOrder.getStockSymbol(), buyOrder.getPrice(), tradeQuantity);

    }

}
