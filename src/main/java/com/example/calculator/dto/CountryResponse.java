package com.example.calculator.dto;

import java.util.List;

public record CountryResponse (List<CountryDTO> countries, String currentLocation){}
