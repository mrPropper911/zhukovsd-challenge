package by.belyahovich.service;

import by.belyahovich.domain.ExchangeRates;
import by.belyahovich.dto.ExchangeRatesResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ExchangeRatesService {

    List<ExchangeRatesResponse> getAll() throws SQLException;

    Optional<ExchangeRatesResponse> getByCode(String code) throws SQLException;
}
