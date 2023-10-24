package by.belyahovich.dto;

import by.belyahovich.domain.Currencies;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrenciesMapper {
    CurrenciesMapper INSTANCE = Mappers.getMapper(CurrenciesMapper.class);

    @Mappings({
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "sign", target = "sign"),
            @Mapping(source = "fullName", target = "name")
    })
    CurrenciesRequest currenciesToCurrenciesRequest(Currencies currencies);

    @Mappings({
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "name", target = "fullName"),
            @Mapping(source = "sign", target = "sign")
    })
    Currencies currenciesRequestToCurrencies(CurrenciesRequest currenciesRequest);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "fullName", target = "name"),
            @Mapping(source = "sign", target = "sign")
    })
    CurrenciesResponse curremciesToCurrenciesResponse(Currencies currencies);

}
