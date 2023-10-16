package by.belyahovich;

import by.belyahovich.domain.Currencies;
import by.belyahovich.repository.impl.CurrenciesRepositoryJDBC;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        CurrenciesRepositoryJDBC currenciesRepositoryJDBC = new CurrenciesRepositoryJDBC();
        CurrenciesRepositoryJDBC.initTables();
        CurrenciesRepositoryJDBC.initCurrenciesData();
        CurrenciesRepositoryJDBC.initExchangeRatesData();
        currenciesRepositoryJDBC.save(Currencies.newBuilder()
                .setCode("BLR")
                        .setFullName("RB")
                        .setSign("()_()")
                .build());

        Optional<Currencies> usd = currenciesRepositoryJDBC.getByCode("USD");
        int a =1;
    }
}