package com.demo.trading.exception;

import com.demo.trading.constants.ErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author mohitrawat0123
 */
@Log4j2
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleInvalidRequestException(HttpServletRequest request, MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(err -> errorMessage.append(err.getDefaultMessage()).append(" "));
        log.error("InvalidRequestExceptionOccurred - Request: " + request.getRequestURL() + " raised: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class,
            ServletRequestBindingException.class, HttpMessageConversionException.class,
            MethodArgumentTypeMismatchException.class, ConstraintViolationException.class,
            HttpMessageNotReadableException.class, ValidationException.class})
    public ResponseEntity<?> handleInvalidRequestException(HttpServletRequest request, Exception ex) {
        log.error("InvalidRequestExceptionOccurred - Request: " + request.getRequestURL() + " raised: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorEnum.InvalidRequestError.getErrorMessage());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(HttpServletRequest request, CustomException ex) {
        log.error("CustomExceptionOccurred - Request: " + request.getRequestURL() + " raised: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(HttpServletRequest request, Exception ex) {
        log.error("UnexpectedException - Request: " + request.getRequestURL() + " raised: {} ", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorEnum.InternalServerError.getErrorMessage());
    }
}
