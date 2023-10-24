package by.belyahovich.service;

import by.belyahovich.domain.Currencies;
import by.belyahovich.dto.CurrenciesRequest;
import by.belyahovich.dto.CurrenciesResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CurrenciesService {
    List<Currencies> getAll();

    Optional<Currencies> getByCode(String code);//todo return CurrenciesResponse

    CurrenciesResponse save(CurrenciesRequest currenciesRequest) throws SQLException;
}
