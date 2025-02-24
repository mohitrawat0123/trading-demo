package com.demo.trading.exception;

import com.demo.trading.constants.ErrorEnum;
import lombok.Getter;

/**
 * @author mohitrawat0123
 */
@Getter
public class CustomException extends Exception {

    private String errorCode;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(ErrorEnum errorEnum) {
        super(errorEnum.getErrorMessage());
        this.errorCode = errorEnum.getErrorCode();
    }

    public CustomException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
