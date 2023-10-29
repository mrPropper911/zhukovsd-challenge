package by.belyahovich.service;

import by.belyahovich.domain.ExchangeRates;
import by.belyahovich.dto.ExchangeRatesResponse;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRatesService {

    List<ExchangeRatesResponse> getAll() throws SQLException;
}
