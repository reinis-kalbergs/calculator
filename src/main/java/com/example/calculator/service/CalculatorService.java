package com.example.calculator.service;

import com.example.calculator.dto.CompareRequest;
import com.example.calculator.dto.Operation;
import com.example.calculator.dto.CalculatorResult;
import com.example.calculator.dto.SimpleCalculation;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.example.calculator.dto.CompareEnum.LARGEST;

@Service
public class CalculatorService {

    private static final int MAX_NUMBERS = 5;

    public CalculatorResult getSimpleCalculation(SimpleCalculation request) {
        return new CalculatorResult(
                doSimpleCalculation(request.getNumberOne(), request.getNumberTwo(), request.getOperation())
        );
    }

    private Double doSimpleCalculation(Double numberOne, Double numberTwo, Operation operation) {
        switch (operation) {
            case ADDITION -> {
                return numberOne + numberTwo;
            }
            case SUBTRACTION -> {
                return numberOne - numberTwo;
            }
            case MULTIPLICATION -> {
                return numberOne * numberTwo;
            }
            case DIVISION -> {
                return numberOne / numberTwo;
            }
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not recognized operation");
        }
    }

    public CalculatorResult calculateFromString(String exspressionAsAString) {

        validateInput(exspressionAsAString);

        Expression e = new ExpressionBuilder(exspressionAsAString).build();
        if (!e.validate().isValid()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid expression");
        }
        return new CalculatorResult(e.evaluate());
    }

    public Long compareNumbers(CompareRequest request) {
        validateNumberCount(request.getNumbers().size());
        var numberStream = request.getNumbers().stream();
        if (request.getCompareBy() == LARGEST) {
            return numberStream.max(Long::compareTo).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        }
        return numberStream.min(Long::compareTo).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    private void validateInput(String exspressionAsAString) {

        String[] numbersAsString = exspressionAsAString.split("[-+/*]");
        for (String number : numbersAsString) {
            try {
                Long.parseLong(number);
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can only input whole numbers");
            }
        }

        validateNumberCount(numbersAsString.length);
    }

    private void validateNumberCount(long numberCount) {
        if (numberCount > MAX_NUMBERS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Too many numbers");
        }
    }
}
