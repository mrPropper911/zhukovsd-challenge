package by.belyahovich.service.impl;

import by.belyahovich.domain.Currencies;
import by.belyahovich.domain.ExchangeRates;
import by.belyahovich.dto.ExchangeRatesMapper;
import by.belyahovich.dto.ExchangeRatesResponse;
import by.belyahovich.repository.CrudRepository;
import by.belyahovich.repository.impl.ExchangeRatesRepositoryJDBC;
import by.belyahovich.service.ExchangeRatesService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesServiceImpl  implements ExchangeRatesService {

    private final CrudRepository<ExchangeRates> exchangeRatesCrudRepository;

    public ExchangeRatesServiceImpl() {
        exchangeRatesCrudRepository = ExchangeRatesRepositoryJDBC.getInstance();
    }

    @Override
    public List<ExchangeRatesResponse> getAll() throws SQLException {
        List<ExchangeRatesResponse> exchangeRatesList = new ArrayList<>();
        exchangeRatesCrudRepository.getAll()
                .forEach(exchangeRates -> exchangeRatesList.add(ExchangeRatesMapper.
                        INSTANCE.exchangeRatesToExchangeRatesResponse(exchangeRates)));
        return exchangeRatesList;
    }
}
