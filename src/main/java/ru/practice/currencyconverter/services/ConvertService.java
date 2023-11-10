package ru.practice.currencyconverter.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practice.currencyconverter.config.UrlHolder;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ConvertService {

    private final UrlHolder urlHolder;
    private final WebClient client;


    public BigDecimal convert(Long sum, String fromCurrency, String toCurrency) throws JsonProcessingException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Mono<Void> responseWritten = client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("apikey", "{userKey}")
                        .queryParam("currencies", "{toCurrency}")
                        .queryParam("base_currency", "{fromCurrency}")
                        .build(urlHolder.apiKey(), toCurrency, fromCurrency))
                .exchange()
                .flatMap(response -> {
                    if (MediaType.APPLICATION_JSON_UTF8.equals(response.headers().contentType().get())) {
                        Flux<DataBuffer> body = response.bodyToFlux(DataBuffer.class);
                        return createSpreadsheet(body, os);
                    } else {
                        System.out.println("No content found");
                        return null;
                    }
                });

        String str = os.toString();
        Map<String, Map<String, Double>> res = new ObjectMapper().readValue(str, HashMap.class);
        Double course = res.get("data").get(toCurrency);

        return BigDecimal.valueOf(sum * course);
    }


    private static Mono<Void> createSpreadsheet(Flux<DataBuffer> body, ByteArrayOutputStream os) {
        return DataBufferUtils.write(body, os).map(DataBufferUtils::release).then();
    }
}
