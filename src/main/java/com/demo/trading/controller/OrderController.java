package com.demo.trading.controller;

import com.demo.trading.dto.request.ModifyRequestDTO;
import com.demo.trading.dto.request.OrderRequestDTO;
import com.demo.trading.supports.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author mohitrawat0123
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Order Controller", description = "APIs for Order Management")
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    @Operation(summary = "Place Order", description = "API to place an order")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        orderService.createOrder(orderRequestDTO);
        return ResponseEntity.ok("Order Submitted successfully !!");
    }

    @PatchMapping("/{orderId}")
    @Operation(summary = "Modify Order", description = "API to modify an order")
    public ResponseEntity<?> modifyOrder(@NotBlank(message = "OrderId cannot be blank") @PathVariable(name = "orderId") String orderId,
                                         @Valid @RequestBody ModifyRequestDTO modifyRequestDTO) {
        orderService.modifyOrder(orderId, modifyRequestDTO.getPrice(), modifyRequestDTO.getQuantity());
        return ResponseEntity.ok("Order modified successfully !!");
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Cancel Order", description = "API to cancel an order")
    public ResponseEntity<?> cancelOrder(@NotBlank(message = "OrderId cannot be blank") @PathVariable String orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled successfully !!");
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get Order", description = "API to get details for an order")
    public ResponseEntity<?> getOrder(@NotBlank(message = "OrderId cannot be blank")
                                      @PathVariable(name = "orderId") String orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get Orders", description = "API to get orders for a user")
    public ResponseEntity<?> getOrders(@NotNull(message = "UserId cannot be null")
                                       @PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(orderService.getOrders(userId));
    }

    @GetMapping("/stock/{symbol}")
    @Operation(summary = "Get Open Orders", description = "API to get open orders for a stock")
    public ResponseEntity<?> getOpenOrders(@NotBlank(message = "Symbol cannot be blank")
                                           @PathVariable(name = "symbol") String symbol) {
        return ResponseEntity.ok(orderService.getStockOrders(symbol));
    }

}
