package by.belyahovich.service.impl;

import by.belyahovich.domain.Currencies;
import by.belyahovich.dto.CurrenciesMapper;
import by.belyahovich.dto.CurrenciesRequest;
import by.belyahovich.dto.CurrenciesResponse;
import by.belyahovich.repository.CrudRepository;
import by.belyahovich.repository.impl.CurrenciesRepositoryJDBC;
import by.belyahovich.service.CurrenciesService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrenciesServiceImpl implements CurrenciesService {

    private final CrudRepository<Currencies> currenciesRepository;

    public CurrenciesServiceImpl() {
        currenciesRepository = CurrenciesRepositoryJDBC.getInstance();
    }

    @Override
    public List<CurrenciesResponse> getAll() throws SQLException {
        List<CurrenciesResponse> currenciesResponseList = new ArrayList<>();
        currenciesRepository.getAll()
                .forEach(currencies ->
                        currenciesResponseList.add(CurrenciesMapper.INSTANCE.currenciesToCurrenciesResponse(currencies)));
        return currenciesResponseList;
    }

    @Override
    public Optional<CurrenciesResponse> getByCode(String code) throws SQLException {
        Optional<Currencies> optionalCurrencies = currenciesRepository.getByCode(code);
        return optionalCurrencies.map(CurrenciesMapper.INSTANCE::currenciesToCurrenciesResponse);
    }

    @Override
    public CurrenciesResponse save(CurrenciesRequest currenciesRequest) throws SQLException {
        Currencies currencies = CurrenciesMapper.INSTANCE.currenciesRequestToCurrencies(currenciesRequest);
        Currencies savedCurrency = currenciesRepository.save(currencies);
        return CurrenciesMapper.INSTANCE.currenciesToCurrenciesResponse(savedCurrency);
    }
}
