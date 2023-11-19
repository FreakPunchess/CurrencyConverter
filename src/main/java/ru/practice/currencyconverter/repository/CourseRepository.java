package ru.practice.currencyconverter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practice.currencyconverter.model.CurrencyCourse;

@Repository
public interface CourseRepository extends JpaRepository<CurrencyCourse, Long> {
    CurrencyCourse findByTargetCurrency(String targetCurrency);
}
