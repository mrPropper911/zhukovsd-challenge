package by.belyahovich.repository;

import by.belyahovich.domain.Currencies;

import java.sql.SQLException;
import java.util.Optional;

public interface CurrenciesRepository {

    Currencies save(Currencies currencies) throws SQLException;

    Iterable<Currencies> getAll();

    Optional<Currencies> getByCode(String code);
}
