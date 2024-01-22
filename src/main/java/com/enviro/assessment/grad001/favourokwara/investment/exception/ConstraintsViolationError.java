package com.enviro.assessment.grad001.favourokwara.investment.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConstraintsViolationError {

    private String property;
    private String message;
}
