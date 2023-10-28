package by.belyahovich.service;

import by.belyahovich.dto.CurrenciesRequest;
import by.belyahovich.dto.CurrenciesResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CurrenciesService {
    List<CurrenciesResponse> getAll() throws SQLException;

    Optional<CurrenciesResponse> getByCode(String code) throws SQLException;

    CurrenciesResponse save(CurrenciesRequest currenciesRequest) throws SQLException;
}
