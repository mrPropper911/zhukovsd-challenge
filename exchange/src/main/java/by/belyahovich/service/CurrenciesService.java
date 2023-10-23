package by.belyahovich.service;

import by.belyahovich.domain.Currencies;

import java.util.List;
import java.util.Optional;

public interface CurrenciesService {
    List<Currencies> getAll();

    Optional<Currencies> getByCode(String code);

}
