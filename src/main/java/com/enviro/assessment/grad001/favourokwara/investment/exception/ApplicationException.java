package com.enviro.assessment.grad001.favourokwara.investment.exception;

import java.util.List;


public class ApplicationException extends RuntimeException {
    
    static final Long serialVersionUID = 1L;

    private final ErrorResponse errorResponse;
    private final List<Object> messageArguments;

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public ApplicationException(ErrorResponse errorResponse, List<Object> messages) {
        this.errorResponse = errorResponse;
        this.messageArguments = messages;
    }
    
    public ApplicationException(ErrorResponse errorResponse, List<Object> messages, Throwable cause) {
        super(cause);
        this.errorResponse = errorResponse;
        this.messageArguments = messages;
    }

    public ApplicationException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
        this.messageArguments = List.of();
    }


    public ApplicationException(ErrorResponse errorResponse, Throwable cause) {
        super(cause);
        this.errorResponse = errorResponse;
        this.messageArguments = List.of();
    }

    @Override
    public String getMessage() {
        return messageArguments.isEmpty()? 
            errorResponse.getMessage() : 
            String.format(
                errorResponse.getMessage(),
                messageArguments.stream().toArray()
            );
    }
}
