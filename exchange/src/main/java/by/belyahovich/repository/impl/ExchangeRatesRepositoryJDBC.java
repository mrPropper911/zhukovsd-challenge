package by.belyahovich.repository.impl;

import by.belyahovich.domain.Currencies;
import by.belyahovich.domain.ExchangeRates;
import by.belyahovich.repository.BasicConnectionPool;
import by.belyahovich.repository.ConnectionPool;
import by.belyahovich.repository.CrudRepository;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ExchangeRatesRepositoryJDBC implements CrudRepository<ExchangeRates> {

    private static final Logger log = Logger.getLogger(ExchangeRatesRepositoryJDBC.class);

    private static final ConnectionPool connectionPool;

    private static final String DB_URL;

    private static volatile ExchangeRatesRepositoryJDBC instance;

    private final CurrenciesRepositoryJDBC currenciesRepositoryJDBC = CurrenciesRepositoryJDBC.getInstance();

    static {
        try {
            InputStream resourceAsStream =
                    ExchangeRatesRepositoryJDBC.class.getResourceAsStream("/config.properties");
            Properties properties = new Properties();
            properties.load(resourceAsStream);
            DB_URL = properties.getProperty("db.url");
            connectionPool = BasicConnectionPool.create(DB_URL);
        } catch (IOException e) {
            log.error("Properties load exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static ExchangeRatesRepositoryJDBC getInstance() {
        ExchangeRatesRepositoryJDBC localInstance = instance;
        if (localInstance == null) {
            synchronized (ExchangeRatesRepositoryJDBC.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ExchangeRatesRepositoryJDBC();
                }
            }
        }
        return localInstance;
    }

    @Override
    public Iterable<ExchangeRates> getAll() throws SQLException {
        List<ExchangeRates> exchangeRatesList = new ArrayList<>(Collections.emptyList());
        Connection connection = connectionPool.getConnection();

        try {
            String querySelectAll = """
                    select e.id,
                            base_currency.id as "base_currency_id",
                            base_currency.full_name as "base_currency_full_name",
                            base_currency.code as "base_currency_code",
                            base_currency.full_name as "base_currency_sign",
                           target_currency.id as "target_currency_id",
                           target_currency.full_name as "target_currency_full_name",
                           target_currency.code as "target_currency_code",
                           target_currency.full_name as "target_currency_sign",
                           e.rate
                    from exchange_rates e
                     join currencies base_currency on base_currency.id = e.base_currency_id
                    join currencies target_currency on target_currency.id = e.target_currency_id
                     """;

            PreparedStatement preparedStatement = connection.prepareStatement(querySelectAll);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ExchangeRates exchangeRates = ExchangeRates.newBuilder()
                        .setId(resultSet.getInt("id"))
                        .setBaseCurrency(
                                Currencies.newBuilder()
                                        .setId(resultSet.getInt("base_currency_id"))
                                        .setFullName(resultSet.getString("base_currency_full_name"))
                                        .setCode(resultSet.getString("base_currency_code"))
                                        .setSign(resultSet.getString("base_currency_sign"))
                                        .build())
                        .setTargetCurrency(
                                Currencies.newBuilder()
                                        .setId(resultSet.getInt("target_currency_id"))
                                        .setFullName(resultSet.getString("target_currency_full_name"))
                                        .setCode(resultSet.getString("target_currency_code"))
                                        .setSign(resultSet.getString("target_currency_sign"))
                                        .build())
                        .setRate(resultSet.getDouble("rate"))
                        .build();
                exchangeRatesList.add(exchangeRates);
            }
        } finally {
            connectionPool.releaseConnection(connection);
        }
        return exchangeRatesList;
    }

    @Override
    public Optional<ExchangeRates> getByCode(String code) throws SQLException {
        return Optional.empty();
    }

    @Override
    public Currencies save(ExchangeRates currencies) throws SQLException {
        return null;
    }
}
