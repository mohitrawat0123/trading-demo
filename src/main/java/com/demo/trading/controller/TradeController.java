package com.demo.trading.controller;

import com.demo.trading.supports.StockExchange;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author mohitrawat0123
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/trades")
@Tag(name = "Trade Controller", description = "APIs for Trade Management")
public class TradeController {

    private final StockExchange stockExchange;

    @GetMapping("/stock/{symbol}")
    public ResponseEntity<?> getTrades(@NotBlank(message = "Symbol cannot be blank")
                                       @PathVariable(name = "symbol") String symbol) {
        return ResponseEntity.ok(stockExchange.getTrades(symbol));
    }
}
