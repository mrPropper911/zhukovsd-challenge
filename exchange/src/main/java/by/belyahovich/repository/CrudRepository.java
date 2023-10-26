package by.belyahovich.repository;

import by.belyahovich.domain.Currencies;

import java.sql.SQLException;
import java.util.Optional;

public interface CrudRepository<T> {

    Iterable<T> getAll() throws SQLException;

    Optional<T> getByCode(String code) throws SQLException;

    Currencies save(T currencies) throws SQLException;
}
