package com.example.calculator.controller;

import com.example.calculator.dto.CompareRequest;
import com.example.calculator.dto.CalculatorResult;
import com.example.calculator.dto.SimpleCalculation;
import com.example.calculator.dto.StringRequest;
import com.example.calculator.service.CalculatorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class CalculatorController {

    private final CalculatorService calculatorService;

    @GetMapping("/simple-calculation")
    public CalculatorResult getSimpleCalculation(@RequestBody @Valid SimpleCalculation request) {
        return calculatorService.getSimpleCalculation(request);
    }

    @GetMapping("/calculation")
    public CalculatorResult getSimpleCalculation(@RequestBody @Valid StringRequest request) {
        return calculatorService.calculateFromString(request.getRequest());
    }

    @GetMapping("/compare")
    public Long compareDigits(@RequestBody @Valid CompareRequest request) {
        return calculatorService.compareNumbers(request);
    }

}
