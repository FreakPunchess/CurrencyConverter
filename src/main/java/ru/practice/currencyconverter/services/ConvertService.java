package ru.practice.currencyconverter.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practice.currencyconverter.config.UrlHolder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConvertService {

    @Autowired
    private UrlHolder urlHolder;

    public BigDecimal convert(Long sum, String fromCurrency, String toCurrency) throws JsonProcessingException {
        final String uri = String.format(urlHolder.getConverterUrl(), toCurrency, fromCurrency);

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        Map<String, Map<String, Double>> res = new ObjectMapper().readValue(result, HashMap.class);
        Double course = res.get("data").get(toCurrency);

        return BigDecimal.valueOf(sum * course);
    }
}
