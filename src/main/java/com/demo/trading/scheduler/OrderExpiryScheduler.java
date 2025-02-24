package com.demo.trading.scheduler;

import com.demo.trading.constants.OrderExpiry;
import com.demo.trading.supports.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author mohitrawat0123
 */
@Log4j2
@Component
@RequiredArgsConstructor
public class OrderExpiryScheduler {

    private final OrderService orderService;

    @Scheduled(cron = "0 31 15 * * ?")
    public void removeIntradayOrders() {
        var expiringOrders = orderService.getExpiringOrders(OrderExpiry.DAY);
        expiringOrders.forEach(order -> orderService.cancelOrder(order.getId()));
    }
}
