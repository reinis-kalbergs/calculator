package com.example.calculator.controller;

import com.example.calculator.dto.CountryResponse;
import com.example.calculator.service.CountryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class CountriesController {

    private final CountryService countryService;

    @GetMapping("countries")
    public Mono<CountryResponse> getCountries(HttpServletRequest request) {
        String clientIP = request.getRemoteAddr();
        return countryService.getCountriesAndCurrentLocation(clientIP);
    }

}
