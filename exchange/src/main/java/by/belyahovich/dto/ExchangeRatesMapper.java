package by.belyahovich.dto;

import by.belyahovich.domain.ExchangeRates;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {CurrenciesMapper.class})
public interface ExchangeRatesMapper {
    ExchangeRatesMapper INSTANCE = Mappers.getMapper(ExchangeRatesMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "baseCurrency", target = "baseCurrency"),
            @Mapping(source = "targetCurrency", target = "targetCurrency"),
            @Mapping(source = "rate", target = "rate")
    })
    ExchangeRatesResponse exchangeRatesToExchangeRatesResponse(ExchangeRates exchangeRates);

    ExchangeRatesRequest exchangeRatesToExchangeRatesRequest(ExchangeRates exchangeRates);

    ExchangeRates exchangeRatesResponseToExchangeRates(ExchangeRatesResponse exchangeRatesResponse);

    ExchangeRates exchangeRatesRequestToExchangeRates(ExchangeRatesRequest exchangeRatesRequest);
}

