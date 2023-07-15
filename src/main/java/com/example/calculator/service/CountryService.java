package com.example.calculator.service;

import com.example.calculator.config.CustomClientException;
import com.example.calculator.dto.CountryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.calculator.dto.CountryDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final WebClient.Builder webClientBuilder;

    private WebClient countriesWebClient;
    private WebClient geoLocationWebClient;

    @Value("${api.url.countries}")
    public void setCountriesApiUrl(String countriesApiUrl) {
        this.countriesWebClient = webClientBuilder.baseUrl(countriesApiUrl).build();
    }
    @Value("${api.url.geo-location}")
    public void setGeoLocationApiUrl(String geoLocationApiUrl) {
        this.geoLocationWebClient = webClientBuilder.baseUrl(geoLocationApiUrl).build();
    }

    public Mono<CountryResponse> getCountriesAndCurrentLocation(String clientIP) {
        return Mono.zip(
                getCountries().collectList(),
                getCurrentLocationCountryName(clientIP)
        ).map(tuple -> new CountryResponse(tuple.getT1(), tuple.getT2()));
    }

    private Flux<CountryDTO> getCountries() {
        return countriesWebClient
                .get()
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new CustomClientException("Client error from external service")))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new CustomClientException("Server error from external service")))
                .bodyToFlux(Map.class)
                .map(countryMap -> {
                    Map<String, String> nameMap = (Map<String, String>) countryMap.get("name");
                    return new CountryDTO(nameMap.get("common"));
                })
                .sort(Comparator.comparing(CountryDTO::name));
    }

    private Mono<String> getCurrentLocationCountryName(String clientIP) {
        if ("0:0:0:0:0:0:0:1".equals(clientIP) || "127.0.0.1".equals(clientIP)) {
            return Mono.just("Not available");
        }

        return geoLocationWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.pathSegment(clientIP).build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new CustomClientException("Client Error from External Service")))
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new CustomClientException("Server Error from External Service")))
                .bodyToMono(Map.class)
                .map(geoData -> (String) geoData.get("country"));
    }
}
