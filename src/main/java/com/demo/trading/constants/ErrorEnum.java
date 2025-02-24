package com.demo.trading.constants;

import lombok.Getter;

@Getter
public enum ErrorEnum {

    InternalServerError("ERR_000", "Uh-oh ! Something went wrong."),
    InvalidRequestError("ERR_001", "Invalid request. Pls check your inputs."),
    OrderNotFoundError("ERR_002", "Order not found.");

    private final String errorCode;
    private final String errorMessage;


    ErrorEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
