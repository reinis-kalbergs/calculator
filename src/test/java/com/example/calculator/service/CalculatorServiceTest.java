package com.example.calculator.service;

import com.example.calculator.dto.CalculatorResult;
import com.example.calculator.dto.CompareEnum;
import com.example.calculator.dto.CompareRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    public void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    public void testValidExpression() {
        String expression = "3+3*4/3";
        CalculatorResult calculatorResult = calculatorService.calculateFromString(expression);
        assertEquals(7, calculatorResult.result(), 0.0001);
    }

    @Test
    public void testTooManyNumbers() {
        String expression = "1+2+3+4+5+6";
        assertThrows(ResponseStatusException.class, () -> calculatorService.calculateFromString(expression));
    }

    @Test
    void testValidateNumberCount_ThrowsException_WhenExceedingMaxNumbers() {
        List<Long> numbers = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            numbers.add((long) i);
        }
        CompareRequest request = new CompareRequest(numbers, CompareEnum.LARGEST);

        assertThrows(ResponseStatusException.class, () -> calculatorService.compareNumbers(request));
    }

    @Test
    void testCompareNumbers_ReturnsLargestNumber_WhenCompareTypeIsLargest() {
        List<Long> numbers = Arrays.asList(5L, 7L, 3L, 8L, 6L);
        CompareRequest request = new CompareRequest(numbers, CompareEnum.LARGEST);

        Long result = calculatorService.compareNumbers(request);

        assertEquals(8L, result);
    }

    @Test
    void testCompareNumbers_ThrowsException_WhenListIsEmpty_AndCompareTypeIsSmallest() {
        List<Long> numbers = new ArrayList<>();
        CompareRequest request = new CompareRequest(numbers, CompareEnum.SMALLEST);

        assertThrows(ResponseStatusException.class, () -> calculatorService.compareNumbers(request));
    }

}
