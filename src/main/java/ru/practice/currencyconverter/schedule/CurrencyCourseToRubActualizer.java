package ru.practice.currencyconverter.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.practice.currencyconverter.services.ConvertService;

import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class CurrencyCourseToRubActualizer {
    private final Logger log =
            LoggerFactory.getLogger(CurrencyCourseToRubActualizer.class);
    ConvertService service;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void actualize() throws JsonProcessingException {
        log.info("Start actualization courses...");
        service.actualize();
        log.info("Courses are actualized...");
    }
}

