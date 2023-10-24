package by.belyahovich.service.impl;

import by.belyahovich.domain.Currencies;
import by.belyahovich.dto.CurrenciesMapper;
import by.belyahovich.dto.CurrenciesRequest;
import by.belyahovich.dto.CurrenciesResponse;
import by.belyahovich.repository.CurrenciesRepository;
import by.belyahovich.repository.impl.CurrenciesRepositoryJDBC;
import by.belyahovich.service.CurrenciesService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesServiceImpl implements CurrenciesService {

    private final CurrenciesRepository currenciesRepository = CurrenciesRepositoryJDBC.getInstance();


    @Override
    public List<Currencies> getAll() {
        List<Currencies> currenciesList = new ArrayList<>();
        currenciesRepository.getAll().forEach(currenciesList::add);
        return currenciesList;
    }

    @Override
    public Optional<Currencies> getByCode(String code) {
        return currenciesRepository.getByCode(code);
    }

    @Override
    public CurrenciesResponse save(CurrenciesRequest currenciesRequest) throws SQLException {
        Currencies currencies = CurrenciesMapper.INSTANCE.currenciesRequestToCurrencies(currenciesRequest);
        Currencies savedCurrency = currenciesRepository.save(currencies);
        return CurrenciesMapper.INSTANCE.curremciesToCurrenciesResponse(savedCurrency);
    }
}
