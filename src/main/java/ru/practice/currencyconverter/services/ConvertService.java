package ru.practice.currencyconverter.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practice.currencyconverter.config.UrlHolder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ConvertService {

    private final UrlHolder urlHolder;
    private final WebClient client;


    public BigDecimal convert(Long sum, String fromCurrency, String toCurrency) throws JsonProcessingException {

        String response = client
                .get()
                .uri(uriBuilder -> uriBuilder
                .queryParam("apikey", "{userKey}")
                .queryParam("currencies", "{toCurrency}")
                .queryParam("base_currency", "{fromCurrency}")
                .build(urlHolder.apiKey(), toCurrency, fromCurrency))
                .retrieve()
                .bodyToFlux(String.class)
                .blockFirst();
        Map<String, Map<String, Double>> res = new ObjectMapper().readValue(response, HashMap.class);
        Double course = res.get("data").get(toCurrency);

        return BigDecimal.valueOf(sum * course);
    }
}
