package by.belyahovich.repository;

import by.belyahovich.domain.Currencies;

import java.util.Optional;

public interface CurrenciesRepository {

    void save(Currencies currencies);

    Iterable<Currencies> getAll();

    Optional<Currencies> getByCode(String code);
}
