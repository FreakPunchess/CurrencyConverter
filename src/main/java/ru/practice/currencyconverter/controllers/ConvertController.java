package ru.practice.currencyconverter.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practice.currencyconverter.services.ConvertService;

import java.math.BigDecimal;

@RestController()
public class ConvertController {

    private ConvertService service;

    public ConvertController(ConvertService service) {
        this.service = service;
    }

    @GetMapping("/convert")
    public BigDecimal convert(@RequestParam Long sum,
                              @RequestParam String fromCurrency,
                              @RequestParam String toCurrency) throws JsonProcessingException {
        return service.convert(sum, fromCurrency, toCurrency);
    }
}
