package com.demo.trading.event;

import lombok.Getter;
import org.slf4j.MDC;
import org.springframework.context.ApplicationEvent;

/**
 * @author mohitrawat0123
 */
@Getter
public class OrderEvent extends ApplicationEvent {

    private final String traceId;
    private final String stockSymbol;

    public OrderEvent(Object source, String stockSymbol) {
        super(source);
        this.traceId = MDC.get("traceId");
        this.stockSymbol = stockSymbol;
    }
}
