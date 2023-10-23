package by.belyahovich.repository.impl;

import by.belyahovich.domain.Currencies;
import by.belyahovich.repository.CurrenciesRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.sqlite.JDBC;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CurrenciesRepositoryJDBC implements CurrenciesRepository {

    private static final String DB_URL = "jdbc:sqlite:D:\\CodeProgram\\zhukovsd-challenge\\exchange\\src\\main\\resources\\exchange.db";
    private static final Logger log = LogManager.getLogger(CurrenciesRepositoryJDBC.class);
    private static Connection connection;
    private static volatile CurrenciesRepositoryJDBC instance;

    static {
        try {
            java.sql.DriverManager.registerDriver(new JDBC());
        } catch (SQLException E) {
            throw new RuntimeException("Can't register driver!");
        }
    }


    public static CurrenciesRepositoryJDBC getInstance() {
        CurrenciesRepositoryJDBC localInstance = instance;
        if (localInstance == null) {
            synchronized (CurrenciesRepositoryJDBC.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new CurrenciesRepositoryJDBC();
                }
            }
        }
        return localInstance;
    }


    public static void initTables() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            String queryDropTableCurrencies = """
                    drop table if exists currencies
                    """;
            String queryDropTableExchangeRates = """
                    drop table if exists exchange_rates
                    """;
            String queryCreateCurrencies = """
                    create table currencies(
                        id integer primary key autoincrement,
                        code varchar unique,
                        full_name varchar,
                        sign varchar
                    )""";
            String queryCreateExchangeRate = """
                    create table exchange_rates(
                        id integer primary key autoincrement,
                        base_currency_id integer,
                        target_currency_id integer,
                        rate decimal(6),
                        foreign key (base_currency_id) references currencies (id),
                        foreign key (target_currency_id) references currencies (id)
                    )""";

            Statement statement = connection.createStatement();
            statement.addBatch(queryDropTableCurrencies);
            statement.addBatch(queryDropTableExchangeRates);
            statement.addBatch(queryCreateCurrencies);
            statement.addBatch(queryCreateExchangeRate);
            statement.executeBatch();

            log.info("Init tables successful");
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void initCurrenciesData() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            String queryInsert = """
                    insert into currencies (code, full_name, sign) VALUES (?, ?, ?)
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(queryInsert);
            preparedStatement.setString(1, "AUD");
            preparedStatement.setString(2, "Australian dollar");
            preparedStatement.setString(3, "A$");
            preparedStatement.addBatch();

            preparedStatement.setString(1, "USD");
            preparedStatement.setString(2, "United States dollar");
            preparedStatement.setString(3, "$");
            preparedStatement.addBatch();

            preparedStatement.setString(1, "EUR");
            preparedStatement.setString(2, "Euro");
            preparedStatement.setString(3, "€");
            preparedStatement.addBatch();

            preparedStatement.setString(1, "RUB");
            preparedStatement.setString(2, "Russian Ruble");
            preparedStatement.setString(3, "₽");
            preparedStatement.addBatch();

            preparedStatement.setString(1, "CNY");
            preparedStatement.setString(2, "Yuan");
            preparedStatement.setString(3, "¥");
            preparedStatement.addBatch();

            preparedStatement.executeBatch();
            log.info("Init table currencies successful");
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void initExchangeRatesData() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            String queryInsert = """
                    insert into exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(queryInsert);
            //USD -> EUR
            preparedStatement.setInt(1, 2);
            preparedStatement.setInt(2, 3);
            preparedStatement.setDouble(3, 0.95);
            preparedStatement.addBatch();
            //RUB -> EUR
            preparedStatement.setInt(1, 4);
            preparedStatement.setInt(2, 3);
            preparedStatement.setDouble(3, 0.0097);
            preparedStatement.addBatch();
            //RUB -> CNY
            preparedStatement.setInt(1, 4);
            preparedStatement.setInt(2, 5);
            preparedStatement.setDouble(3, 0.075);
            preparedStatement.addBatch();
            //USD -> AUD
            preparedStatement.setInt(1, 2);
            preparedStatement.setInt(2, 1);
            preparedStatement.setDouble(3, 1.58);
            preparedStatement.addBatch();

            preparedStatement.executeBatch();
            log.info("Init table exchange_rates successful");
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Currencies currencies) {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(false);
            String querySaveCurrencies = """
                    insert into currencies (code, full_name, sign) VALUES (?, ?, ?)
                    """;
            PreparedStatement preparedStatement =
                    connection.prepareStatement(querySaveCurrencies, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, currencies.getCode());
            preparedStatement.setString(2, currencies.getFullName());
            preparedStatement.setString(3, currencies.getSign());
            preparedStatement.execute();
            connection.commit();

            log.info("Transaction commit");
            log.info("Save currencies succeed: " + currencies.getCode());
        } catch (SQLException e) {
            try {
                connection.rollback();
                log.error("Transaction rollback");
                log.error(e.getMessage());
            } catch (Exception ex) {
                log.error("Transaction rollback exception");
                ex.printStackTrace();
            }
        }
    }

    @Override
    public Iterable<Currencies> getAll() {
        List<Currencies> currenciesList = new ArrayList<>(Collections.emptyList());
        try {
            connection = DriverManager.getConnection(DB_URL);

            String querySelectAll = """
                    select * from currencies
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(querySelectAll);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                currenciesList.add(Currencies.newBuilder()
                        .setId(resultSet.getInt("id"))
                        .setCode(resultSet.getString("code"))
                        .setFullName(resultSet.getString("full_name"))
                        .setSign(resultSet.getString("sign"))
                        .build());
            }
            log.info("Get all currency successful");
        } catch (SQLException e) {
            throw new RuntimeException();
        }
        return currenciesList;
    }

    @Override
    public Optional<Currencies> getByCode(String code) {
        Currencies currencies = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
            String querySelectByCode = """
                    select * from currencies where code = (?)
                    """;

            PreparedStatement preparedStatement = connection.prepareStatement(querySelectByCode);
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                return Optional.empty();
            }

            currencies = Currencies.newBuilder()
                    .setId(resultSet.getInt("id"))
                    .setCode(resultSet.getString("code"))
                    .setFullName(resultSet.getString("full_name"))
                    .setSign(resultSet.getString("sign"))
                    .build();
            log.info("Get by code successful: " + code);

        } catch (SQLException e) {
            log.error("Error Get by code:" + code);
            log.error(e.getMessage());
            throw new RuntimeException();
        }

        return Optional.ofNullable(currencies);
    }
}
