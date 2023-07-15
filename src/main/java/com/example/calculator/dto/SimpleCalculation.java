package com.example.calculator.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SimpleCalculation {
    @NotNull
    private Double numberOne;
    @NotNull
    private Double numberTwo;
    @Valid
    @NotNull
    private Operation operation;
}
