package ru.practice.currencyconverter.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practice.currencyconverter.config.UrlHolder;
import ru.practice.currencyconverter.model.CurrencyCourse;
import ru.practice.currencyconverter.repository.CourseRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ConvertService {

    private final UrlHolder urlHolder;
    private final WebClient client;
    private final CourseRepository repository;

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

    public void actualize() throws JsonProcessingException {
        String actualCurrency = urlHolder.actualCurrency();
        String baseCurrencies = urlHolder.baseCurrencies();
        String response = client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", "{userKey}")
                        .queryParam("currencies", baseCurrencies)
                        .queryParam("base_currency", actualCurrency)
                        .build(urlHolder.apiKey(), baseCurrencies, actualCurrency))
                .retrieve()
                .bodyToFlux(String.class)
                .blockFirst();

        Map<String, Double> courses = (Map<String, Double>) new ObjectMapper().readValue(response, HashMap.class).get("data");
        CurrencyCourse cc;
        for (String key : courses.keySet()) {
            cc = repository.findByTargetCurrency(key);
            if (cc == null) {
                cc = new CurrencyCourse(null, actualCurrency, key, 1 / courses.get(key));
            } else {
                cc.setCourse(1 / courses.get(key));
            }
            repository.save(cc);
        }
    }
}
