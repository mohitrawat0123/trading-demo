package com.demo.trading.event;

import com.demo.trading.supports.StockExchange;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author mohitrawat0123
 */
@Component
@RequiredArgsConstructor
public class OrderEventListener implements ApplicationListener<OrderEvent> {

    private final StockExchange stockExchange;

    @Override
    public void onApplicationEvent(OrderEvent event) {
        MDC.put("traceId", event.getTraceId());
        stockExchange.matchOrders(event.getStockSymbol());
    }
}
