package com.enviro.assessment.grad001.favourokwara.investment.exception;

import org.springframework.http.HttpStatus;

public enum Errors implements ErrorResponse {
    INVESTOR_NOT_FOUND( "INVESTOR_NOT_FOUND", HttpStatus.NOT_FOUND, "Investor with id='%d' not found."),
    PRODUCT_NOT_FOUND( "PRODUCT_NOT_FOUND", HttpStatus.NOT_FOUND, "Product with id='%d' not found."),
    INSUFFICIENT_FUNDS("INSUFFICIENT_FUNDS", HttpStatus.BAD_REQUEST, "Product with id='%d' has insufficient funds."),
    INVESTOR_NOT_ELIGIBLE("INVESTOR_NOT_ELIGIBLE", HttpStatus.FORBIDDEN, "Investor below 65 years of age.");
    
    String key;
    HttpStatus httpStatus;
    String message;

    Errors(String key, HttpStatus httpStatus, String message) {
        this.message = message;
        this.key = key;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getMessage() {
        return message;
    }
    
    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
