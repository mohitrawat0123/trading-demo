package com.demo.trading.event;

import com.demo.trading.supports.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author mohitrawat0123
 */
@Component
@RequiredArgsConstructor
public class TradeEventListener implements ApplicationListener<TradeEvent> {

    private final OrderService orderService;

    @Override
    public void onApplicationEvent(TradeEvent event) {
        var buyOrderId = event.getBuyOrderId();
        var sellOrderId = event.getSellOrderId();
        orderService.notifyExecution(buyOrderId);
        orderService.notifyExecution(sellOrderId);
    }
}
