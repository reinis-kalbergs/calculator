package com.example.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompareRequest {
    List<Long> numbers;
    CompareEnum compareBy;
}
