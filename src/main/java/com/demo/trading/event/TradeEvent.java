package com.demo.trading.event;

import lombok.Data;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEvent;

/**
 * @author mohitrawat0123
 */
@Data
public class TradeEvent extends ApplicationEvent {

    private final String traceId;
    private final String buyOrderId;
    private final String sellOrderId;

    public TradeEvent(Object source, String buyOrderId, String sellOrderId) {
        super(source);
        this.traceId = MDC.get("traceId");
        this.buyOrderId = buyOrderId;
        this.sellOrderId = sellOrderId;
    }
}
