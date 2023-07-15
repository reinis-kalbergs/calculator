package com.example.calculator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StringRequest {
    @NotBlank
    private String request;
}
