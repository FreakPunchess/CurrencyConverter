package ru.practice.currencyconverter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:URL.properties")
public class AppConfig {

    @Value("${base-url}")
    private String baseUrl;

    @Value("${api-key}")
    private String apiKey;

    @Value("${req-param}")
    private String reqParam;


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public UrlHolder urlHolder() {
        return new UrlHolder(apiKey, reqParam);
    }

    @Bean
    public WebClient client() {
        return WebClient.create(baseUrl);
    }
}
